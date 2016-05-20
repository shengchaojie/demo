package com.scj;

import org.junit.Test;

import java.io.File;
import java.io.InputStreamReader;

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
}
