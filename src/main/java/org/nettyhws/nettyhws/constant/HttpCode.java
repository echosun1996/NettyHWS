package org.nettyhws.nettyhws.constant;

/**
 * 规定HTTP返回值。
 * 用于在重载的 response 方法中，规定使用何种逻辑响应。
 *
 * @author thenk008,echosun
 */
public class HttpCode {
public static final byte NOT_FOUND=1;

public static final byte SERVER_ERROR =2;

public static final byte OK =0;

public static final byte WEB_SOCKET =3;
}
