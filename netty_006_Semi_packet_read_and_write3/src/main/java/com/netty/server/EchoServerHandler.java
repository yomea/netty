package com.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {
	
	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//因为在channel中添加了StringDecoder解码器，所以收取到值是String类型的
		String body = (String) msg;
		
		System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
		
		body += "$_";
		
		ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
		//先写到缓存中，需要flush
		ctx.write(resp);
		
		
		
		
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//刷新
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	

}
