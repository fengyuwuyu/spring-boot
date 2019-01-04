package com.ll.spring.boot.socket.server.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class IoServer {

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(9999);
		Socket client = null;
		try {
			while (true) {
				BufferedReader reader = null;
				PrintWriter writer = null;
				
				try {

					client = server.accept();
					reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
					writer = new PrintWriter(client.getOutputStream());
					
					String line = reader.readLine();
					writer.write(String.format("server response : %s",  line));
				} finally {
					reader.close();
					writer.close();
					client.close();
				}
			}
		} finally {
			if (server != null) {
				server.close();
			}
		}
	}
}
