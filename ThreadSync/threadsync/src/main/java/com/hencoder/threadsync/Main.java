package com.hencoder.threadsync;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
//        thread();
//        runnable();
//        threadFactory();
//        executor();
//        callable();
//        runSynchronized1Demo();
//        runSynchronized2Demo();
        runSynchronized3Demo();
//        runReadWriteLockDemo();
    }

    /**
     * 使用 Thread 类来定义工作
     */
    static void thread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread started!");
            }
        };
        thread.start();
    }

    /**
     * 使用 Runnable 类来定义工作
     */
    static void runnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread with Runnable started!");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    static void threadFactory() {
        ThreadFactory factory = new ThreadFactory() {
            AtomicInteger count = new AtomicInteger(0); // int

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread-" + count.incrementAndGet()); // ++count
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " started!");
            }
        };

        Thread thread = factory.newThread(runnable);
        thread.start();
        Thread thread1 = factory.newThread(runnable);
        thread1.start();
    }

    static void executor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread with Runnable started!");
            }
        };

        Executor executor = Executors.newCachedThreadPool();
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);

        ExecutorService myExecutor = new ThreadPoolExecutor(5, 100,
                5, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
        myExecutor.execute(runnable);

        /*
        // 将工作线程设置为SHUTWDOWN状态，能够确保队列里面的任务能够执行完
        myExecutor.shutdown();
        // 将工作线程设置为STOP状态，然后调用Thread的interrupt()方法尽最大努力去终止已经运行的任务
        myExecutor.shutdownNow();

        举个工人吃包子的例子：
        1. 一个厂的工人（Workers）正在吃包子（可以理解为任务），假如接到shutdown的命令，那么这个厂的工人们则会把手头上的包子给吃完，没有拿到手里的笼子里面的包子则不能吃！
        2. 而如果接到shutdownNow的命令以后呢，这些工人们立刻停止吃包子，会把手头上没吃完的包子放下，更别提笼子里的包子了。
        */

        /*
        // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。但是需要注意会有OOM风险，一般项目会自定义线程池，然后配置给RxJava等用于全局
        newCachedThreadPool()
        // 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
        newSingleThreadExecutor()
        // 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待，就是没有那么灵活：不用的时候不会回收用的时候不会增加。一般用于集中处理一次性的爆发性的需求，先用完然后直接shutdown()
        newFixedThreadPool()
        // 创建一个定长线程池，支持定时及周期性任务执行
        newScheduledThreadPool()
        */
    }

    static void callable() {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Done!";
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(callable);
        while (true) {
            if (future.isDone()) {
                try {
                    String result = future.get();
                    System.out.println("result: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    static void runSynchronized1Demo() {
        new Synchronized1Demo().runTest();
    }

    static void runSynchronized2Demo() {
        new Synchronized2Demo().runTest();
    }

    static void runSynchronized3Demo() {
        new Synchronized3Demo().runTest();
    }

    static void runReadWriteLockDemo() {
        new ReadWriteLockDemo().runTest();
    }
}
