package org.nettyhws.nettyhws.i;

import io.netty.channel.ChannelHandlerContext;

/**
 * 用于向客户端返回消息的接口。
 * @author thenk008,echosun
 */
public interface RequestManager {
	/**
	 * 重写该方法，完成对客户端的响应。
	 *
	 * @param channelHandlerContext ChannelHandlerContext
	 * @param msg 返回消息
	 * @param httpCode HttpCode 类中规定的返回码。
	 */
	void response(ChannelHandlerContext channelHandlerContext,String msg,byte httpCode);
}
