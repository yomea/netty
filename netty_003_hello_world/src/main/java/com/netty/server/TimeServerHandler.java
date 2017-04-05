package com.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandler extends ChannelHandlerAdapter {
	
	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ByteBuf类似于JDK中的ByteBuffer,不过netty提供的这个功能更加强大
		ByteBuf buf = (ByteBuf) msg;
		
		byte[] req = new byte[buf.readableBytes()];
		
		buf.readBytes(req);
		
		String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());
		
		System.err.println("半包读写问题！！！");
		
		System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
		
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
		
		currentTime += System.lineSeparator();
		
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
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
