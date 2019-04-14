import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private volatile T result;
    private TaskExecutionException taskException = null;


    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {

        /*if (taskException != null) {
            throw taskException;
        }
        */

        T taskResult = null;

        if (result != null){
            taskResult =  result;
        }

        else {
            synchronized (this){
                if (result == null) {
                    try {
                        result = callable.call();
                    } catch (Exception e) {
                        taskException = new TaskExecutionException(e.getMessage());
                        throw taskException;
                    }
                }
                taskResult = result;
            }
        }

        return taskResult;
    }
}

