package com.scj.concurrencyutil;

/**
 * Created by shengcj on 2017/1/9.
 */
public class SleepTest {
    public static final Object object =new Object();

    static class ThreadA extends Thread{
        public ThreadA(String name){
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"in");
            synchronized (object){
                for (int i=0;i<10;i++){
                    System.out.printf("%s: %d\n",this.getName(),i);
                    if(i%4==0){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public static void main(String[] args) {
            ThreadA t1 =new ThreadA("t1");
            ThreadA t2 =new ThreadA("t2");
            t1.start();
            t2.start();
        }
    }
}
