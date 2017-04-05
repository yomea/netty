package com.netty.server;

import com.netty.entity.SubscribeReqProto;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqServer {
	
	
	public void bind(int port) {
		//NIO线程组，Reactor线程组，一个用于接受客户端的请求，另一个用于进行SocketChannel的操作
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			//用于NIO服务端启动的辅助类
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(bossGroup, workerGroup)
			  .channel(NioServerSocketChannel.class)
			  .option(ChannelOption.SO_BACKLOG, 100)
			  .handler(new LoggingHandler(LogLevel.INFO))
			  .childHandler(new ChildChannelHandler());
			
			//绑定端口，同步等待成功，一直等待到绑定端口成功，返回一个ChannelFuture,类似JDK中的java.util.concurrent.Future
			//用于异步的通知会调
			ChannelFuture f = b.bind(port).sync();
			
			//进行阻塞，等待服务器链路关闭，就退出
			f.channel().closeFuture().sync();
		
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			
			//优雅退出，释放线程资源
			
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			
		}
		
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			arg0.pipeline().addLast(new ProtobufVarint32FrameDecoder());//用来处理protocolbuf的读写半包问题
			//加入解码器，并制定解码后对应的类型
			arg0.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
			arg0.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
			//加入编码器，对写入到通道的对象进行编码
			arg0.pipeline().addLast(new ProtobufEncoder());
			arg0.pipeline().addLast(new SubReqServerHandler());
			
		}
		
		
	}
	
	
	public static void main(String[] args) {
		
		int port = 8080;
		
		if(args != null && args.length > 0) {
			try {
				
				port = Integer.valueOf(args[0]);
				
			} catch (NumberFormatException e) {
				
				
			}
			
		}
		
		new SubReqServer().bind(port);
		
	}
	

}
