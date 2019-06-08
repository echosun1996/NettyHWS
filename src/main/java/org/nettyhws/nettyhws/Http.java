package org.nettyhws.nettyhws;

import java.io.UnsupportedEncodingException;

import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.nettyhws.nettyhws.agreement.ShareMessage;
import org.nettyhws.nettyhws.constant.Config;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.i.RequestManager;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.tools.ShareCon;
import org.nettyhws.nettyhws.tools.ShareCon;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;

public class Http extends SimpleChannelInboundHandler<Object> implements RequestManager {
	// 建立 WebSocket 的端口
	private int port;

	public Http(int port) {
		this.port = port;
	}

// private RequestManager man ;

	private WebSocketServerHandshaker handshaker;
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 用户出现异常 ,返回业务异常提醒
		System.out.println("产生异常信息");
		response(ctx, null, HttpCode.SERVER_ERROR);
		super.exceptionCaught(ctx, cause);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, Object msg) throws Exception {
		if(Config.DEGUB) {
			SystemLog.DEBUG("channelRead0",msg.toString());
		}

		if (msg instanceof FullHttpRequest) {
			// 收到 HTTP 数据包后，转交给 handleHttpRequest 处理
			// Note! WebSocket 建立链接第一步，需要发送 HTTP 数据包。
			handleHttpRequest(arg0, (FullHttpRequest) msg);

		} else if (msg instanceof WebSocketFrame) {
			// 收到 WebSocket 数据包后，转交给 handleWebSocketFrame 处理
			WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
			if(webSocketFrame.content().readableBytes()<Config.MAX_CONTENT_LENGTH){
				handleWebSocketFrame(arg0, webSocketFrame);
			}
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ch, WebSocketFrame frame) throws Exception {
		// 判断是否是关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ch.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否是PING消息
		if (frame instanceof PingWebSocketFrame) {
			ch.channel().write(new PongWebSocketFrame(frame.content().retain()));
		}
		// 如果消息不是文本消息则抛出异常
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new Exception(String.format("%s down", frame.getClass().getName()));
		}
		String frameString = ((TextWebSocketFrame) frame).text();
		//TODO 增加 WebSocket 处理逻辑
		MyControl control = new MyControl();
		control.webSocketController(this,ch);
		if(Config.DEGUB){
			SystemLog.DEBUG("WebSocket",frameString);
		}
	}

	private void handleHttpRequest(ChannelHandlerContext arg0, FullHttpRequest fullHttpRequest) throws Exception {
		if(Config.DEGUB){
			SystemLog.DEBUG("ChannelHandlerContext",arg0.toString());
			SystemLog.DEBUG("FullHttpRequest",fullHttpRequest.toString());
		}

		// 请求头没有 Upgrade 字段，则为一个 HTTP 请求
		if (fullHttpRequest.headers().get("Upgrade") == null) {
			FullHttpResponse response;
			ShareCon share = new ShareCon();
			MyControl control = new MyControl();
			if (!fullHttpRequest.decoderResult().isSuccess()) {
				//response =
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
				return;
			}

			// 大于信息包最大长度
			if (fullHttpRequest.content().readableBytes() > Config.MESSAGE_MAX) {
				//response =
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
				System.out.println("请求体过长");
				return;
			}
			ByteBuf fu;// = Unpooled.directBuffer(4096)
			// 读取POST数据
			fu = fullHttpRequest.content();
			// 创建字节数组
			byte[] body = new byte[fu.readableBytes()];
			// 将缓存区内容读取到字节数组中
			fu.readBytes(body);
			String bodys = new String(body, "UTF-8");

			SystemLog.INFO(bodys);

			ShareMessage message = share.getShareMessage(fullHttpRequest.uri(), bodys);

			// 使用反射处理消息
			boolean bm = control.httpController(message, this, arg0);

			if (!bm) {
				response(arg0, null, HttpCode.NOT_FOUND);
			}
		}


		// 有 Upgrade 字段，则表明为 WebSocket 握手包
		else{
			SystemLog.INFO("执行 WebSocket 握手");
			WebSocketServerHandshakerFactory ws = new WebSocketServerHandshakerFactory("ws://127.0.0.1:"+port+"/websocket",
					null, false);
			handshaker = ws.newHandshaker(fullHttpRequest);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(arg0.channel());
			} else {
				handshaker.handshake(arg0.channel(), fullHttpRequest);
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
				ByteBuf byteBuf ;//= Unpooled.directBuffer(1024);
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
				channelHandlerContext.channel().writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
