import java.util.List;

public interface Service {
    @Cache(identityBy = String.class)
    double doHardWork(String s, int a);

    @Cache(identityBy = String.class, keyName = "work")
    double doLessHardWork(String s, int a);

    @Cache(identityBy = String.class, keyName = "list", listList = 2)
    List<Integer> doListWork(String s, int a);

    @Cache(identityBy = String.class, keyName = "file", cacheType = CacheType.FILE)
    double doFileWork(String s, int a);

    @Cache(identityBy = String.class, keyName = "zip", cacheType = CacheType.FILE, zip = true)
    double doZipWork(String s, int a);

    @Cache()
    double doParameterWork(String s, int a);

    @Cache(identityBy = {String.class, Integer.class}, keyName = "extra name", cacheType = CacheType.FILE, zip = true)
    double doExtraWork(String s, int a);
}
