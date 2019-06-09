package org.nettyhws.nettyhws.son;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.ChannelInboundHandler;

/**
 * 抽象类，用于执行 HttpBoss 方法。
 *
 * @author thenk008,echosun
 */
public abstract class AbstractHttpBoss {
	private  Class<?> httpBoss;

	protected void setHttpBoss(Class<?> httpBoss) {
		this.httpBoss = httpBoss;
	}

	protected void invokeHttpBoss(String postBodyString, Map<Object, Object> getMap, ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext) {
		try {
			Method httpReceiverMethod = httpBoss.getMethod("httpReceiver", ChannelInboundHandler.class, ChannelHandlerContext.class,String.class, Map.class);
			Object obj = httpBoss.newInstance();
			httpReceiverMethod.invoke(obj, channelInboundHandler, channelHandlerContext,postBodyString, getMap);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
