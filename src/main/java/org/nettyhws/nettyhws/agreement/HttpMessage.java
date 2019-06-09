package org.nettyhws.nettyhws.agreement;

import java.util.Map;

/**
 * 用于保存 HTTP 协议的信息和携带的参数。
 * @author thenk008,echosun
 */
public class HttpMessage {
	/**
	 * HTTP 请求 URI 。
	 */
	private String uri;

	/**
	 * HTTP 请求体。
	 */
	private String body;

	/**
	 * HTTP 请求参数。
	 */
	private Map<Object, Object> params;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<Object, Object> getParams() {
		return params;
	}

	public void setParams(Map<Object, Object> params) {
		this.params = params;
	}
}
