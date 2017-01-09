package com.scj.concurrencyutil.producerandconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by shengcj on 2017/1/9.
 */
public class Factory2 {
    private int capacity;
    private int size;
    private Lock lock =new ReentrantLock();
    private Condition fullCondition =lock.newCondition();
    private Condition emptyCondition =lock.newCondition();

    public Factory2(int capacity) {
        this.capacity = capacity;
        this.size = 0;
    }

    public  void produce(int val) throws InterruptedException {
        int left = val;
        lock.lock();
        while (left > 0) {
            if (size >= capacity) {
                fullCondition.await();
            }

            int inc = left + size > capacity ? capacity - size : left;
            size += inc;
            left -= inc;

            System.out.printf("current size:%d ,produce : %d ,left :%d ,%s\n", size, inc, left,Thread.currentThread().getName());
            emptyCondition.signalAll();
        }
        lock.unlock();
    }

    public  void consume(int val) throws InterruptedException {
        int left = val;
        lock.lock();
        while (left > 0) {
            if (size == 0) {
                emptyCondition.await();
            }

            int dec = left > size ? size : left;
            size -= dec;
            left -= dec;

            System.out.printf("current size:%d ,consume:%d,left:%d ,%s\n", size, dec, left,Thread.currentThread().getName());
            fullCondition.signalAll();
        }
        lock.unlock();
    }

    static class Consumer {
        private Factory2 factory;

        public Consumer(Factory2 factory) {
            this.factory = factory;
        }

        public void comsume(final int count) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        factory.consume(count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    static class Producer {
        private Factory2 factory;

        public Producer(Factory2 factory) {
            this.factory = factory;
        }

        public void produce(final int val) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        factory.produce(val);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        Factory2 factory =new Factory2(50);
        Producer producer= new Producer(factory);
        Consumer consumer =new Consumer(factory);

        producer.produce(60);
        consumer.comsume(70);
        producer.produce(80);
    }
}
