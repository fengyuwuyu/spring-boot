package com.ll.spring.boot.socket.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	private final String host;
	private final int port;
	
	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {
		new EchoClient("127.0.0.1", 9999).start();
	}

	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		EchoClientHandler handler = new EchoClientHandler();
		
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group) //
				.channel(NioSocketChannel.class) //
				.remoteAddress(host, port) //
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(handler);
					}

				});
			ChannelFuture future = boot.connect().sync();
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
