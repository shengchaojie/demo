package com.scj.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-04-25 9:54
 */
public class NettyDecodeServer {


    public static void main(String[] args) {
        NioEventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap
                    .group(parentEventLoopGroup, eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new DelimiterBasedFrameChannelInitializer());
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex) {

        }finally {
            parentEventLoopGroup.shutdownGracefully();
            eventLoopGroup.shutdownGracefully();
        }
    }

    static class DelimiterBasedFrameChannelInitializer extends  ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ByteBuf delimiter = Unpooled.wrappedBuffer("*__".getBytes());
            ch.pipeline()
                    .addLast(new DelimiterBasedFrameDecoder(2000,delimiter))
                    .addLast(new StringDecoder())
                    .addLast(new TestEchoChannelHandler());
        }
    }

    static class LengthFieldBasedChannelInitializer extends  ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline()
                    .addLast(new LengthFieldBasedFrameDecoder(3000,0,4,0,4))
                    .addLast(new StringDecoder())
                    .addLast(new TestEchoChannelHandler());
        }
    }

    static class TestEchoChannelHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String  message = (String) msg;
            System.out.println(message.length());
            System.out.println(message);
        }
    }
}
