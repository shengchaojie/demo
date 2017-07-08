package com.scj.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2017/2/4 0004.
 */
public class WeakHashMapTest {
      static class Element{
        private String ident;

        public Element(String ident) {
            this.ident = ident;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "ident='" + ident + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Element element = (Element) o;

            return ident != null ? ident.equals(element.ident) : element.ident == null;

        }

        @Override
        public int hashCode() {
            return ident != null ? ident.hashCode() : 0;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("finalizing "+getClass().getSimpleName()+" "+ident);
        }
    }

    static class Key extends Element{
        public Key(String ident) {
            super(ident);
        }
    }

    static class value extends  Element{
        public value(String ident) {
            super(ident);
        }
    }

    public static void main(String[] args) {
        int size =100;
        if(args.length>=1){
            size =new Integer(args[0]);
        }

        Key[] keys =new Key[size];
        Map map =new WeakHashMap<Key,value>();
        for(int i=0;i<size;i++){
            //Key key =new WeakHashMapTest().new Key(Integer.toString(i));
            //如果不是内部静态类就要这么使用 比较麻烦
            Key key =new Key(Integer.toString(i));
            value value =new value(Integer.toString(i));
            if(i%3==0){
                keys[i]=key;//这边的对象是可获得的，所以回收不掉
            }
            map.put(key,value);
        }
        System.gc();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
