package com.shareData.chainMarket.agreement;

import java.util.Map;

public class ShareMessage {
	private String uri;
	private String body;
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
