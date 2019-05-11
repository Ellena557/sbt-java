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

        createThreads(callback, tasks);
        callbackThread = createCallbackThread(callback);

        return new ContextImpl(taskNumber);
    }

    private void createThreads(Runnable callback, Runnable... tasks){

        //not to create useless threads
        /*if (numThreads > tasks.length){
            numThreads = tasks.length;
        }*/

        numThreads = tasks.length;

        for (int count = 0; count < numThreads; count++) {
            String threadName = "Thread_" + count;
            Thread thread = new Thread(new RunThreadsClass(taskQueue.poll()));
            thread.setName(threadName);
            threads.add(thread);
            thread.start();
        }
    }

    private Thread createCallbackThread(Runnable callback){
        Thread callbackThread = new Thread(callback::run);
        callbackThread.setName("callback");
        callbackThread.start();
        return callbackThread;
    }

    public class ContextImpl implements Context {
        //private AtomicInteger completedTaskCount;
        //private AtomicInteger failedTaskCount;
        //private AtomicInteger interruptedTaskCount = new AtomicInteger();
        private int tasksNumber;
        //private List<Thread> threads;

        public ContextImpl(int tasksNumber) {
            //this.completedTaskCount = completedTaskCount;
            //this.failedTaskCount = failedTaskCount;
            //this.threads = threads;
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
            //System.out.println("INTERRUPTING");
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
            System.out.println("C " + completedTaskCount.get());
            System.out.println(getCompletedTaskCount());
            System.out.println(getInterruptedTaskCount());
            System.out.println(" I " + interruptedTaskCount.get());
            System.out.println(tasksNumber);
            return (getCompletedTaskCount() + getInterruptedTaskCount() == tasksNumber);
        }
    }


    public class RunThreadsClass implements Runnable{

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
                //System.out.println(isInterrupted);

                if (!isInterrupted.get()) {
                    //synchronized (isInterrupted) {
                        //if (!isInterrupted.get()) {
                    task.run();
                    completedTaskCount.incrementAndGet();
                    //System.out.println("COMPLETED : " + Thread.currentThread().getName());
                    //System.out.println("CC " + isInterrupted.get());
                        //}
                    //}
                }
            }
            catch (InterruptedException e){
                //interruptedTaskCount.incrementAndGet();
                //System.out.println("INTERRUPTED : " + Thread.currentThread().getName());
            }
            catch (Exception e){
                failedTaskCount.incrementAndGet();
            }
        }
    }
}


