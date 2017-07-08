package com.scj.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class TestMain {
    public static void main(String[] args) {
        ServiceLoader<Animal> serviceLoader =ServiceLoader.load(Animal.class);
        Iterator<Animal> animals =serviceLoader.iterator();
        while (animals.hasNext())
        {
            Animal animal =animals.next();
            animal.walk();
        }

    }
}
