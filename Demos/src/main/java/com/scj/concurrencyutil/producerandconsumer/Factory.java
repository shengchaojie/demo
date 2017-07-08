package com.scj.concurrencyutil.producerandconsumer;

/**
 * Created by shengcj on 2017/1/9.
 */
public class Factory {
    private int capacity;
    private int size;

    public Factory(int capacity) {
        this.capacity = capacity;
        this.size = 0;
    }

    public synchronized void produce(int val) throws InterruptedException {
        int left = val;

        while (left > 0) {
            if (size >= capacity) {
                wait();
            }

            int inc = left + size > capacity ? capacity - size : left;
            size += inc;
            left -= inc;

            System.out.printf("current size:%d ,produce : %d ,left :%d ,%s\n", size, inc, left,Thread.currentThread().getName());
            notifyAll();
            //如果最后剩下2个生产 线程的时候，会互相唤醒，导致死锁
        }

    }

    public synchronized void consume(int val) throws InterruptedException {
        int left = val;

        while (left > 0) {
            if (size == 0) {
                wait();
            }

            int dec = left > size ? size : left;
            size -= dec;
            left -= dec;

            System.out.printf("current size:%d ,consume:%d,left:%d ,%s\n", size, dec, left,Thread.currentThread().getName());
            notifyAll();
        }
    }

    static class Consumer {
        private Factory factory;

        public Consumer(Factory factory) {
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
        private Factory factory;

        public Producer(Factory factory) {
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
        Factory factory =new Factory(50);
        Producer producer= new Producer(factory);
        Consumer consumer =new Consumer(factory);

        producer.produce(60);
        consumer.comsume(70);
        producer.produce(80);
    }
}
