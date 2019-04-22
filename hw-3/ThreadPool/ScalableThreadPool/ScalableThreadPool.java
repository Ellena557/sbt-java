package Scalable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ScalableThreadPool implements ThreadPool{
    private volatile int numThreads;
    private int minThreads;
    private int maxThreads;
    private volatile int runningTasks;

    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>(); //or use BlokingQueue
    private List<Thread> workerThreads = new ArrayList<>();

    public ScalableThreadPool(int minThreads, int maxThreads){
        if (minThreads <= 0 || minThreads > maxThreads) {
            throw new IllegalArgumentException();
        }
        this.maxThreads = maxThreads;
        this.minThreads = minThreads;
        this.numThreads = minThreads;
        this.runningTasks = 0;
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
    }

    @Override
    public void execute(Runnable runnable) throws InterruptedException {
        if (runnable == null) {
            throw new NullPointerException();
        }

        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();   // do we need it?
        }

        start();
    }

    public void addThread() throws InterruptedException {
        System.out.println("Add a new thread");

        if (numThreads < maxThreads) {
            this.numThreads += 1;
        }
    }

    public void runTasks() throws InterruptedException {
        while (!taskQueue.isEmpty()){
            Runnable runnable = null;

            synchronized (taskQueue) {
                if (!taskQueue.isEmpty()) {
                    runnable = taskQueue.remove();
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

            this.runningTasks += 1;
            doTask(runnable);
            this.runningTasks -= 1;
        }
    }

    public void doTask(Runnable runnable) throws InterruptedException {

        String threadName = Thread.currentThread().getName();
        System.out.println("Task started by " + threadName);

        if (runningTasks > 0 &&
                runningTasks > numThreads &&
                numThreads < maxThreads){
            addThread();
        }

        runnable.run();

        System.out.println("Task completed by " + threadName);
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
