package org.nettyhws.nettyhws;

import org.nettyhws.nettyhws.agreement.ShareMessage;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.log.SystemLog;

/**
 * @author thenk008,echosun
 */
public class MyControl extends MySon {

	public boolean httpController(ShareMessage share, Http http, ChannelHandlerContext ch) {
		Class<?> c;
		boolean isRight = true;
		try {
			c = Class.forName(share.getUri());
			setMyboss(c);
			body(share.getBody(),share.getParams(), http, ch);
		} catch (ClassNotFoundException e) {
			isRight = false;
		}
		return isRight;
	}
	public boolean webSocketController(Http http, ChannelHandlerContext ch) {
		// TODO 这里需要使用反射
		boolean isRight = true;
		SystemLog.INFO("发送消息");
		http.response(ch,"WebSocket Server Send", HttpCode.WEB_SOCKET);

		return isRight;
	}
}