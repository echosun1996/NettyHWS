package org.nettyhws.nettyhws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author thenk008,echosun
 */
public abstract class MySon {
	private  Class<?> myboss;

	public Class<?> getMyboss() {
		return myboss;
	}

	void setMyboss(Class<?> mybos) {
		myboss = mybos;
	}

	void body(String body, Map<Object, Object> map, Http web, ChannelHandlerContext ch) {
		try {
			Method me = myboss.getMethod("body", String.class, Map.class);
			Method you = myboss.getMethod("head", Http.class, ChannelHandlerContext.class);
			Object obj = myboss.newInstance();
			you.invoke(obj, web, ch);
			me.invoke(obj, body, map);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
