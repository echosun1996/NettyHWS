package org.nettyhws.nettyhws.def;

import java.util.Map;

/**
 * 保存控制器和对应路径关系的工厂类。
 *
 * @author echosun
 */
public class Mapping {
	private static final Mapping MAPPING = new Mapping();
	/**
	 * 存储 HTTP 映射。
	 */
	private static Map<String, Class> httpClassMap;

	/**
	 * 存储 WebSocketDemo 映射。
	 */
	private static Map<String, Class> webSocketClassMap;

	private Mapping() {
	}

	public Map<String, Class> getHttpController() {
		return httpClassMap;
	}

	public void setWebSocketController(Map<String, Class> webSocketClassMap) {
		Mapping.webSocketClassMap = webSocketClassMap;
	}

	public Map<String, Class> getWebSocketController() {
		return webSocketClassMap;
	}

	public void setHttpController(Map<String, Class> httpClassMap) {
		Mapping.httpClassMap = httpClassMap;
	}

	public static Mapping get() {
		return MAPPING;
	}
}
