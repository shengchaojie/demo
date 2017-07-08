package com.scj.spi;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class Dog implements Animal{
    @Override
    public void walk() {
        System.out.println("dog");
    }
}
