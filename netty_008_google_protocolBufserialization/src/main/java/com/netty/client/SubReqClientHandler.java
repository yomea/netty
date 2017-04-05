package com.netty.client;

import java.util.logging.Logger;

import com.netty.entity.SubscribeReqProto;
import com.netty.entity.SubscribeRespProto;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqClientHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(SubReqClientHandler.class.getName());
	
	private int counter;
	
	private SubscribeReqProto.SubscribeReq subReq;
	
	public SubReqClientHandler() {
		
		
		
	}
	
	public SubscribeReqProto.SubscribeReq createSubscribeReq(int subID) {
		
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		
		builder.setSubReqID(subID);
		
		builder.setUsername("root");
		
		builder.setProductName("heheda");
		
		builder.setPhoneNumber("123424");
		
		builder.setAddress("happiness home");
		
		
		return builder.build();
		
	}
	
	//通道准备就绪，就直接发送信息到服务器
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		for(int i = 0; i < 10; i++) {
			subReq = createSubscribeReq(i);
			ctx.writeAndFlush(subReq);
			
		}
		
		
		
		
	}
	
	/**
	 * 当服务器发送信息回来时会调用这个方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//因为在channel中添加了StringDecoder解码器，所以收取到值是String类型的
		SubscribeRespProto.SubscribeResp subResp = (SubscribeRespProto.SubscribeResp) msg;
		System.out.print(++counter + "-->");
		System.out.println(subResp);
		
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		logger.warning("Unexpected exception from dodwnstream : " + cause.getMessage());
		//释放资源
		ctx.close();
	}
	
}
