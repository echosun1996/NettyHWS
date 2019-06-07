package org.nettyhws.nettyhws;

import java.net.InetSocketAddress;

import org.nettyhws.nettyhws.constant.Config;
//import ResConfig;

import org.nettyhws.nettyhws.def.ResConfig;
import org.nettyhws.nettyhws.log.SystemLog;
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

/**基于Netty的TCP Socket/HTTP框架
 *
 * @author thenk008,echosun
 */
public class HttpServer {

//	public static void main(String[] args) {
//		HttpServer httpServer=new HttpServer();
//
//		httpServer.connect(1234,"123");
//	}


    /**
     * EventLoopGroup是一组EventLoop的抽象，每个EventLoop维护着一个Selector实例，类似单线程Reactor模式地工作着。
     *
      */
	public void connect(int port,String url){
		SystemLog.WELCOME();
		ResConfig.get().setControl(url);
        // 对应mainReactor：接受新连接线程，主要负责创建新连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 对应subReactor：负责读取数据的线程，主要用于读取数据以及业务逻辑处理
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 服务端启动器
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			// 设置TCP参数
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, true).childHandler(new ChildHandler());

			Channel channel = serverBootstrap.bind(new InetSocketAddress(port)).sync().channel();
			SystemLog.INFO("http server start port open :" + port);
			channel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
class ChildHandler extends ChannelInitializer<SocketChannel>   {

    /**
     * Channel是Netty最核心的接口，一个Channel就是一个联络Socket的通道，通过Channel，可以对Socket进行各种操作。
     * 用Netty编写网络程序的时候，主要是通过ChannelHandler来间接操纵Channel。
     *
     * @param socketChannel SocketChannel
     */
	@Override
	protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(new HttpServerCodec());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(Config.MAX_CONTENT_LENGTH));
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new Http());
	}

}