package com.ll.spring.boot.socket.server.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AioServer implements Runnable {
	
	AsynchronousServerSocketChannel server;
	private CountDownLatch countDownLatch;
	

	public AioServer(int port) throws IOException {
		server = AsynchronousServerSocketChannel.open();
		server.bind(new InetSocketAddress(port));
		countDownLatch = new CountDownLatch(1);
		System.out.println("server is start success");
	}

	@Override
	public void run() {
		server.accept(this, new AcceptCompletionHandler());
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
