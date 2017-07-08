package com.scj;

import org.junit.Test;

import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * Created by Shengchaojie on 2016/5/20.
 */

public class MyTest {

    @Test
    public void testClassLoaderResource()
    {
       try {
           InputStreamReader isr =new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("test.txt"));
           char[] buffer =new char[128];
           int size =0;
           while (( size =isr.read(buffer))!=-1)
           {
               System.out.println(new String(buffer,0,size));
           }

       }catch (Exception ex)
       {

       }

    }

    @Test
    public void test2()
    {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println(MyTest.class.getClassLoader().getResource(""));
        System.out.println(ClassLoader.getSystemResource(""));
        System.out.println(MyTest.class.getResource(""));
        System.out.println(MyTest.class.getResource("/")); // Class文件所在路径
        System.out.println(new File("/").getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test3(){
            BigDecimal decimal =new BigDecimal(0);
            decimal =decimal.add(new BigDecimal((30-10)*1));
            System.out.println(decimal);

        BigDecimal bignum1 = new BigDecimal("10");
        BigDecimal bignum2 = new BigDecimal("5");
        BigDecimal bignum3 = null;

//加法
        bignum3 =  bignum1.add(bignum2);
        System.out.println("和 是：" + bignum3);

//减法
        bignum3 = bignum1.subtract(bignum2);
        System.out.println("差  是：" + bignum3);

//乘法
        bignum3 = bignum1.multiply(bignum2);
        System.out.println("积  是：" + bignum3);

//除法
        bignum3 = bignum1.divide(bignum2);
        System.out.println("商  是：" + bignum3);
    }
}
