package com.ll.spring.boot.socket.server.aio;

import java.io.IOException;

public class AioServerApp {

	public static void main(String[] args) throws IOException {
		new Thread(new AioServer(9999)).start();
	}
}
