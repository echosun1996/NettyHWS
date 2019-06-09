package org.nettyhws.nettyhws.def;

import java.util.Map;

/**
 * 保存控制器路径
 *
 * @author echosun
 */
public class ResConfig {
	private static final ResConfig CONFIG = new ResConfig();
	private static Map<String, Class> httpClassMap;
	private static Map<String, Class> webSocketClassMap;

	private ResConfig() {
	}

	public Map<String, Class> getHttpController() {
		return httpClassMap;
	}

	public void setWebSocketController(Map<String, Class> webSocketClassMap) {
		ResConfig.webSocketClassMap = webSocketClassMap;
	}

	public Map<String, Class> getWebSocketController() {
		return webSocketClassMap;
	}

	public void setHttpController(Map<String, Class> httpClassMap) {
		ResConfig.httpClassMap = httpClassMap;
	}


	public static ResConfig get() {
		return CONFIG;
	}
}
