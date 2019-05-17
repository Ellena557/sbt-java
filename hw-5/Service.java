import java.util.List;

public interface Service {
    @Cache(identityBy = String.class)
    double doHardWork(String s, int a);

    @Cache(identityBy = String.class, keyName = "work")
    double doLessHardWork(String s, int a);

    @Cache(identityBy = String.class, keyName = "list", listList = 2)
    List<Integer> doListWork(String s, int a);
}
