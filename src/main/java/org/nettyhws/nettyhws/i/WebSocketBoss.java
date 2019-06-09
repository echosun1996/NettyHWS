package org.nettyhws.nettyhws.i;

import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.Http;

import java.util.Map;

public  interface WebSocketBoss {
    /**
     * 具体实现类
     *
     * @param message Map<Object,Object>
     */
    void body(String message);

    /**
     * 初始化数据
     *
     * @param web Http
     * @param ch  ChannelHandlerContext
     */
    void head(Http web, ChannelHandlerContext ch);
}
