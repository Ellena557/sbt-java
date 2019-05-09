package Scalable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPool2 implements ThreadPool {
    private AtomicInteger numThreads;
    private int minThreads;
    private int maxThreads;

    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    private List<Thread> workerThreads = new ArrayList<>();

    public ScalableThreadPool2(int minThreads, int maxThreads) {
        if (minThreads <= 0 || minThreads > maxThreads) {
            throw new IllegalArgumentException();
        }

        this.maxThreads = maxThreads;
        this.minThreads = minThreads;
        this.numThreads = new AtomicInteger(minThreads);
    }

    @Override
    public synchronized void start() {
        workerThreads = new ArrayList<>();
        for (int count = 0; count < numThreads.get(); count++) {
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
            if (taskQueue.size() >= 2){
                addThread();
            }
            taskQueue.add(runnable);
            taskQueue.notify();
        }
    }

    public void addThread() throws InterruptedException {

        if (numThreads.get() < maxThreads) {
            System.out.println("Add a new thread");
            this.numThreads.addAndGet(1);
            String threadName = "Thread_" + numThreads;
            Thread thread = new Thread(() -> {
                try {
                    runTasks();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.setName(threadName);
            workerThreads.add(thread);
            thread.start();
        }
    }

    public void runTasks() throws InterruptedException {
        while (true) {
            if (!taskQueue.isEmpty()) {
                Runnable runnable = null;

                synchronized (taskQueue) {
                    if (!taskQueue.isEmpty()) {
                        runnable = taskQueue.remove();
                    } else {
                        try {
                            System.out.println("Waiting for tasks");
                            taskQueue.wait(1);
                            removeThreads();
                            numThreads.set(minThreads);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                doTask(runnable);
            }
        }
    }

    public void doTask(Runnable runnable) throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.println("Task started by " + threadName);
        runnable.run();
        System.out.println("Task completed by " + threadName);
    }

    public void removeThreads() {
        if (numThreads.get() > minThreads) {
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
