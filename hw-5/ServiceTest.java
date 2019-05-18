import java.util.List;

public class ServiceTest {
    public static void main(String[] args) {
        ServiceImpl service = new ServiceImpl();
        //run(service);

        Service service1 = (Service) CacheProxy.cacheObject(service);

        System.out.println("First test : choosing parameters");

        double r1 = service1.doHardWork("work1", 10);
        double r2 = service1.doHardWork("work1", 5);

        System.out.println();
        System.out.println("Next test : keyName");

        double r3 = service1.doLessHardWork("work1", 10);
        double r4 = service1.doLessHardWork("work1", 5);

        System.out.println();
        System.out.println("Next test : List");

        List<Integer> r5 = service1.doListWork("list1", 10);
        List<Integer> r6 = service1.doListWork("list1", 5);
        System.out.println(r5.size() + " " + r6.size());

        System.out.println();
        System.out.println("Next test : File");

        double r7 = service1.doFileWork("work1", 10);
        double r8 = service1.doFileWork("work1", 5);

        System.out.println();
        System.out.println("Next test : Zip");

        double r9 = service1.doZipWork("work1", 10);
        double r10 = service1.doZipWork("work1", 5);

        System.out.println();
        System.out.println("Next test : Default parameters");

        double r11 = service1.doParameterWork("work1", 10);
        double r12 = service1.doParameterWork("work1", 5);
    }

    static void run(ServiceImpl service) {
        double r1 = service.doHardWork("work1", 10); //считает результат
        double r2 = service.doHardWork("work2", 5);  //считает результат
        double r3 = service.doHardWork("work1", 10); //результат из кеша
    }
    
}
