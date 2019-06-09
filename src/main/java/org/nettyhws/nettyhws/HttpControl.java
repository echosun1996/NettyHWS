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
public class HttpControl extends HttpSon {

	public boolean httpController(ShareMessage share, Http http, ChannelHandlerContext ch) {
		Class<?> c;
		boolean isRight = true;
		try {
			Map<String, Class> my=ResConfig.get().getHttpController();
			if(my.get(share.getUri())==null){
				SystemLog.INFO("Not Found");
				return false;
			}

			c = my.get(share.getUri());
			setHttpBoss(c);
			body(share.getBody(),share.getParams(), http, ch);
		} catch (Exception e) {
			isRight = false;
		}
		return isRight;
	}
//	public void webSocketController(String uri,Http http, ChannelHandlerContext ch) {
//		// TODO 这里需要使用反射
//		boolean isRight = true;
//		Class<?> c;
//
//		Map<String, Class> my = ResConfig.get().getWebSocketController();
//		if (my.get(uri) == null) {
//			SystemLog.INFO("Not Found");
//			return ;
//		}
//		c = my.get(uri);
//		setWebSocketBoss(c);
//
//		http.response(ch, "hello world！", HttpCode.WEB_SOCKET);
//		(c);
//
//
////		return isRight;
//	}
}