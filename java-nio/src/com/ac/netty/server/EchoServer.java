package com.ac.netty.server;

import com.ac.netty.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
	public static final int PORT = 8989;

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
		EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
		
		ServerBootstrap server = new ServerBootstrap();
		server.group(parentEventLoopGroup, childEventLoopGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 10)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new EchoServerHandler());
				}
			})
			.localAddress(new InetSocketAddress(PORT));
		
		try {
			ChannelFuture channelFuture = server.bind().sync();
			
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			childEventLoopGroup.shutdownGracefully().sync();
		}	
	}
}
