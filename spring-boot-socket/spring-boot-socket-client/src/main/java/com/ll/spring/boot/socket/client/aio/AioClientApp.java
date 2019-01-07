package com.ll.spring.boot.socket.client.aio;

import java.io.IOException;

public class AioClientApp {

	public static void main(String[] args) throws IOException {
		new Thread(new AsyncClientHandler("127.0.0.1", 9999)).start();
	}
}
