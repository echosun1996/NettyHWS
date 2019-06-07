package org.nettyhws.nettyhws.i;

import java.util.Map;

import org.nettyhws.nettyhws.Http;

import io.netty.channel.ChannelHandlerContext;

/**
 * 基于HTTP通信的接口
 *
 * @author thenk008,echosun
 */
public interface Myboss {
    /**
     * 具体实现类
     * @param body String
     * @param param Map<Object,Object>
     */
    void body(String body,Map<Object,Object> param);

    /**
     * 初始化数据
     * @param web Http
     * @param ch ChannelHandlerContext
     */
    void head(Http web ,ChannelHandlerContext ch);
}
