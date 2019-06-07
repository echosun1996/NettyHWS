package com.shareData.chainMarket.i;

import io.netty.channel.ChannelHandlerContext;

public interface RequestManager {
	//返回管理后台登录用户信息
	void response(ChannelHandlerContext ch,String msg,byte httpCode);
	
}
