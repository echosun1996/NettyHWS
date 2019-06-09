package org.nettyhws.nettyhws.tools;

import java.util.HashMap;
import java.util.Map;

import org.nettyhws.nettyhws.agreement.HttpMessage;
import org.nettyhws.nettyhws.log.SystemLog;

/**
 * 用于处理 HTTP 请求，提取其中的参数。
 *
 * @author thenk008,echosun
 */
public class ShareCon {
	public HttpMessage getShareMessage(String uri, String bodyMessage) {
		Map<Object, Object> map = new HashMap<>();
		String params;
		// 存在GET参数
		if (uri.indexOf("?") > 0) {
			params = uri.split("\\?")[1];
			uri = uri.split("\\?")[0];
			String[] names = params.split("&");
			for (String name : names) {
				String[] nameAndValue = name.split("=");
				map.put(nameAndValue[0], nameAndValue[1]);
			}
		}
		SystemLog.DEBUG("URI",uri);
		HttpMessage share = new HttpMessage();
		share.setBody(bodyMessage);
		share.setUri(uri);
		share.setParams(map);
		return share;
	}
}
