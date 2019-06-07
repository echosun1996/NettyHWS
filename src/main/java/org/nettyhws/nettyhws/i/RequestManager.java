package org.nettyhws.nettyhws.i;

import io.netty.channel.ChannelHandlerContext;

/**
 * 回调函数
 * @author thenk008,echosun
 */
public interface RequestManager {
	/**
	 *
	 * @param ch ChannelHandlerContext
	 * @param msg String
	 * @param httpCode byte
	 */
	void response(ChannelHandlerContext ch,String msg,byte httpCode);
	
}
