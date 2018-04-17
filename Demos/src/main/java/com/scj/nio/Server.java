package com.scj.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 使用java nio api 实现echo服务器
 */
public class Server {

    public static final byte[] ENTER = new byte[]{13,10};

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket() .bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if(selector.select(2000)==0){
                System.out.println(".");
                continue;
            }

            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();


                if(selectionKey.isAcceptable()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(200);
                    SocketChannel socketChannel = ((ServerSocketChannel)selectionKey.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.write(ByteBuffer.wrap("Welcome to scj telnet server\r\n".getBytes()));
                    socketChannel.register(selectionKey.selector(),SelectionKey.OP_READ,byteBuffer);
                    continue;
                }

                if(selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(byteBuffer);
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }

                if(selectionKey.isWritable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                    byteBuffer.compact();
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }



    }

}
