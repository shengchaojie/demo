package com.scj.nio;

import java.io.IOException;
import java.net.Socket;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-04-13 17:44
 */
public class InteAddressTest {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("www.baidu.com",80);
        System.out.println(socket.getLocalAddress());
        System.out.println(socket.getLocalPort());
    }

}
