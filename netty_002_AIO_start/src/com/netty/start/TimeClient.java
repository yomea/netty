package com.netty.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class TimeClient implements Runnable {
	
	@Override
	public void run() {
		
		try {
			AsynchronousSocketChannel asc = AsynchronousSocketChannel.open();
			
			asc.connect(new InetSocketAddress("localhost", 8080));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		TimeClient tc = new TimeClient();
		
		Thread t = new Thread(tc);
		
		t.start();
		
	}
	
	
	

}
