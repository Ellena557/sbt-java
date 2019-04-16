import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FixedThreadPool implements ThreadPool {

    /**
     * Количество потоков задается в конструкторе и не меняется.
     */

    private int numThreads;
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>(); //or use BlokingQueue
    private final List<Thread> workerThreads = new ArrayList<>();

    public FixedThreadPool(int numThreads) {
        if (numThreads <= 0) {
            throw new IllegalArgumentException();
        }
        this.numThreads = numThreads;
    }

    @Override
    public void start() {
        //workerThreads = null;
        for (int count = 0; count < numThreads; count++) {
            String threadName = "Thread_" + count;
            //System.out.println(threadName);
            Thread thread = new Thread(() -> {runTasks();}) ;
            thread.setName(threadName);
            workerThreads.add(thread);
            thread.start();
        }
        //System.out.println(workerThreads + "hooray");
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        
        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();   // do we need it?
        }
    }

    public void runTasks(){
        start();
        while (!taskQueue.isEmpty()){
            //System.out.println(workerThreads);
            for (Thread thread: workerThreads ) {
                //thread.start();
            }
            if(!taskQueue.isEmpty()) {
                synchronized (taskQueue.peek()) {
                    Runnable runnable = taskQueue.poll();
                    if (runnable != null) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println("Task started by" + threadName);
                        runnable.run();
                        System.out.println("Task completed by" + threadName);
                    } else {
                            System.out.println("Waiting for tasks");

                    }
                }
            }
        }
    }
}


