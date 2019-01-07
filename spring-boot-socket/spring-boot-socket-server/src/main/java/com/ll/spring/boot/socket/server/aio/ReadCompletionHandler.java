package com.ll.spring.boot.socket.server.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel channel;

	public ReadCompletionHandler(AsynchronousSocketChannel channel) {
		this.channel = channel;
	}

	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] bs = new byte[attachment.remaining()];
		attachment.get(bs);
		
		String req = null;
		try {
			req = new String(bs, "utf-8");
			System.out.println("receive message is " + req);
			doWrite(System.currentTimeMillis() + "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void doWrite(String response) {
		byte[] bs = response.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		writeBuffer.put(bs);
		writeBuffer.flip();
		this.channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				if (attachment.hasRemaining()) {
					channel.write(attachment, attachment, this);
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
