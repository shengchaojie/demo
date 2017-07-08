package com.scj.collection;

import java.util.*;

/**
 * Created by Administrator on 2017/2/3 0003.
 */
public class ListTests {
    private static final String data ="one two three four five six seven eight nine ten";
    private static List<String> testList =null;

    static{
        String[] datas =data.split(" ");
        List<String> tempList =Arrays.asList(datas);
        System.out.println(tempList);
        datas[0]="123";//这个的修改 对上面的testList 产生影响 所以内部引用了数组
        System.out.println(tempList);
        testList =new ArrayList<>(tempList);//这边是深拷贝 再修改无效
        datas[0]="456";
        tempList.set(0,"789");
        System.out.println(testList);
    }

    public static void iterationTest() {
        ListIterator<String> listIterator =testList.listIterator();
        listIterator.add("scj");
        listIterator.next();
        System.out.println(testList);
        listIterator.set("scj-change");
        System.out.println(testList);
        listIterator.next();
        listIterator.set("scj2");
        listIterator.remove();
        //listIterator.remove();//already remove can not remove again
        System.out.println(testList);
        listIterator.next();
        listIterator.set("scj2");
        listIterator.set("scj3");
        System.out.println(testList);
    }

    public static void main(String[] args) {
        iterationTest();
    }
}
