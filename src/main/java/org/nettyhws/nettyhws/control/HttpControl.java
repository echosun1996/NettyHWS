package org.nettyhws.nettyhws.control;

import org.nettyhws.nettyhws.ChannelInboundHandler;
import org.nettyhws.nettyhws.agreement.HttpMessage;
import io.netty.channel.ChannelHandlerContext;
import org.nettyhws.nettyhws.def.Mapping;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.son.AbstractHttpBoss;
import java.util.Map;

/**
 * HTTP 控制器。
 * 使用反射，处理 URI 请求。
 *
 * @author thenk008,echosun
 */
public class HttpControl extends AbstractHttpBoss {
	public boolean httpController(HttpMessage share, ChannelInboundHandler http, ChannelHandlerContext ch) {
		Class<?> c;
		Map<String, Class> my= Mapping.get().getHttpController();
		// 从映射中查找路径是否有绑定处理类。
		if(my.get(share.getUri())==null){
			SystemLog.INFO(share.getUri()+" (http URI Not Found.)");
			return false;
		}
		SystemLog.INFO("HTTP "+share.getUri());
		c = my.get(share.getUri());
		setHttpBoss(c);
		invokeHttpBoss(share.getBody(),share.getParams(), http, ch);
		return true;
	}
}