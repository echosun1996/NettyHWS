package org.nettyhws.nettyhws.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WebSocket 注解
 * value 表示路径。访问路径为：ws://localhost:[PORT]/[value]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)//到运行时有效
public @interface WebSocketMapping {
    String value();
}
