package com.shareData.chainMarket.constant;

public class Config {
	public static final int SERVER_PORT = 8080;// 微服务链接端口 要抽出
	public static final byte[] POINTER = "$_".getBytes();// 分隔符
	public static final String POINTER_STRING = "$_";// 分隔符
	public static final int MESSAGE_MAX = 2048;// 信息包最大长度  
}
