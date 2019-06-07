package com.shareData.chainMarket.i;

import java.util.Map;

import com.shareData.chainMarket.Http;

import io.netty.channel.ChannelHandlerContext;

public interface Myboss {
//具体实现类
 public void body(String body,Map<Object,Object> param);
//初始化数据
 public void head(Http web ,ChannelHandlerContext ch);
}
