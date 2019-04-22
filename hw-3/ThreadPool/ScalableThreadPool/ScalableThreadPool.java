package Scalable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ScalableThreadPool implements ThreadPool{
    private volatile int numThreads;
    private int minThreads;
    private int maxThreads;
    private volatile int runingTasks;

    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>(); //or use BlokingQueue
    private List<Thread> workerThreads = new ArrayList<>();

    public ScalableThreadPool(int minThreads, int maxThreads){
        if (minThreads <= 0 || minThreads > maxThreads) {
            throw new IllegalArgumentException();
        }
        this.maxThreads = maxThreads;
        this.minThreads = minThreads;
        this.numThreads = minThreads;
        this.runingTasks = 0;
    }

    @Override
    public synchronized void start() {
        workerThreads = new ArrayList<>();
        for (int count = 0; count < numThreads; count++) {
            String threadName = "Thread_" + count;
            Thread thread = new Thread(() -> {
                try {
                    runTasks();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }) ;
            thread.setName(threadName);
            workerThreads.add(thread);
            thread.start();
        }
        //System.out.println("WT : " + workerThreads.size() + "NUMM : " + numThreads);
    }

    @Override
    public void execute(Runnable runnable) throws InterruptedException {
        if (runnable == null) {
            throw new NullPointerException();
        }

        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();   // do we need it?
            /*if (runingTasks > 0 && runingTasks >= workerThreads.size() && workerThreads.size() < maxThreads){
                addThread();
            } */
        }

        start();
    }

    public void addThread() throws InterruptedException {

        System.out.println("Add a new thread");
        //System.out.println("RT : " + runingTasks + " WT : " + workerThreads.size() + " Numm " + numThreads);
        /*String threadName = "Thread_" + numThreads;
        Thread thread = new Thread(() -> {
            try {
                runTasks();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }) ;
        thread.setName(threadName);
        workerThreads.add(thread);
        thread.start();
        */
        if (numThreads < maxThreads) {
            this.numThreads += 1;
        }
    }

    public void runTasks() throws InterruptedException {
        while (!taskQueue.isEmpty()){
            //System.out.println(numThreads);
            Runnable runnable = null;
            synchronized (taskQueue) {
                if (!taskQueue.isEmpty()) {
                    //System.out.println(runingTasks);
                    runnable = taskQueue.remove();
                    //System.out.println(runingTasks);
                } else {
                    try {
                        System.out.println("Waiting for tasks");
                        taskQueue.wait(1);
                        removeThreads();
                        numThreads = minThreads;
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //System.out.println(runingTasks);

            this.runingTasks += 1;
            doTask(runnable);
            this.runingTasks -= 1;
        }
    }

    public void doTask(Runnable runnable) throws InterruptedException {

        //this.runingTasks += 1;
        String threadName = Thread.currentThread().getName();
        System.out.println("Task started by " + threadName);
        if (runingTasks > 0 &&
                runingTasks > numThreads &&
                numThreads < maxThreads){

            addThread();
        }
        runnable.run();
        //System.out.println("RT : "+ runingTasks);
        //System.out.println("WT size : " + workerThreads.size());
        //System.out.println("num : " + numThreads);
        System.out.println("Task completed by " + threadName);
        //this.runingTasks -= 1;

    }

    public void removeThreads() {
        if (numThreads > minThreads) {
            for (int i = workerThreads.size() - 1; i >= minThreads; i--) {
                Thread thread = workerThreads.remove(i);
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Removing threads!!!");
        }
    }
}