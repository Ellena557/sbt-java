import java.util.concurrent.Callable;

public class Task1<T> {
    private final Callable<? extends T> callable;
    private T result;                                   //volatile излишний 
    private TaskExecutionException taskException;       //volatile излишний
    private volatile boolean isCounted;

    public Task1(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {
        if (!isCounted) {
            synchronized (this) {
                if (!isCounted) {
                    try {
                        return result = callable.call();
                    } catch (Exception e) {
                        this.taskException = new TaskExecutionException(e.getMessage());
                        throw this.taskException;
                    } finally {
                        isCounted = true;
                    }
                }
            }
        }

        if (taskException != null) throw taskException;
        return result;
    }
}
