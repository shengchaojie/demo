package com.scj.concurrencyutil;

/**
 * Created by shengchaojie on 2017/5/9.
 */
public class WaitTest2 {
    private static class ThreadA implements Runnable{

        private Object object;

        public ThreadA(Object object) {
            this.object = object;
        }
        @Override
        public void run() {
            synchronized (object){
                //object.notify();
                System.out.println("after notify "+Thread.currentThread().getName());

                try {
                    //object.wait();
                    //object.notify();
                    System.out.println("after wait "+Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object lock =new Object();
        for(int i =0;i<=2;i++){
            new Thread(new ThreadA(lock)).start();
        }

        Thread.sleep(100);

        synchronized (lock){
            System.out.println("main thread into waiting");
            lock.wait();
        }

        System.out.println("end...");
    }
}
