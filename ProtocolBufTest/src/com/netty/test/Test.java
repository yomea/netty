package com.netty.test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.netty.entity.SubscribeReqProto;

public class Test {
	
	public static byte[] encoder(SubscribeReqProto.SubscribeReq subReq) {
		
		
		return subReq.toByteArray();
	}
	
	public static SubscribeReqProto.SubscribeReq decoder(byte[] data) throws InvalidProtocolBufferException {
		
		return SubscribeReqProto.SubscribeReq.parseFrom(data);
	}
	
	public static SubscribeReqProto.SubscribeReq create() {
		
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		
		builder.setSubReqID(1);
		
		builder.setUsername("root");
		
		builder.setProductName("wifi rute");
		
		builder.setPhoneNumber("100");
		
		builder.setAddress("地狱第十八层");
		
		return builder.build();
	}
	
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		
		SubscribeReqProto.SubscribeReq subReq = create();
		
		byte[] data = Test.encoder(subReq);
		
		SubscribeReqProto.SubscribeReq s = Test.decoder(data);
		
		System.out.println(s);
		
		
		
	}
	
	
	
	

}
