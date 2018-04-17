package com.scj.jvm;

import sun.misc.GC;

/**
 -Xms40M
 -Xmx40M
 -Xmn30M
 -XX:+PrintGCDetails
 -XX:+PrintGCTimeStamps
 * 不是说好会回收这里么，不知道怎么回事居然没有回收
 * Created by shengchaojie on 2017/7/16.
 */
public class GCTest1 {

    private static final int _1MB =1024*1024;

    private byte[] byteSize = new byte[2*_1MB];

    public Object reference =null;

    public static void main(String[] args) {

        GCTest1 reference1 =new GCTest1();
        GCTest1 reference2 =new GCTest1();

        //reference1.reference =reference2;
        //reference2.reference =reference1;

        System.gc();
    }
}

/**
 0.337: [GC (System.gc()) [PSYoungGen: 8357K->3068K(27136K)] 8357K->5124K(37376K), 0.0033562 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 0.341: [Full GC (System.gc()) [PSYoungGen: 3068K->0K(27136K)] [ParOldGen: 2056K->4990K(10240K)] 5124K->4990K(37376K), [Metaspace: 3347K->3347K(1056768K)], 0.0085578 secs] [Times: user=0.01 sys=0.01, real=0.01 secs]
 Heap
 PSYoungGen      total 27136K, used 235K [0x00000007be200000, 0x00000007c0000000, 0x00000007c0000000)
 eden space 23552K, 1% used [0x00000007be200000,0x00000007be23af88,0x00000007bf900000)
 from space 3584K, 0% used [0x00000007bf900000,0x00000007bf900000,0x00000007bfc80000)
 to   space 3584K, 0% used [0x00000007bfc80000,0x00000007bfc80000,0x00000007c0000000)
 ParOldGen       total 10240K, used 4990K [0x00000007bd800000, 0x00000007be200000, 0x00000007be200000)
 object space 10240K, 48% used [0x00000007bd800000,0x00000007bdcdf9a8,0x00000007be200000)
 Metaspace       used 3354K, capacity 4496K, committed 4864K, reserved 1056768K
 class space    used 369K, capacity 388K, committed 512K, reserved 1048576K
fullgc前会先出发一次minorgc,至于为什么这2个对象还在，暂时还没调查出原因
 */
