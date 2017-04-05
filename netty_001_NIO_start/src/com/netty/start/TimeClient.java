package com.netty.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClient implements Runnable {
	
	private volatile boolean stop = false;
	

	@Override
	public void run() {
		
		Selector selector = null;
		
		try {
			SocketChannel sc = SocketChannel.open();
			
			selector = Selector.open();
			
			sc.configureBlocking(false);
			
			boolean flag = sc.connect(new InetSocketAddress("localhost", 8080));
			
			if(flag) {
				
				sc.register(selector, SelectionKey.OP_READ);
				
				
				
			} else {
				
				
				sc.register(selector, SelectionKey.OP_CONNECT);
			}
			
			
			while(!stop) {
				
				int len = selector.select();
				
				if(len == 0) continue;
				
				Set<SelectionKey> keys = selector.selectedKeys();
 				
				
				Iterator<SelectionKey> it = keys.iterator();
				
				while(it.hasNext()) {
					
					SelectionKey key = it.next();
					
					if(key.isValid()) {
						
						if(key.isConnectable()) {
							
							SocketChannel socketChannel = (SocketChannel) key.channel();
							
							if(socketChannel.finishConnect()) {
								socketChannel.configureBlocking(false);
								
								socketChannel.register(selector, SelectionKey.OP_READ);
								
								ByteBuffer bb = ByteBuffer.allocate(1024);
								
								bb.put("hello world".getBytes());
								
								bb.flip();
								
								socketChannel.write(bb);
								
							}
							
						}
						
						if(key.isReadable()) {
							SocketChannel socketChannel = (SocketChannel) key.channel();
							
							ByteBuffer bb = ByteBuffer.allocate(1024);
							
							int ll = socketChannel.read(bb);
							
							if(ll > 0) {
								
								bb.flip();
								
								System.out.println("服务器给我的回应");
								System.out.println(new String(bb.array(), 0, bb.remaining()));
								
							} else if(ll < 0) {
								
								bb.clear();
								
								key.cancel();
								
								socketChannel.close();
								
							}
							
							
							
							
						}
						
					}
					
					it.remove();
					
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			try {
				selector.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	
		
		
		
	}
	
	public static void main(String[] args) {
		
		TimeClient tc = new TimeClient();
		
		Thread t = new Thread(tc);
		
		t.start();
		
	}
	
	
	

}
