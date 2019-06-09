package org.nettyhws.test.demo.WebSocketDemo;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.ChannelInboundHandler;
import org.nettyhws.nettyhws.annotations.WebSocketMapping;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.i.WebSocketBoss;

/**
 * @author echosun
 */
@WebSocketMapping("/WebSocketTest")
public class WebSocketDemo implements WebSocketBoss {
    @Override
    public void webSocketReceiver(ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext, String message) {
        System.out.println("WebSocket Message: "+message);
        channelInboundHandler.response(channelHandlerContext,"Server:Receive WebSocketDemo Message!", HttpCode.WEB_SOCKET);
    }
}









