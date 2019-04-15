import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TestClass {
    static int counter;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start run1");
        run1();
        System.out.println("Finish run1");

        Thread.sleep(1000);

        System.out.println("Start run2");
        run2();
        System.out.println("Finish run2");

    }

    public static void run1(){
        Task<String> task = new Task<>(() -> {
            Thread.sleep(100);
            return "task answer";
        });

        /* every thread works with the task */
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " : " + task.get());
                } catch (Exception e) {
                    e.getMessage();
                }
            });

            thread.start();
        }
        //System.out.println("Finish run1");
    }

    public static void run2() throws InterruptedException {

        counter = 5;

        Thread.sleep(100);
        Task<Integer> task = new Task<>(() -> {
            Thread.sleep(100);
            return counter;
        });

        /* make each thread run several times */
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                try {
                    runThread(task);
                } catch (Exception e) {
                    e.getMessage();
                }
            });

            thread.start();
        }

    }

    private static void runThread(Task<Integer> task){
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " : " + task.get() + " iteration " + i);
        }
    }


}
