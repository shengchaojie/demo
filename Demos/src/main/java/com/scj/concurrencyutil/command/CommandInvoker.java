package com.scj.concurrencyutil.command;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * 对运行中的任务可以进行控制，比如运行中几个，等待中几个，超过等待队列的直接报出异常
 * Created by Administrator on 2017/2/12 0012.
 */
public class CommandInvoker {

    //用于控制运行中的线程
    //private Semaphore semaphore;

    //等待线程的数量
    private int waitingSize =5;

    //監控等待線程的數量
    private volatile int waitingStatus =0;

    private BlockingQueue<Command> commandQueue;

    private static final int SLEEPTIME=1000;

    public CommandInvoker(int waitingSize,int semaphoreSize) {
        this.waitingSize = waitingSize;
        this.commandQueue =new ArrayBlockingQueue<Command>(waitingSize);
        //this.semaphore =new Semaphore(semaphoreSize);
        for(int i=0;i<semaphoreSize;i++){
            new Thread(new Worker(commandQueue)).start();
        }
    }

    public void invoke(Command command){
        if(!this.commandQueue.offer(command)){
            System.out.println("不能再加命令");
        }
    }

    private static class Worker implements Runnable{

        private BlockingQueue<Command> commandQueue;

        public Worker(BlockingQueue<Command> commandQueue){
            this.commandQueue =commandQueue;
        }

        @Override
        public void run() {
            System.out.println("worker started!");
            while (true) {
                try {

                    Command command = commandQueue.take();
                    command.run();
                    Thread.sleep(SLEEPTIME);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class SleepCommand implements Command{

        private String id;

        public SleepCommand(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println(id+":sleep 100 seconds");
                Thread.sleep(SLEEPTIME);
                System.out.println(id+":sleep over");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CommandInvoker commandInvoker =new CommandInvoker(10,3);
        for (int i=1;i<=100;i++){
            commandInvoker.invoke(new SleepCommand(String.valueOf(i)));
            if(i%10==0){
                try {
                    Thread.sleep(SLEEPTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
