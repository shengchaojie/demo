package com.scj.jvm;

import java.io.IOException;

public class ShutdownHookTest {

    public static void main(String[] args) {
        System.out.println("De,mo" +
                "");
        ShutdownHook shutdownHook =new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ShutdownHook extends Thread{

        @Override
        public void run() {
            System.out.println("do some clean");
        }
    }
}
