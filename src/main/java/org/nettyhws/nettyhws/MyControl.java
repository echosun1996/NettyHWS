package org.nettyhws.nettyhws;

import org.nettyhws.nettyhws.agreement.ShareMessage;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.def.ResConfig;
import org.nettyhws.nettyhws.log.SystemLog;

import java.util.Map;

/**
 * @author thenk008,echosun
 */
public class MyControl extends MySon {

	public boolean httpController(ShareMessage share, Http http, ChannelHandlerContext ch) {
		Class<?> c;
		boolean isRight = true;
		try {
			Map<String, Class> my=ResConfig.get().getController();
			if(my.get(share.getUri())==null){
				SystemLog.INFO("Not Found");
				return false;
			}

			c = my.get(share.getUri());
			setMyboss(c);
			body(share.getBody(),share.getParams(), http, ch);
		} catch (Exception e) {
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