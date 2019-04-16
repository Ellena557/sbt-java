import com.sun.jmx.snmp.tasks.Task;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FixedThreadPoolTest {

    @Test
    public void poolStartsWorking(){
        FixedThreadPool threadPool = new FixedThreadPool(4);

        threadPool.start();

        ArrayList<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            tasks.add(new Task(i));
        }

        for (Runnable task : tasks) {
            threadPool.execute(task);
        }

        //threadPool.runTasks();
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
    public void pool2Works(){
        FixedThreadPool2 threadPool2 = new FixedThreadPool2(4);

        threadPool2.start();

        ArrayList<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tasks.add(new Task(i));
        }

        for (Runnable task : tasks) {
            threadPool2.execute(task);
        }

        //threadPool.runTasks();
    }
}