package com.scj.nio.readbuffer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-08-20 15:22
 */
public class CustomEchoServer {


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(eventLoopGroup,childEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(9999))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new BufferedChannelHandler());
                        /*ch.pipeline().addLast(new EchoDecoder())
                                .addLast(new EchoOutputChannelHandler());*/
                        ch.pipeline()
                                .addLast(new TestHandlerAdded())
                                .addLast(new DelimiterBasedFrameDecoder(1024,false,Unpooled.wrappedBuffer(new byte[]{'\r','\n'}),Unpooled.wrappedBuffer(new byte[]{'\r'})))
                                .addLast(new EchoOutputChannelHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind().sync();
        channelFuture.channel().closeFuture().sync();
    }

    public static class BufferedChannelHandler extends ChannelDuplexHandler{

        private StringBuilder buffer = new StringBuilder();

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("hello\r\n".getBytes()));
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf =(ByteBuf) msg;
            if(byteBuf.writerIndex()>1&&byteBuf.getByte(byteBuf.writerIndex()-2)=='\r'||byteBuf.getByte(byteBuf.writerIndex()-1)=='\n'){
                buffer.append("\r\n");
                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(buffer.toString().getBytes()));
                buffer = new StringBuilder();
            }else{
                String input =new String(ByteBufUtil.getBytes(byteBuf));
                buffer.append(input);
                System.out.println(input);
            }
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);
        }
    }

    public static class EchoDecoder extends ByteToMessageDecoder{


        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("hello\r\n".getBytes()));
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            ByteBuf byteBuf =(ByteBuf) in;
            if(byteBuf.writerIndex()>1&&byteBuf.getByte(byteBuf.writerIndex()-2)=='\r'||byteBuf.getByte(byteBuf.writerIndex()-1)=='\n'){
                out.add(in.readBytes(byteBuf.readableBytes()));

            }
        }
    }

    public static class EchoOutputChannelHandler extends ChannelDuplexHandler{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("hello\r\n".getBytes()));
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf =(ByteBuf) msg;
            ctx.writeAndFlush(byteBuf);
        }
    }

    public static class TestHandlerAdded extends ChannelInboundHandlerAdapter{
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("add");
        }


    }


}
