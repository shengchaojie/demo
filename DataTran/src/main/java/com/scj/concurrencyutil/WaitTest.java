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
               //notify();//notify之后 要等到这个代码块结束之后才会把锁让出去，当然如果在notify之后又有wait，那就会主动把锁让出去


               try {
                   //System.out.println(Thread.currentThread().getName()+" wait");
                   //wait();

                   Thread.sleep(10000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               System.out.println(Thread.currentThread().getName()+" after notify");
           }
        }
    }

    public static void main(String[] args) {
        ThreadA t1 =new ThreadA("t1");

        synchronized (t1){
            System.out.println(Thread.currentThread().getName()+" start t1");
            t1.start();

            System.out.println(Thread.currentThread().getName()+" wait");
            try {
                t1.wait();

                //System.out.println(Thread.currentThread().getName()+" notify");
               // t1.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" continue");
        }
    }
}
