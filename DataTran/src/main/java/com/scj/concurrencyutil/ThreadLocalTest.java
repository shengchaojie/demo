package com.scj.concurrencyutil;

import java.util.Date;

/**
 * Created by shengcj on 2016/8/19.
 * InheritableThreadLocal 里面线程绑定的值能被子线程使用
 * 但是ThreadLocal只能当前线程使用
 */
public class ThreadLocalTest {
    public static  ThreadLocal<String> itl = new InheritableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return new Date().toString();
        }
        @Override
        protected String childValue(String parentValue) {
            return parentValue + " hello";
        }
    };
    public static  ThreadLocal<String> it2 = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return new Date().toString();
        }

    };
    static {
        System.out.println(itl.get());
        System.out.println(it2.get());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        itl.get();
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
                System.out.println(Thread.currentThread().getName() + " : " + it2.get());
            }
        }).start();
    }
}
