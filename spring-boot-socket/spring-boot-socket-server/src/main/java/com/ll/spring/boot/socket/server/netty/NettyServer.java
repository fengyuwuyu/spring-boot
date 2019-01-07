package com.ll.spring.boot.socket.server.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

	private int port;

	public NettyServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {
		new NettyServer(9999).start();
	}

	public void start() throws InterruptedException {
		final TimeServerHandler handler = new TimeServerHandler();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group)
				.channel(NioServerSocketChannel.class) //
				.localAddress(port) //
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(handler);
					}
				});
			ChannelFuture future = boot.bind().sync();
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
