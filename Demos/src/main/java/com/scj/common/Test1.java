package com.scj.common;

/**
 * Created by shengchaojie on 2017/11/12.
 */
public class Test1 {

    static class SubClass extends SuperClass
    {
        public String name = "SubClass";
    }

    static class SuperClass
    {
        public String name = "SuperClass";
    }


    public static void main(String[] args)
    {
        SuperClass clz = new SubClass();
        //你觉得这里输出什么?
        System.out.println(clz.name);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000L);
                    System.out.println("scj");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
    }



}
