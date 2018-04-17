package com.scj.nio;

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

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket() .bind(new InetSocketAddress(9999));
        while(true){
            final SocketChannel socketChannel = serverSocketChannel.accept();
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
                while (true){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(200);
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
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
