package org.nettyhws.nettyhws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author thenk008,echosun
 */
public abstract class HttpSon {
	private  Class<?> httpBoss;

	public Class<?> getHttpBoss() {
		return httpBoss;
	}

	void setHttpBoss(Class<?> httpBoss) {
		this.httpBoss = httpBoss;
	}

	void body(String body, Map<Object, Object> map, Http web, ChannelHandlerContext ch) {
		try {
			Method me = httpBoss.getMethod("body", String.class, Map.class);
			Method you = httpBoss.getMethod("head", Http.class, ChannelHandlerContext.class);
			Object obj = httpBoss.newInstance();
			you.invoke(obj, web, ch);
			me.invoke(obj, body, map);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
