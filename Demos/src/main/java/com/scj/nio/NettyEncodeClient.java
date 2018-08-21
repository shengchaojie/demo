package com.scj.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-04-25 11:10
 */
public class NettyEncodeClient {

    public static void main(String[] args) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.channel(NioSocketChannel.class)
                    .group(nioEventLoopGroup)
                    .handler(new LengthFieldBasedChannelHandler());
            ChannelFuture channelFuture = bootstrap.connect("localhost",9999).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){

        }finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }


    static class DelimiterBasedFrameChannelHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
            ByteBuf byteBuf = allocator.buffer(20);
            byteBuf.writeBytes("12345678".getBytes());
            byteBuf.writeBytes("*__".getBytes());
            byteBuf.writeBytes("12345678".getBytes());
            byteBuf.writeBytes("*__".getBytes());
            for(int i =0 ;i<500;i++){
                byteBuf.writeBytes("1234".getBytes());
            }
            byteBuf.writeBytes("*__".getBytes());
            ctx.channel().writeAndFlush(byteBuf);
            ctx.channel().close();
        }
    }

    static class LengthFieldBasedChannelHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
            ByteBuf byteBuf = allocator.buffer(20);
            byteBuf.writeInt(8);
            byteBuf.writeBytes("12345678".getBytes());
            byteBuf.writeInt(8);
            byteBuf.writeBytes("12345678".getBytes());
            byteBuf.writeInt(2000);
            for(int i =0 ;i<500;i++){
                byteBuf.writeBytes("1234".getBytes());
            }
            ctx.channel().writeAndFlush(byteBuf);
            ctx.channel().close();
        }
    }
}
