package org.nettyhws.test.demo.HttpDemo;
import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.ChannelInboundHandler;
import org.nettyhws.nettyhws.annotations.HttpMapping;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.i.HttpBoss;
import org.nettyhws.nettyhws.log.SystemLog;

import java.util.Map;

/**
 * @author echosun
 */
@HttpMapping("/HttpTest")
public class HttpDemo implements HttpBoss {
    @Override
    public void httpReceiver(ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext, String post, Map<Object, Object> param) {
        for(Object o:param.keySet()){
            System.out.println("Param: key= "+ o.toString() + " and value= " + param.get(o).toString());
        }
        System.out.println("POST: "+post);
        channelInboundHandler.response(channelHandlerContext,"Server:Receive HTTP Message!", HttpCode.OK);

    }
}
