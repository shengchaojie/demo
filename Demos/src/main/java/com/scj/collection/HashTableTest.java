package com.scj.collection;

import com.google.common.collect.Lists;

import java.util.*;

public class HashTableTest {

    public static void main(String[] args) {
        /*Hashtable<String,String> ht = new Hashtable<>();
        ht.put(null,"123");*/

        List<Integer> test = Lists.newArrayList(1,3,2);
        ListIterator<Integer> integerIterator = test.listIterator();
        while (integerIterator.hasNext()){
            integerIterator.add(1);
            Integer num =integerIterator.next();
            System.out.println(num);
        }
        System.out.println(test);
    }
}
