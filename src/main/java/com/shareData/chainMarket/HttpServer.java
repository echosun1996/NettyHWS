package com.shareData.chainMarket;

import java.net.InetSocketAddress;

import com.shareData.chainMarket.constant.Config;
import com.shareData.chainMarket.def.ResConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServer {
	public void connect(int port,String url) {
		ResConfig.get().setControl(url);
		EventLoopGroup group = new NioEventLoopGroup();
		EventLoopGroup boss = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group, boss).channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, true)
					.childHandler(new Em());
			Channel nel = b.bind(new InetSocketAddress(port)).sync().channel();
			System.out.println("http server start port open :" + port);
			nel.closeFuture().sync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}
}

class Em extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new HttpServerCodec());
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		ch.pipeline().addLast(new ChunkedWriteHandler());
		ch.pipeline().addLast(new Http());
	}

}