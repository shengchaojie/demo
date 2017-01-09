package com.scj.concurrencyutil;

import java.util.Date;

/**
 * Created by shengcj on 2016/8/19.
 */
public class ThreadLocalTest {
    public static final InheritableThreadLocal<String> itl = new InheritableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return new Date().toString();
        }

        @Override
        protected String childValue(String parentValue) {
            return parentValue + " hello";
        }
    };

    public static void main(String[] args) throws InterruptedException {
        //System.out.println(Thread.currentThread().getName() + " : " + itl.get());

       // Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " : " + itl.get());
            }
        }).start();

        Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " : " + itl.get());
            }
        }).start();
    }
}
