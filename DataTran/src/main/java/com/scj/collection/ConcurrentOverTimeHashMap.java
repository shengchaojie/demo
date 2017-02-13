package com.scj.collection;

import javax.rmi.CORBA.Tie;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 给concurrentHashmap加上超时的功能
 * Created by Administrator on 2017/2/12 0012.
 */
public class ConcurrentOverTimeHashMap<K, V> {

    private Map<K, WrappedValue<V>> map = null;

    public ConcurrentOverTimeHashMap() {
        this.map = new ConcurrentHashMap<>();
    }

    public V get(K key) {
        long systemTime = System.currentTimeMillis();
        WrappedValue<V> value = map.get(key);
        //如果timeout设置小于等于0,相当于这个元素不会超时
        if (value == null){
            return null;
        }
        if(value.getTimeout() > 0 &&
                value.getTimeout() <= systemTime) {
            //已经超时的元素，从这里移除
            map.remove(key);
            return null;
        }
        return value.getValue();
    }

    public void put(K key, V value, long timeout) {
        map.put(key, new WrappedValue<V>(value, timeout));
    }

    public void remove(K key){
        map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.containsValue(new WrappedValue<V>(value, 0));
    }

    private static class WrappedValue<V> {

        private V value;
        private long timeout;

        public WrappedValue(V value, long timeout) {
            this.value = value;
            this.timeout = timeout;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WrappedValue<?> that = (WrappedValue<?>) o;

            return value != null ? value.equals(that.value) : that.value == null;

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public static void main(String[] args) {
        final ConcurrentOverTimeHashMap<String,String> timeHashMap =new ConcurrentOverTimeHashMap<>();
        timeHashMap.put("123","123",System.currentTimeMillis()+10000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count =1;
                while (true) {
                    String value = timeHashMap.get("123");
                    System.out.println(count++ +" counts to get,value = "+value);
                    if(value ==null){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
