package com.scj.concurrencyutil;

/**
 * Created by shengcj on 2017/1/9.
 */
public class WaitTest {

    static class ThreadA extends Thread {
        public ThreadA(String name){
            super(name);
        }

        @Override
        public void run() {
            synchronized (this){
                System.out.println(Thread.currentThread().getName()+" call notify()");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadA t1 =new ThreadA("t1");

        synchronized (t1){
            System.out.println(Thread.currentThread().getName()+" start t1");
            t1.start();

            System.out.println(Thread.currentThread().getName()+" wait");
            t1.wait();

            System.out.println(Thread.currentThread().getName()+" continue");
        }
    }
}