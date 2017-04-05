package com.netty.entity;

import java.io.Serializable;

public class SubscribeResp implements Serializable {
	
	
	private static final long serialVersionUID = 11234907055378374L;

	private int subReqId;
	
	private int respCode;
	
	private String desc;

	public int getSubReqId() {
		return subReqId;
	}

	public void setSubReqId(int subReqId) {
		this.subReqId = subReqId;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "SubscribeResp [subReqId=" + subReqId + ", respCode=" + respCode + ", desc=" + desc + "]";
	}
	
	
	

}
