package org.nettyhws.nettyhws;

import java.io.UnsupportedEncodingException;

import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import org.nettyhws.nettyhws.agreement.HttpMessage;
import org.nettyhws.nettyhws.constant.Config;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.control.HttpControl;
import org.nettyhws.nettyhws.control.WebSocketControl;
import org.nettyhws.nettyhws.i.RequestManager;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.tools.ShareCon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;

/**
 * 基于 Netty 实现协议。
 *
 * @author echosun
 */
public class ChannelInboundHandler extends SimpleChannelInboundHandler<Object> implements RequestManager {
	/**
	 * 服务绑定端口。
	 */
	private int port;

	/**
	 * 构造函数，传入服务绑定端口。
	 *
	 * @param port 服务端口号
	 */
	public ChannelInboundHandler(int port) {
		this.port = port;
	}

	private WebSocketServerHandshaker handshake;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 用户出现异常 ,返回业务异常提醒。
		SystemLog.ERROR("NettyHWS-出现用户业务异常!");
		response(ctx, null, HttpCode.SERVER_ERROR);
		super.exceptionCaught(ctx, cause);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		SystemLog.DEBUG("channelRead0-ctx",ctx.toString());
		SystemLog.DEBUG("channelRead0-msg",msg.toString());
		// 收到 HTTP 数据包后，转交给 handleHttpRequest 处理。
		// 注意！WebSocketDemo 建立链接第一步，需要发送 HTTP 数据包。
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		}
		// 收到 WebSocketDemo 数据包后，转交给 handleWebSocketFrame 处理。
		else if (msg instanceof WebSocketFrame) {
			WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
			if(webSocketFrame.content().readableBytes()<Config.MAX_CONTENT_LENGTH){
				handleWebSocketFrame(ctx, webSocketFrame);
			}
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext context, WebSocketFrame frame) throws Exception {
		// 判断是否是关闭链路的指令。
		if (frame instanceof CloseWebSocketFrame) {
			handshake.close(context.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否是PING消息。
		if (frame instanceof PingWebSocketFrame) {
			context.channel().write(new PongWebSocketFrame(frame.content().retain()));
		}
		// 如果消息不是文本消息则抛出异常。
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new Exception(String.format("%s down", frame.getClass().getName()));
		}
		// 将 WebSocketDemo 传输的消息转为字符串。
		String frameString = ((TextWebSocketFrame) frame).text();
		// 获取 WebSocketDemo 请求的 URI。
		String uri=context.channel().attr(AttributeKey.valueOf("URI")).get().toString();
		SystemLog.DEBUG("WebSocketDemo URI",uri);
		// 初始化一个 WebSocketDemo 控制器。
		WebSocketControl control = new WebSocketControl();
		boolean requestResult = control.webSocketController(uri,this,context,frameString);
		SystemLog.DEBUG("WebSocketDemo 传入",frameString);
		if (!requestResult) {
			response(context, null, HttpCode.NOT_FOUND);
		}
	}

	private void handleHttpRequest(ChannelHandlerContext context, FullHttpRequest fullHttpRequest) throws Exception {
		// 请求头没有 Upgrade 字段，则为一个 HTTP 请求。
		if (fullHttpRequest.headers().get("Upgrade") == null) {
			// 初始化一个 HTTP 请求信息类。
			ShareCon share = new ShareCon();
			if (!fullHttpRequest.decoderResult().isSuccess()) {
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
				return;
			}
			// 大于信息包最大长度。
			if (fullHttpRequest.content().readableBytes() > Config.MESSAGE_MAX) {
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
				System.out.println("请求体过长");
				return;
			}
			ByteBuf fu;
			// 读取POST数据。
			fu = fullHttpRequest.content();
			// 创建字节数组。
			byte[] bodyByte = new byte[fu.readableBytes()];
			// 将缓存区内容读取到字节数组中。
			fu.readBytes(bodyByte);
			// 将字节数组内容转化为字符串。
			String body = new String(bodyByte, "UTF-8");
			HttpMessage message = share.getShareMessage(fullHttpRequest.uri(), body);
			// 初始化一个 HTTP 控制器
			HttpControl control = new HttpControl();
			// 使用反射处理消息
			boolean requestResult= control.httpController(message, this, context);
			if (!requestResult) {
				response(context, null, HttpCode.NOT_FOUND);
			}
		}
		// 有 Upgrade 字段，则表明为 WebSocketDemo 握手包。
		else{
			HttpMethod method=fullHttpRequest.method();
			String uri=fullHttpRequest.uri();
			// 保存 WebSocketDemo 请求的 URI 。
			if(method==HttpMethod.GET){
				SystemLog.INFO("检测到 WebSocketDemo 请求，路径为: "+uri);
				context.channel().attr(AttributeKey.valueOf("URI")).set(uri);
			}
			SystemLog.INFO("执行 WebSocketDemo 握手");
			//注意：这里的"/websocket"没有意义。WS协议消息的接收不受这里控制。
			WebSocketServerHandshakerFactory ws = new WebSocketServerHandshakerFactory("ws://127.0.0.1:"+port+"/websocket",
					null, false);
			handshake = ws.newHandshaker(fullHttpRequest);
			if (handshake == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
			} else {
				handshake.handshake(context.channel(), fullHttpRequest);
			}
		}
	}

	@Override
	public void response(ChannelHandlerContext channelHandlerContext, String msg, byte httpCode) {
		try {
			FullHttpResponse response;
			switch (httpCode) {
			case HttpCode.NOT_FOUND:
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
				break;
			case HttpCode.SERVER_ERROR:
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
				break;
			case HttpCode.OK:
				ByteBuf byteBuf ;
				byte[] msgBytes = msg.getBytes("UTF-8");
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
				response.headers().set("CONTENT_TYPE", "text/html;charset=UTF-8");
				response.headers().set("Access-Control-Allow-Origin", "*");
				byteBuf = response.content();
				byteBuf.writeBytes(msgBytes);
				break;
			default:
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED);
			}

			if(HttpCode.WEB_SOCKET!=httpCode) {
				channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			}
			else{
				channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(msg));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
