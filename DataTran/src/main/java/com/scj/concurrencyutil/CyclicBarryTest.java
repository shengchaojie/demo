package com.scj.concurrencyutil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class CyclicBarryTest {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        final CyclicBarrier barrier = new CyclicBarrier(100, new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        });

        Thread[] threads = new Thread[100];

        for (int i = 0; i < threads.length; i++) {
            final int count = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("before"+count);
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println("after"+count);
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        System.out.println("主线程会阻塞吗");
    }
}
