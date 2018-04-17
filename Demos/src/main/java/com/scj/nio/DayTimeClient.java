package com.scj.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-04-13 16:56
 */
public class DayTimeClient {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("time.nist.gov",13);
            socket.setSoTimeout(15000);
            InputStream is = socket.getInputStream();
            StringBuilder sb  = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(is,"ASCII");
            for(int c = isr.read();c!=-1;c=isr.read()){
                sb.append((char)c);
            }
            System.out.println(sb.toString());
        }catch (Exception ex){
            System.out.println(ex);
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
