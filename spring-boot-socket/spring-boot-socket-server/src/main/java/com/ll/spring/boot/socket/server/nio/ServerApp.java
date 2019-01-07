package com.ll.spring.boot.socket.server.nio;

import java.io.IOException;

public class ServerApp {

	public static void main(String[] args) throws IOException {
		new Thread(new ServerHandler(9999)).start();
	}
}
