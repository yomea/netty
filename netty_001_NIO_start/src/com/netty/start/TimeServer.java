package com.netty.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 原先使用BIO阻塞式io的时候，需要对每个连接启动一个client保存它的socket对象，如今有了NIO，可以使用Selector来解决
 * @author Administrator
 *
 */
public class TimeServer implements Runnable {
	
 private volatile boolean stop = false;
	
	@Override
	public void run() {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			
			ssc.configureBlocking(false);
			
			ssc.bind(new InetSocketAddress("localhost", 8080));
			
			
			Selector selector = Selector.open();
			
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while(!stop) {
				int len = selector.select();
				
				if(len == 0) continue;
				
				Set<SelectionKey>keys = selector.selectedKeys();
				
				Iterator<SelectionKey> it = keys.iterator();
				
				while(it.hasNext()) {
					
					SelectionKey key = it.next();
					
					if(key.isValid()) {
						
						if(key.isAcceptable()) {
							
							ServerSocketChannel srs = (ServerSocketChannel) key.channel();
							
							 SocketChannel sc = srs.accept();
							 
							 if(sc != null) {
								 sc.configureBlocking(false);
								 
								 sc.register(selector, SelectionKey.OP_READ);
								 
								 
							 }
						}
						
						
					
					
					
					
					
					if(key.isReadable()) {
						
						SocketChannel sc = (SocketChannel) key.channel();
						
						ByteBuffer bb = ByteBuffer.allocate(1024);
						
						int readLen = sc.read(bb);
						
						if(readLen > 0) {
							
							bb.flip();
							
							if(bb.hasRemaining()) {
								System.out.println("转发回给客户端");
								sc.write(bb);
								
								
								
								
							} else {
								
								bb.clear();
								
							}
							
							
							
							
						} else if(readLen < 0) {
							
							key.cancel();
							
							key.channel().close();
							
							
						} 
						
						
						
						
						
					}
					
					it.remove();
					
					}
				}
				
				
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		TimeServer ts = new TimeServer();
		
		Thread t = new Thread(ts);
		
		t.start();
		
	}
	

}
