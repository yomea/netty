package com.netty.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * AIO非阻塞式
 * @author Administrator
 *
 */
public class TimeServer implements Runnable {
	
	@Override
	public void run() {
		
		try {
			AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open();
			
			assc.bind(new InetSocketAddress("localhost", 8080));
			
			CountDownLatch cdl = new CountDownLatch(1);
			
			//异步接收，这个异步线程好像是守护线程，工作线程不能够退出，否则守护线程也跟着退出了
			assc.accept(assc, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {

				@Override
				public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
					//循环接受请求
					assc.accept(attachment, this);
					System.out.println("爱丽舍宫；懒得搜噶三大公司对方不能结婚的数学波司登；反攻倒算 ");
					
					
					ByteBuffer bb = ByteBuffer.allocate(1024);
					
					bb.put("北京欢迎您，为你开天辟地，流动中梦想充满这朝气".getBytes());
					
					bb.flip();
					
					result.write(bb, bb, new CompletionHandler<Integer, ByteBuffer>() {
                                                //由于存在半包问题，所以需要递归发送
						@Override
						public void completed(Integer l, ByteBuffer bb) {
							if(bb.hasRemaining()) {
								
								result.write(bb, bb, this);
								
							}
						}

						@Override
						public void failed(Throwable exc, ByteBuffer bb) {
							
							try {
								result.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						
						
						
					});
					
				}
				//attachment 附件
				@Override
				public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
					//LockSupport.unpark(thread);
					cdl.countDown();//出现异常时结束进程
				}
			});
			
			//将线程阻塞，不能让程序退出，否则就没用了
			//LockSupport.park();
			cdl.await();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		
		TimeServer ts = new TimeServer();
		
		Thread t = new Thread(ts);
		
		t.start();
		
	}
	

}
