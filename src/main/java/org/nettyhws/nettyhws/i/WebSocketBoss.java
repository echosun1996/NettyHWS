package org.nettyhws.nettyhws.i;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.ChannelInboundHandler;

/**
 * WebSocket 接口。
 *
 * @author echosun
 */
public interface WebSocketBoss {
    /**
     * 重写该方法，完成对传入参数的处理。
     *
     * @param channelInboundHandler ChannelInboundHandler
     * @param channelHandlerContext ChannelHandlerContext
     * @param message WebSocket 消息
     */
    void webSocketReceiver(ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext,String message);
}
