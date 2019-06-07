package org.nettyhws.nettyhws;

import org.nettyhws.nettyhws.agreement.ShareMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author thenk008,echosun
 */
public class MyControl extends MySon {

	public boolean httpController(ShareMessage share, Http http, ChannelHandlerContext ch) {
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