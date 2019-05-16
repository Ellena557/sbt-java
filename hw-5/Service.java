public interface Service {
    @Cache(identityBy = String.class)
    double doHardWork(String s, int a);
}
