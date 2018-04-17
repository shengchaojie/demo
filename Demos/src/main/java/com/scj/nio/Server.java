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
                    SocketChannel socketChannel = ((ServerSocketChannel)selectionKey.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.write(ByteBuffer.wrap("Welcome to scj telnet server\r\n".getBytes()));
                    socketChannel.register(selectionKey.selector(),SelectionKey.OP_READ,new StringBuilder());
                }

                if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    StringBuilder sb = (StringBuilder) selectionKey.attachment();
                    ByteBuffer byteBuffer =ByteBuffer.allocate(20);
                    long size = socketChannel.read(byteBuffer);
                    if(size == -1){
                        socketChannel.close();
                    }else if(size ==2){
                        byteBuffer.flip();
                        if(byteBuffer.get(0)==ENTER[0]&&byteBuffer.get(1)==ENTER[1]){
                            selectionKey.interestOps(SelectionKey.OP_WRITE );
                        }
                    }else if(size ==0) {
                        selectionKey.interestOps(SelectionKey.OP_WRITE );
                    }else{
                            byteBuffer.flip();
                            if(byteBuffer.hasRemaining()){
                                sb.append((char)(byteBuffer.get()));
                            }
                            System.out.println(sb.toString());
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                    }

                if(selectionKey.isWritable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    StringBuilder sb = (StringBuilder) selectionKey.attachment();
                    if("exit".equals(sb.toString())){
                        socketChannel.close();
                        continue;
                    }else if("hello".equals(sb.toString())){
                        socketChannel.write(ByteBuffer.wrap("Hello,my boy!\r\n".getBytes()));
                    } else{
                        socketChannel.write(ByteBuffer.wrap(sb.toString().getBytes()));
                        socketChannel.write(ByteBuffer.wrap(new byte[]{13,10}));
                    }
                    selectionKey.attach(new StringBuilder());
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }



    }

}
