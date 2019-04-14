public class TaskExecutionException extends RuntimeException {

    public TaskExecutionException(String exceptionMessage){
        super(exceptionMessage);
    }

    public TaskExecutionException(){
        super("TaskExecutionException");
    }
}
