package com.scj.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-04-17 18:19
 */
public class BlockingServer {

    public static final byte[] ENTER = new byte[]{13,10};

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket() .bind(new InetSocketAddress(9999));
        while(true){
            final SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);
            socketChannel.write(ByteBuffer.wrap("\r\n".getBytes()));
            new Thread(new SocketThread(socketChannel)).start();
        }
    }

    static class SocketThread implements Runnable {

        SocketChannel socketChannel;

        public SocketThread(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(200);
                byteBuffer.put((byte)1);
                byteBuffer.get();
                StringBuilder sb = new StringBuilder();
                while (true){
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    if(byteBuffer.limit()>1&&byteBuffer.get()==ENTER[0]&&byteBuffer.get()==ENTER[1]){
                        socketChannel.write(ByteBuffer.wrap(sb.toString().getBytes()));
                        socketChannel.write(ByteBuffer.wrap("\r\n".getBytes()));
                        byteBuffer.clear();
                        sb = new StringBuilder();
                        continue;
                    }
                    while (byteBuffer.hasRemaining()){
                        sb.append((char)byteBuffer.get());
                    }
                    //必须进行clear,不然阻塞不住
                    //byteBuffer.clear();
                }
            }catch (Exception ex){

            }finally {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
