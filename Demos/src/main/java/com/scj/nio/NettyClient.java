package com.scj.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by shengchaojie on 2018/4/24.
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0 ;i<400;i++){//会产生拆包
                            sb.append("hello");
                        }
                        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(sb.toString().getBytes()));
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf byteBuf = (ByteBuf)msg;
                        System.out.println(new String(ByteBufUtil.getBytes(byteBuf)));
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("...");
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("localhost",9999).sync();
        channelFuture.channel().closeFuture().sync();


    }

}
