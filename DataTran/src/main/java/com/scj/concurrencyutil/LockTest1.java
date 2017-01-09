package com.scj.concurrencyutil;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by shengcj on 2016/12/15.
 */
public class LockTest1 {
    public static void main(String[] args) {
        final ReentrantLock reentrantLock =new ReentrantLock();
        final Condition condition =reentrantLock.newCondition();

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    System.out.println("被锁住了需要解锁！"+this);
                    condition.await();//await和wait一样，也相当于会把锁让出去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("拿到了锁"+this);
            }
        },"waitThread");
        thread.start();

        Thread thread1 =new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLock.lock();
                System.out.println("我拿到了锁");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                condition.signalAll();//这边的singalall和notify一样，会执行结束才让锁，除非中间再await
                //singalAll 和 notify 其实并不是让另外等待线程运行 而是激活它 让他进入就绪状态
                // 释放锁这个行为是窒息执行到同步代码块后面 或者手动释放锁 造成的
                //结论notify 和 singal 并不是释放锁 只会激活线程
                System.out.println("我发了个信号");
                reentrantLock.unlock();
            }
        },"signalThread");
        thread1.start();
    }
}
