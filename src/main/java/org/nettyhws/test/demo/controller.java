package org.nettyhws.test.demo;
import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.Http;
//import org.nettyhws.nettyhws.MyChannelInboundHandler;
import org.nettyhws.nettyhws.annotations.HttpMapping;
import org.nettyhws.nettyhws.constant.HttpCode;
import org.nettyhws.nettyhws.i.HttpBoss;

import java.util.Map;

@HttpMapping("/has")
public class controller implements HttpBoss {
    private Http http;
    private ChannelHandlerContext channelHandlerContext;
    @Override
    public void body(String s, Map<Object, Object> map) {
        http.response(channelHandlerContext,"ok123", HttpCode.OK);
        System.out.println("++++"+s);
        for(Object o:map.keySet()){
            System.out.println("+++key= "+ o.toString() + " and value= " + map.get(o).toString());
        }
        System.out.println("+++++"+map);
    }

    @Override
    public void head(Http http, ChannelHandlerContext channelHandlerContext) {
        this.http=http;
        this.channelHandlerContext=channelHandlerContext;
        System.out.println("====="+http.toString());
        System.out.println("====="+channelHandlerContext.toString());


    }
}
