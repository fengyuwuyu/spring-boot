package com.ll.spring.boot.socket.server.nio;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Demo {

	public static void main(String[] args) throws UnknownHostException {
		InetAddress local = InetAddress.getByName("127.0.0.1");
		System.out.println(local.getHostAddress());
		System.out.println(local.getHostName());
	}
}
