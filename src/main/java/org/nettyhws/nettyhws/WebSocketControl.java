package org.nettyhws.nettyhws;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.agreement.ShareMessage;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.def.ResConfig;
import org.nettyhws.nettyhws.log.SystemLog;

import java.util.Map;

/**
 * @author thenk008,echosun
 */
public class WebSocketControl extends WebSocketSon {

	public void webSocketController(String uri,Http http, ChannelHandlerContext ch,String frameString) {
		// TODO 这里需要使用反射
		boolean isRight = true;
		Class<?> c;

		Map<String, Class> my = ResConfig.get().getWebSocketController();
		if (my.get(uri) == null) {
			SystemLog.INFO("Not Found");
			return ;
		}
		c = my.get(uri);
		setWebSocketSon(c);
		body(uri, http, ch);

//		return isRight;
	}
}