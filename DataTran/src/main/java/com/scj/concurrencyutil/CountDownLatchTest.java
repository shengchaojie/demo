package com.scj.concurrencyutil;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch =new CountDownLatch(1);

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(i);
                }
                latch.countDown();
            }
        });
        thread.start();
        latch.await();
        System.out.println(111);
    }
}
