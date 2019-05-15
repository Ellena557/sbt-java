package Fixed;

import Fixed.FixedThreadPool;
import Fixed.FixedThreadPool2;
import org.junit.Test;

import java.util.ArrayList;

public class FixedThreadPoolTestNew {
   
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
    public void poolNewWorks(){
        ArrayList<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            tasks.add(new Task(i));
        }

        FixedThreadPool3 threadPool3 = new FixedThreadPool3(6);

        for (Runnable task : tasks) {
            threadPool3.execute(task);
        }

        threadPool3.start();
    }
}
