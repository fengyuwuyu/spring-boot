package com.ll.spring.boot.socket.client.nio;

import java.io.IOException;

public class ClientApp {

	public static void main(String[] args) throws IOException {
		new Thread(new ClientHandler("127.0.0.1", 9999)).start();
	}
}
