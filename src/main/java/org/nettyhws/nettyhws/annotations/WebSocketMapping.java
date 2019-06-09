package org.nettyhws.nettyhws.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WebSocketDemo 注解。
 * value 表示路径。访问路径为：ws://localhost:[PORT]/[value] 。
 *
 * @author echosun
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketMapping {
    String value();
}
