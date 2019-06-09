package org.nettyhws.test;

import org.nettyhws.nettyhws.NettyHWSApplication;

/**
 * @author echosun
 */
public class TestMain {
    public static void main(String[] args) {
        // 启动服务
        new NettyHWSApplication(13142,"org.nettyhws.test.demo",true);
    }
}
