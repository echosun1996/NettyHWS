package org.nettyhws.nettyhws.i;

import java.util.Map;
import org.nettyhws.nettyhws.ChannelInboundHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * HTTP 接口。
 *
 * @author thenk008,echosun
 */
public interface HttpBoss {
    /**
     * 重写该方法，完成对传入参数的处理。
     *
     * @param channelInboundHandler ChannelInboundHandler
     * @param channelHandlerContext ChannelHandlerContext
     * @param body Post 请求内容
     * @param param Get 请求参数
     */
    void httpReceiver(ChannelInboundHandler channelInboundHandler , ChannelHandlerContext channelHandlerContext,String body,Map<Object,Object> param);
}
