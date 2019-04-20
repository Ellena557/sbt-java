package Fixed;

import Fixed.FixedThreadPool;
import Fixed.FixedThreadPool2;
import org.junit.Test;

import java.util.ArrayList;

public class FixedThreadPoolTest {

    static class Task implements Runnable {
        String createdBy;
        int number;

        Task(int number) {
            createdBy = Thread.currentThread().getName();
            this.number = number;
        }

        @Override
        public void run() {
            System.out.println("Task: " + number + " is created by thread: " + createdBy + " and is executed by: "
                        + Thread.currentThread().getName());
        }
    }

    @Test
    public void poolWorks(){
        FixedThreadPool threadPool = new FixedThreadPool(6);

        ArrayList<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            tasks.add(new Task(i));
        }

        for (Runnable task : tasks) {
            threadPool.execute(task);
        }
    }
}
