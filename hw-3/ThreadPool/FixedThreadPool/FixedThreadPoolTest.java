package Fixed;

import Fixed.FixedThreadPool;
import Fixed.FixedThreadPool2;
import org.junit.Test;

import java.util.ArrayList;

public class FixedThreadPoolTest {

    @Test
    public void poolOneWorks(){
        FixedThreadPool threadPool = new FixedThreadPool(4);


        ArrayList<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tasks.add(new Task(i));
        }

        threadPool.start();
        for (Runnable task : tasks) {
            threadPool.execute(task);
        }
    }

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
    public void poolTwoWorks(){
        FixedThreadPool2 threadPool2 = new FixedThreadPool2(4);

        threadPool2.start();

        ArrayList<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tasks.add(new Task(i));
        }

        for (Runnable task : tasks) {
            threadPool2.execute(task);
        }
    }

    @Test
    public void poolThreeWorks(){
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