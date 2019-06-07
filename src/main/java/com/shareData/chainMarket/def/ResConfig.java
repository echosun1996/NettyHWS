package com.shareData.chainMarket.def;

public class ResConfig {
	private static final ResConfig CONFIG = new ResConfig();
	private static String control;

	private ResConfig() {
	};

	public void setControl(String url) {
		control = url;
	}

	public String getControl() {
		return control;
	}

	public static ResConfig get() {
		return CONFIG;
	}
}
