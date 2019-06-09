package org.nettyhws.nettyhws.control;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.ChannelInboundHandler;
import org.nettyhws.nettyhws.def.Mapping;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.son.AbstractWebSocketBoss;
import java.util.Map;

/**
 * WebSocket 控制器。
 * 使用反射，处理 URI 请求。
 *
 * @author thenk008,echosun
 */
public class WebSocketControl extends AbstractWebSocketBoss {
	public boolean webSocketController(String uri, ChannelInboundHandler channelInboundHandler, ChannelHandlerContext ch, String frameString) {
		Class<?> c;
		Map<String, Class> my = Mapping.get().getWebSocketController();
		// 从映射中查找路径是否有绑定处理类。
		if (my.get(uri) == null) {
			SystemLog.INFO(uri+" (WebSocketDemo URI Not Found.)");
			return false;
		}
		SystemLog.INFO("WebSocket "+uri);
		c = my.get(uri);
		setWebSocketSon(c);
		invokeWebSocketBoss(frameString,channelInboundHandler, ch);
		return true;
	}
}