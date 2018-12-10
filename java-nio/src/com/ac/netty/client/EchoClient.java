package com.ac.netty.client;

import com.ac.netty.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 8989;

	public static void main(String[] args) {
		
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup)
			.channel(NioSocketChannel.class)
			.remoteAddress( new InetSocketAddress(HOST, PORT))
			.handler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());
				};
			});
		
		ChannelFuture future;
		try {

			future = bootstrap.connect().sync();
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}