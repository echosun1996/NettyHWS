package org.nettyhws.nettyhws.son;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.ChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 抽象类，用于执行 WebSocketBoss 方法。
 *
 * @author thenk008,echosun
 */
public abstract class AbstractWebSocketBoss {
	private  Class<?> webSocketBoss;

	protected void setWebSocketSon(Class<?> webSocketBoss) {
		this.webSocketBoss = webSocketBoss;
	}

	protected void invokeWebSocketBoss(String frameString, ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext) {
		try {
			Method webSocketReceiverMethod = webSocketBoss.getMethod("webSocketReceiver", ChannelInboundHandler.class, ChannelHandlerContext.class, String.class);
			Object instance = webSocketBoss.newInstance();
			webSocketReceiverMethod.invoke(instance, channelInboundHandler, channelHandlerContext,frameString);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
