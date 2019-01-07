package com.ll.spring.boot.socket.client.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncClientHandler implements Runnable, CompletionHandler<Void, AsyncClientHandler> {

	private AsynchronousSocketChannel client;
	private CountDownLatch countDownLatch;
	private int port;
	private String host;

	public AsyncClientHandler(String host, int port) throws IOException {
		super();
		this.host = host;
		this.port = port;
		this.client = AsynchronousSocketChannel.open();
		this.countDownLatch = new CountDownLatch(1);
	}

	@Override
	public void run() {
		this.client.connect(new InetSocketAddress(host, port), this, this);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void completed(Void result, AsyncClientHandler attachment) {
		byte[] req = "query time".getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(req);
		buffer.flip();
		
		client.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				if (attachment.hasRemaining()) {
					client.write(attachment, attachment, this);
				} else {
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

						@Override
						public void completed(Integer result, ByteBuffer attachment) {
							buffer.flip();
							byte[] bs = new byte[attachment.remaining()];
							attachment.get(bs);
							try {
								String body = new String(bs, "utf-8");
								System.out.println("receive message is " + body);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							countDownLatch.countDown();
						}

						@Override
						public void failed(Throwable exc, ByteBuffer attachment) {
							try {
								client.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							countDownLatch.countDown();
						}
					});
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				countDownLatch.countDown();
			}
		});
	}

	@Override
	public void failed(Throwable exc, AsyncClientHandler attachment) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		countDownLatch.countDown();
	}

}
