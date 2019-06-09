package org.nettyhws.test.demo.asasd;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.Http;
import org.nettyhws.nettyhws.annotations.WebSocketMapping;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.i.HttpBoss;
import org.nettyhws.nettyhws.i.WebSocketBoss;
import org.nettyhws.nettyhws.log.SystemLog;

import java.util.Map;

@WebSocketMapping("/hasNo")
public class aaa implements WebSocketBoss {
    private Http http;
    private ChannelHandlerContext channelHandlerContext;
    @Override
    public void body(String s) {
        SystemLog.INFO(s);
        http.response(channelHandlerContext,"Server:Receive WebSocket Message!", HttpCode.WEB_SOCKET);
    }

    @Override
    public void head(Http http, ChannelHandlerContext channelHandlerContext) {
        this.http=http;
        this.channelHandlerContext=channelHandlerContext;
    }
}









