package com.ac.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel Active..Writing hello to server");
		
		ctx.channel().writeAndFlush(Unpooled.copiedBuffer("Hello!!!",
				CharsetUtil.UTF_8));
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("Message received back : " + msg.toString(CharsetUtil.UTF_8));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("Exception occured");
		cause.printStackTrace();
		ctx.close();
	}
}
