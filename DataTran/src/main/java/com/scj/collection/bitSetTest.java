package com.scj.collection;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by Administrator on 2017/2/4 0004.
 */
public class bitSetTest {
    private static void printBitset(BitSet bs){
        System.out.println("bits:"+bs);
        StringBuilder sb =new StringBuilder();
        for (int i=0;i<bs.size();i++){
            sb.append(bs.get(i)?"1":"0");
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        BitSet bitSet =new BitSet();
        Random random =new Random();
        byte bt =(byte)random.nextInt();//1个byte是8位,这边的功能相当于把10进制转换为2进制
        for (int i=0;i<=7;i++){
            if((1<<i&bt)!=0){
                bitSet.set(i);
            }
        }
        System.out.println(bt);
        printBitset(bitSet);
    }
}
