import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionManagerImpl implements ExecutionManager {
    private int numThreads;
    private AtomicInteger interruptedTaskCount = new AtomicInteger();
    private AtomicInteger completedTaskCount = new AtomicInteger();
    private AtomicInteger failedTaskCount = new AtomicInteger();
    private Queue<Runnable> taskQueue;
    private List<Thread> threads = new ArrayList<>();
    private final AtomicBoolean isInterrupted = new AtomicBoolean();
    private Thread callbackThread;


    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        if (tasks == null || tasks.length == 0) {
            throw new NullPointerException();
        }
        
        isInterrupted.set(false);
        taskQueue = new ConcurrentLinkedQueue<>(Arrays.asList(tasks));
        int taskNumber = tasks.length;

        //little check
        assert taskNumber == taskQueue.size();

        createThreads(tasks);
        callbackThread = createCallbackThread(callback);

        return new ContextImpl(taskNumber);
    }

    private void createThreads(Runnable... tasks){

        //not to create useless threads
        numThreads = tasks.length;

        for (int count = 0; count < numThreads; count++) {
            String threadName = "Thread_" + count;
            Thread thread = new Thread(new RunThreadsClass(taskQueue.poll()));
            thread.setName(threadName);
            threads.add(thread);
            thread.start();
        }
    }

    private Thread createCallbackThread(Runnable callback) {
        Thread callbackThread = new Thread(callback::run);
        callbackThread.setName("callback");
        callbackThread.start();
        return callbackThread;
    }

    public class ContextImpl implements Context {
        private int tasksNumber;

        public ContextImpl(int tasksNumber) {
            this.tasksNumber = tasksNumber;
        }

        @Override
        public int getCompletedTaskCount() {
            return completedTaskCount.get();
        }

        @Override
        public int getFailedTaskCount() {
            return failedTaskCount.get();
        }

        @Override
        public int getInterruptedTaskCount() {
            return interruptedTaskCount.get();
        }

        @Override
        public void interrupt() {
            isInterrupted.set(true);

            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    thread.interrupt();
                    interruptedTaskCount.incrementAndGet();
                }
            }

            callbackThread.interrupt();
        }

        @Override
        public boolean isFinished() {
            return (getCompletedTaskCount() + getInterruptedTaskCount() == tasksNumber);
        }
    }


    public class RunThreadsClass implements Runnable {

        private final Runnable task;

        public RunThreadsClass(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            try {
                if(Thread.currentThread().isInterrupted()){
                    throw new InterruptedException();
                }

                if (!isInterrupted.get()) {
                    task.run();
                    completedTaskCount.incrementAndGet();
                }
            }
            catch (InterruptedException e){
                interruptedTaskCount.incrementAndGet();
            }
            catch (Exception e){
                failedTaskCount.incrementAndGet();
            }
        }
    }
}
