package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {

	public void connect(int port, String host) throws Exception {

		// 配合客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			//客户端启动类
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {

						@Override
						protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new FixedLengthFrameDecoder(12));//设置固定长度的解码器
							ch.pipeline().addLast(new StringDecoder());//加入字符串解码器
							ch.pipeline().addLast(new EchoClientHandler());
						}
					});
			
			//发起异步连接操作sync()表示在链接成功之前一直等待
			ChannelFuture f = b.connect(host, port).sync();
			
			//等待客户端链路关闭，sync()在关闭之前一直阻塞
			f.channel().closeFuture().sync();
			
			
			
		} catch (Exception e) {

		} finally {
			//优雅的关闭，释放资源
			group.shutdownGracefully();
			
		}

	}
	
	public static void main(String[] args) throws Exception {
		
		int port = 8080;
		
		if(args != null &&  args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
				
			} catch(NumberFormatException e) {
				
				//采用默认值
			}
		}
		
		new EchoClient().connect(port, "localhost");
		
		
		
		
		
		
	}
	
	
}