import java.util.concurrent.Callable;

public class TestClass {
    public static void main(String[] args) {

        Task<String> task = new Task<>(() -> {
            Thread.sleep(100);
            return "task answer";
        });

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
    }
}
