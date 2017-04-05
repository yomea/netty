package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SubReqClient {

	public void connect(int port, String host) throws Exception {

		// 配合客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			// 客户端启动类
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {

						@Override
						protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
							//netty的java对象序列化支持TCP的粘包和拆包
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
									ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));//禁止缓存加载器，因为随着系统的升级，可能对应的加载器也跟着升级了
							ch.pipeline().addLast(new SubReqClientHandler());
						}
					});

			// 发起异步连接操作sync()表示在链接成功之前一直等待
			ChannelFuture f = b.connect(host, port).sync();

			// 等待客户端链路关闭，sync()在关闭之前一直阻塞
			f.channel().closeFuture().sync();

		} catch (Exception e) {

		} finally {
			// 优雅的关闭，释放资源
			group.shutdownGracefully();

		}

	}

	public static void main(String[] args) throws Exception {

		int port = 8080;

		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);

			} catch (NumberFormatException e) {

				// 采用默认值
			}
		}

		new SubReqClient().connect(port, "localhost");

	}

}