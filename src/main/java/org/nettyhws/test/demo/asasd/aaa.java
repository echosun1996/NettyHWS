package org.nettyhws.test.demo.asasd;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.Http;
import org.nettyhws.nettyhws.annotations.HttpMapping;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.i.Myboss;

import java.util.Map;

@HttpMapping("/hasNo/")
public class aaa implements Myboss {
    private Http http;
    private ChannelHandlerContext channelHandlerContext;
    @Override
    public void body(String s, Map<Object, Object> map) {
        http.response(channelHandlerContext,"ok123", HttpCode.OK);
    }

    @Override
    public void head(Http http, ChannelHandlerContext channelHandlerContext) {
        this.http=http;
        this.channelHandlerContext=channelHandlerContext;

    }
}
