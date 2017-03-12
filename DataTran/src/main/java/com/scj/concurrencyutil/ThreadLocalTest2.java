package com.scj.concurrencyutil;

import org.springframework.core.NamedThreadLocal;

import java.util.Date;

/**
 * Created by USER on 2017/3/10.
 * InheritableThreadLocal的threadlocal会复制到子线程 threaclocal不会
 * */
public class ThreadLocalTest2 {

    public static void main(String[] args) throws InterruptedException {
        final MyLocalObject object =new MyLocalObject();
        object.test();
        Thread.sleep(10000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                object.test();
            }
        }).start();

    }

    static class MyLocalObject{
        ThreadLocal<String> tl1 =new InheritableThreadLocal<String>(){
            @Override
            protected String childValue(String parentValue) {
                return parentValue +"hello";
            }

            @Override
            protected String initialValue() {
                return new Date().toString();
            }
        };

        ThreadLocal<String> tl2 =new ThreadLocal<String>(){
            @Override
            protected String initialValue() {
                return new Date().toString();
            }
        };

        public void test(){
            System.out.println(tl1.get());
            System.out.println(tl2.get());
        }
    }
}
