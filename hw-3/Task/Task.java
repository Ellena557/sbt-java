import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private volatile T result;


    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {
        T taskResult = null;

        if (result != null) {
            taskResult =  result;
        } else {
            synchronized (this) {
                if (result == null) {
                    try {
                        result = callable.call();
                    } catch (Exception e) {
                        TaskExecutionException taskException = new TaskExecutionException(e.getMessage());
                        throw taskException;
                    }
                }
                taskResult = result;
            }
        }

        return taskResult;
    }
}
