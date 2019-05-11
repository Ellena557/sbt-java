import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionManagerImplThreadPool implements ExecutionManager {
    private AtomicInteger interruptedTaskCount = new AtomicInteger();
    private AtomicInteger completedTaskCount = new AtomicInteger();
    private AtomicInteger failedTaskCount = new AtomicInteger();
    private final Object lock = new Object();
    private ExecutorService threadPool = Executors.newFixedThreadPool(8);
    private final AtomicBoolean isInterrupted = new AtomicBoolean();
    private int currentTaskNumber;

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        if (tasks == null || tasks.length == 0) {
            throw new NullPointerException();
        }

        isInterrupted.set(false);
        int taskNumber = tasks.length;
        currentTaskNumber = taskNumber;


        for (int i = 0; i < tasks.length; i++) {
            threadPool.execute(new RunThreadsClass(tasks[i]));
        }

        threadPool.shutdown();

        createCallbackThread(callback);

        return new ContextImpl(taskNumber);
    }

    private void createCallbackThread(Runnable callback) {
        Thread callbackThread = new Thread(callback::run);
        callbackThread.setName("callback");
        callbackThread.start();
    }

    public class ContextImpl implements Context {
        private int tasksNumber;

        ContextImpl(int tasksNumber) {
            this.tasksNumber = tasksNumber;
        }

        @Override
        public int getCompletedTaskCount() {
            synchronized (lock) {
                return completedTaskCount.get();
            }
        }

        @Override
        public int getFailedTaskCount() {
            synchronized (lock) {
                return failedTaskCount.get();
            }
        }

        @Override
        public int getInterruptedTaskCount() {
            synchronized (lock) {
                return interruptedTaskCount.get();
            }
        }

        @Override
        public void interrupt() {
            synchronized (lock) {
                isInterrupted.set(true);
                if (currentTaskNumber > 0) {
                    interruptedTaskCount.addAndGet(currentTaskNumber);
                }
            }
        }

        @Override
        public boolean isFinished() {
            synchronized (lock) {
                return (getCompletedTaskCount() + getInterruptedTaskCount() == tasksNumber);
            }
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

                synchronized (lock) {
                    if (!isInterrupted.get()) {
                        task.run();
                        completedTaskCount.incrementAndGet();
                        currentTaskNumber --;
                    }
                }
            }
            catch (InterruptedException e) {
                //pass
            }
            catch (Exception e){
                synchronized (lock) {
                    failedTaskCount.incrementAndGet();
                    currentTaskNumber --;
                }
            }
        }
    }
}
