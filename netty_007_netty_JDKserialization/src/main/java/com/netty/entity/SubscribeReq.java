package com.netty.entity;

import java.io.Serializable;

public class SubscribeReq implements Serializable {

	private static final long serialVersionUID = -4651529584094474783L;
	
	private int subReqId;
	
	private String username;
	
	private String productName;
	
	private String phoneNumber;
	
	private String address;

	public int getSubReqId() {
		return subReqId;
	}

	public void setSubReqId(int subReqId) {
		this.subReqId = subReqId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "SubscribeReq [subReqId=" + subReqId + ", username=" + username + ", productName=" + productName
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}
	
	
	

}
