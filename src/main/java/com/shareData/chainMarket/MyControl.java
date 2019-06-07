package com.shareData.chainMarket;

import com.shareData.chainMarket.agreement.ShareMessage;

import io.netty.channel.ChannelHandlerContext;

public class MyControl extends MySon {

	public boolean my(ShareMessage share, Http http, ChannelHandlerContext ch) {
		Class<?> c;
		boolean isRight = true;
		try {
			c = Class.forName(share.getUri());
			setMyboss(c);
			body(share.getBody(),share.getParams(), http, ch);
		} catch (ClassNotFoundException e) {
			isRight = false;
		}
		return isRight;
	}

}