package com.scj.jvm;

import org.h2.Driver;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-04-12 15:47
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Enumeration<URL> urls = ClassLoaderTest.class.getClassLoader().getResources("org/h2/Driver.class");
        while (urls.hasMoreElements()){
            System.out.println(urls.nextElement());
        }
        //通过classloader查找文件时，同在classpath以及jar包内进行查找
        urls = ClassLoaderTest.class.getClassLoader().getResources("META-INF/NOTICE.txt");
        while (urls.hasMoreElements()){
            System.out.println(urls.nextElement());
        }
        //加载类
        Class clazz = ClassLoaderTest.class.getClassLoader().loadClass("org.h2.Driver");
    }
}
