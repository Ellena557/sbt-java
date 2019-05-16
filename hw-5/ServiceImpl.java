public class ServiceImpl implements Service {
    @Override
    @Cache(identityBy = String.class)
    public double doHardWork(String s, int a) {
        System.out.println(s + " " + a);
        double r = a;
        return r;
    }
}
