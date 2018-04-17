package com.scj.common;

/**
 * Created by shengchaojie on 2017/11/12.
 */
public class Test2 {
    {
        System.out.println("初始化代码");
    }

    Test2()
    {
        System.out.println("构造器");
    }

    static
    {
        System.out.println("静态代码块");
    }

    //运行后输出结果?
    public static void main(String[] args)
    {
        {
            int a = 10;
            //10
            System.out.println("局部代码块");
        }

        new Test2();
        new Test2();
        new Test2();
    }

    //静态代码块
    //局部代码块
    //初始化代码
    //构造器

}
