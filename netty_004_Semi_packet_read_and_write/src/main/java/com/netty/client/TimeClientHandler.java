package com.netty.client;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	
	private byte[] req = null;
	
	private int counter;
	
	public TimeClientHandler() {
		
		req = ("QUERY TIME ORDER" + System.lineSeparator()).getBytes();
		
	}
	
	//通道准备就绪，就直接发送信息到服务器
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		
		//向服务端发送一百条消息，由于存在tcp的粘包，拆包的问题，服务段可能会显示收取到的信息没有一百条
		for(int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
		
		
		
	}
	
	/**
	 * 当服务器发送信息回来时会调用这个方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//因为在channel中添加了StringDecoder解码器，所以收取到值是String类型的
		String body = (String) msg;
		System.out.println("Now is : " + body + " ; the counter is : " + ++counter);
		
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//释放资源
		logger.warning("Unexpected exception from dodwnstream : " + cause.getMessage());
		
		ctx.close();
	}
	
}
