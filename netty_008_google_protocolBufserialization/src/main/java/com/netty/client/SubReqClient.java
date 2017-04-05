package com.netty.client;

import com.netty.entity.SubscribeRespProto;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

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
							ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());//用来处理protocolbuf的读写半包问题
							//加入解码器，并制定解码后对应的类型
							ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
							
							ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							//加入编码器，对写入到通道的对象进行编码
							ch.pipeline().addLast(new ProtobufEncoder());
							
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