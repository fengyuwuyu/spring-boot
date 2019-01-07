package com.ll.spring.boot.socket.server.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

	@Override
	public void completed(AsynchronousSocketChannel result, AioServer attachment) {
		attachment.server.accept(attachment, this);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		result.read(buffer, buffer, new ReadCompletionHandler(result));
	}

	@Override
	public void failed(Throwable exc, AioServer attachment) {
		
	}

}
