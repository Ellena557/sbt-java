package Fixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FixedThreadPoolNew implements ThreadPool {

    /**
     * Количество потоков задается в конструкторе и не меняется.
     */

    private final int numThreads;
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    private List<Thread> workerThreads = new ArrayList<>();
    private volatile boolean taskTaken = false;

    public FixedThreadPoolNew(int numThreads) {
        if (numThreads <= 0) {
            throw new IllegalArgumentException();
        }
        this.numThreads = numThreads;
    }

    @Override
    public void start() {
        for (int count = 0; count < numThreads; count++) {
            String threadName = "Thread_" + count;
            Thread thread = new Thread(this::runTasks);
            thread.setName(threadName);
            workerThreads.add(thread);
            thread.setDaemon(true);
            thread.start();
        }

        try {
            for (Thread thread : workerThreads) {
                thread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }

        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();
        }
    }

    public void runTasks() {
        while (true) {
            Runnable runnable = null;
            taskTaken = false;
            if (!taskQueue.isEmpty()) {
                synchronized (taskQueue) {
                    if (!taskQueue.isEmpty()) {
                        runnable = taskQueue.remove();
                        taskTaken = true;
                    } else {
                        try {
                            System.out.println("Waiting for tasks");
                            taskQueue.wait(1);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (taskTaken) {
                    doTask(runnable);
                    taskTaken = false;
                }
            }
        }

    }

    public void doTask(Runnable runnable) {
        String threadName = Thread.currentThread().getName();
        System.out.println("Task started by " + threadName);
        runnable.run();
        System.out.println("Task completed by " + threadName);
    }
}
