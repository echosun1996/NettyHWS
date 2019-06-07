package com.shareData.chainMarket;

import java.io.UnsupportedEncodingException;

import com.shareData.chainMarket.agreement.ShareMessage;
import com.shareData.chainMarket.constant.Config;
import com.shareData.chainMarket.constant.HttpCode;
import com.shareData.chainMarket.i.RequestManager;
import com.shareData.chainMarket.tools.ShareCon;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class Http extends SimpleChannelInboundHandler<Object> implements RequestManager {

	// private RequestManager man ;
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 用户出现异常 ,返回业务异常提醒
		System.out.println("产生异常信息");
		response(ctx, null, HttpCode.SERVER_ERROR);
		super.exceptionCaught(ctx, cause);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, Object msg) throws Exception {
		handleHttpRequest(arg0, (FullHttpRequest) msg);

	}

	private void handleHttpRequest(ChannelHandlerContext arg0, FullHttpRequest msg) throws Exception {// !msg.getDecoderResult().isSuccess()
		FullHttpResponse response;
		ShareCon share = new ShareCon();
		MyControl control = new MyControl();
		if (!msg.getDecoderResult().isSuccess()) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (msg.content().readableBytes() > Config.MESSAGE_MAX) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
			System.out.println("请求体过长");
			return;
		}
		ByteBuf fu = Unpooled.directBuffer(4096);
		// 读取POST数据
		fu = msg.content();
		// 创建字节数组
		byte[] body = new byte[fu.readableBytes()];
		// 将缓存区内容读取到字节数组中
		fu.readBytes(body);
		String bodys = new String(body, "UTF-8");
		ShareMessage message = share.getShareMessage(msg.getUri(), bodys);
		boolean bm = control.my(message, this, arg0);
		if (!bm) {
			response(arg0, null, HttpCode.NOT_FOUND);
		}
	}

	@Override
	public void response(ChannelHandlerContext ch, String msg, byte httpCode) {
		try {
			FullHttpResponse response;
			switch (httpCode) {
			case HttpCode.NOT_FOUND:
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
				break;
			case HttpCode.SERVER_ERROR:
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			default:
				ByteBuf bu = Unpooled.directBuffer(1024);
				byte[] lm = msg.getBytes("UTF-8");
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
				response.headers().set("CONTENT_TYPE", "text/html;charset=UTF-8");
				response.headers().set("Access-Control-Allow-Origin", "*");
				bu = response.content();
				bu.writeBytes(lm);
				break;
			}
			ch.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
