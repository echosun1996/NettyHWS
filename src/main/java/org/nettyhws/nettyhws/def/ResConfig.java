package org.nettyhws.nettyhws.def;

import java.util.Map;

/**
 * 保存控制器路径
 *
 * @author echosun
 */
public class ResConfig {
	private static final ResConfig CONFIG = new ResConfig();
	private static Map<String, Class> urlToClassMap;

	private ResConfig() {
	}

	public Map<String, Class> getController() {
		return urlToClassMap;
	}

	public void setController(Map<String, Class> urlToClassMap) {
		ResConfig.urlToClassMap = urlToClassMap;
	}

//	public void setController(String url) {
//		control = url;
//	}

//	public String getController() {
//		return control;
//	}

	public static ResConfig get() {
		return CONFIG;
	}
}
