package org.nettyhws.nettyhws.constant;

/**
 * 静态方法，保存框架静态常量。
 *
 * @author thenk008,echosun
 */
public class Config {
//    //分隔符
//	public static final byte[] POINTER = "$_".getBytes();
//    // 分隔符
//	public static final String POINTER_STRING = "$_";
    // 信息包最大长度
	public static final int MESSAGE_MAX = 2048;
	public static final int MAX_CONTENT_LENGTH = 65536;
	public static final String[] ANNOTATION_NAME={"@org.nettyhws.nettyhws.annotations.HttpMapping",
								"@org.nettyhws.nettyhws.annotations.WebSocketMapping"};
	public static final String HTTP_MAPPING=ANNOTATION_NAME[0];
	public static final String WEBSOCKET_MAPPING=ANNOTATION_NAME[1];

	// 调试开关，打开后将打印运行详情
	public static final boolean DEGUB = true;

}
