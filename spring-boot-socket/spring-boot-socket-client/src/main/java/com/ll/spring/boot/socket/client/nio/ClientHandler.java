package com.ll.spring.boot.socket.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class ClientHandler implements Runnable {
	
	private final static int BUFFER_SIZE = 1024 * 100;
	private SocketChannel client;
	private Selector selector;
	private boolean stop = false;
	private String host;
	private int port;
	

	public ClientHandler(String host, int port) throws IOException {
		this.host = StringUtils.isNoneEmpty(host) ? "127.0.0.1" : host;
		this.port = port;
		
		this.selector = Selector.open();
		this.client = SocketChannel.open();
		this.client.configureBlocking(false);
		this.client.socket().setReuseAddress(true);
		this.client.socket().setReceiveBufferSize(BUFFER_SIZE);
		this.client.socket().setSendBufferSize(BUFFER_SIZE);
	}



	@Override
	public void run() {
		try {
			doConnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		while (!this.stop) {
			try {
				selector.select(1000L);
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (IOException e) {
						e.printStackTrace();
						if (key != null) {
							key.cancel();
							if (key.channel() != null) {
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			if (this.selector != null) {
				this.selector.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				} else {
					System.exit(-1);
				}
			} else if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bs = new byte[readBuffer.remaining()];
					readBuffer.get(bs);
					String body = new String(bs, "utf-8");
					System.out.println("receiver message is " + body);
					this.stop = true;
				} else if (readBytes < 0) {
					key.cancel();
					sc.close();
				}
			}
		}
	}



	private void doConnect() throws IOException {
		boolean connected = this.client.connect(new InetSocketAddress(this.host, this.port));
		if (connected) {
			this.client.register(selector, SelectionKey.OP_READ);
			doWrite(this.client);
		} else {
			this.client.register(selector, SelectionKey.OP_CONNECT);
		}
	}

	private void doWrite(SocketChannel channel) throws IOException {
		byte[] req = "hello world".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		channel.write(writeBuffer);
		if (!writeBuffer.hasRemaining()) {
			System.out.println("send message success!");
		}
	}



	public void setStop(boolean stop) {
		this.stop = stop;
	}
}
