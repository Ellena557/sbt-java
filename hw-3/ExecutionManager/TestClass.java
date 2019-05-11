public class TestClass {
    private static Context testExecute;

    public static void main(String[] args) {
        Runnable runnable1 = () -> {
            int k = 10;
            for (int i = 0; i < 300000; i++) {
                k++;
            }
        };

        Runnable runnable2 = () -> {
            int k = 10;
            for (int i = 0; i < 900000000; i++) {
                k++;
            }
        };

        Runnable runnable3 = () -> {
            int k = 10;
            for (int i = 0; i < 500000000; i++) {
                k++;
            }
        };

        Runnable[] testTasks = new Runnable[6];

        testTasks[0] = runnable1;
        testTasks[1] = runnable1;
        testTasks[2] = runnable2;
        testTasks[3] = runnable2;
        testTasks[4] = runnable3;
        testTasks[5] = runnable3;

        ExecutionManager myManager = new ExecutionManagerImpl();
        testExecute = myManager.execute(TestClass::callback, testTasks);

        int k = 10;
        for (int i = 0; i < 300000; i++) {
            k++;
        }

        testExecute.interrupt();

        System.out.println("------------ Results of the manager ------------");

        System.out.println("Completed tasks: " + testExecute.getCompletedTaskCount());
        System.out.println("Interrupted tasks: " + testExecute.getInterruptedTaskCount());
        System.out.println("Failed tasks: " + testExecute.getFailedTaskCount());
    }

    private static void callback() {
        System.out.println("callback");
    }
}