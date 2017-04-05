package com.netty.server;

import com.netty.entity.SubscribeReq;
import com.netty.entity.SubscribeResp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqServerHandler extends ChannelHandlerAdapter {
	
	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//因为在channel中添加了StringDecoder解码器，所以收取到值是String类型的
		SubscribeReq subReq = (SubscribeReq) msg;
		System.out.print(++counter + "-->");
		System.out.println(subReq);
		
		SubscribeResp subResp = new SubscribeResp();
		
		subResp.setSubReqId(subReq.getSubReqId());
		subResp.setRespCode(1);
		subResp.setDesc("好好的过");
		
		
		//先写到缓存中，需要flush
		ctx.write(subResp);
		
		
		
		
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
