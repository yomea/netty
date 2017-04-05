package com.netty.server;

import java.util.logging.Logger;

import com.netty.entity.SubscribeReqProto;
import com.netty.entity.SubscribeRespProto;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqServerHandler extends ChannelHandlerAdapter {
	
	private Logger logger = Logger.getLogger(SubReqServerHandler.class.getName());
	
	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		SubscribeReqProto.SubscribeReq subReq = (SubscribeReqProto.SubscribeReq) msg;
		System.out.print(++counter + "-->");
		System.out.println(subReq);
		
		SubscribeRespProto.SubscribeResp subResp = this.createSubscribeResp(subReq.getSubReqID());
		
		
		//先写到缓存中，需要flush
		ctx.write(subResp);
		
		
		
		
	}
	
	public SubscribeRespProto.SubscribeResp createSubscribeResp(int subRespId) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubReqID(subRespId);
		
		builder.setRespCode(0);
		
		builder.setDesc("正在派送。。。");
		
		return builder.build();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//刷新
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warning(cause.getMessage());
		ctx.close();
	}
	

}
