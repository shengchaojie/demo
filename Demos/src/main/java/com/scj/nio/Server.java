package com.scj.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {

    public static final byte[] ENTER = new byte[]{13,10};

    public static void main(String[] args) throws IOException {
        ByteBuffer writeBuffer = ByteBuffer.allocate(2000);
        ByteBuffer readBuffer = ByteBuffer.allocate(200);
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
                    socketChannel.register(selectionKey.selector(),SelectionKey.OP_READ);
                }

                if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    long size = socketChannel.read(readBuffer);
                    System.out.println(new String(readBuffer.array()));
                    if(size == -1){
                        socketChannel.close();
                    }else {
                        selectionKey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    }
                }

                if(selectionKey.isWritable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    //对回车进行处理
                    readBuffer.flip();
                    socketChannel.write(readBuffer);
                    readBuffer.clear();
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }



    }

}
