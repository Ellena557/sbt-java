public class ServiceTest {
    public static void main(String[] args) {
        ServiceImpl service = new ServiceImpl();
        //run(service);

        Service service1 = (Service) CacheProxy.cacheObject(service);

        double r1 = service1.doHardWork("work1", 10); //считает результат
        double r2 = service1.doHardWork("work1", 5);
    }

    static void run(ServiceImpl service) {
        double r1 = service.doHardWork("work1", 10); //считает результат
        double r2 = service.doHardWork("work2", 5);  //считает результат
        double r3 = service.doHardWork("work1", 10); //результат из кеша
    }



}
