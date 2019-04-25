import java.util.concurrent.Callable;

public class Task1<T> {
    private final Callable<? extends T> callable;
    private volatile T result;
    private RuntimeException taskException;
    private volatile boolean isCounted;

    public Task1(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {

        T taskResult;

        if (isCounted) {
            taskResult =  result;
        }

        else {
            synchronized (this) {
                if (taskException != null) {
                    throw taskException;
                }

                if (!isCounted) {
                    try {
                        result = callable.call();
                    } catch (Exception e) {
                        taskException =  new RuntimeException(e.getMessage());
                        throw taskException;
                    }
                    isCounted = true;
                }
                taskResult = result;
            }
        }

        return taskResult;
    }
}
