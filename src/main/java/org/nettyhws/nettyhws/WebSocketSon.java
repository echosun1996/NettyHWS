package org.nettyhws.nettyhws;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author thenk008,echosun
 */
public abstract class WebSocketSon {
	private  Class<?> webSocketBoss;

	public Class<?> getWebSocketSun() {
		return webSocketBoss;
	}

	void setWebSocketSon(Class<?> webSocketBoss) {
		this.webSocketBoss = webSocketBoss;
	}

	void body(String body,  Http web, ChannelHandlerContext ch) {
		try {
			Method me = webSocketBoss.getMethod("body", String.class);
			Method you = webSocketBoss.getMethod("head", Http.class, ChannelHandlerContext.class);
			Object obj = webSocketBoss.newInstance();
			you.invoke(obj, web, ch);
			me.invoke(obj, body);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
