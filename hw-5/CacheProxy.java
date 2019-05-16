import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class CacheProxy implements InvocationHandler {

    private Map cacheMap;
    private Object[] args;
    private Object object;

    /*private Method method;
    public CacheProxy(Method method, Object[] args) {
        this.cacheMap = new HashMap();
        this.method = method;
        this.args = args;
    } */

    private CacheProxy(Object args) {
        this.cacheMap = new HashMap();
        this.object = args;
        //this.args = args;
    }

    static Object cacheObject(Object o) {
        Class clazz = o.getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        Class[] interfaces = clazz.getInterfaces();

        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, new CacheProxy(o));

        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            return invokeMethod(method, args);
        } else {
            return method.invoke(object, args);
        }

    }

    private Object invokeMethod(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Map cache = getCacheValue(method);
        List params = Arrays.asList(args);

        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        Class[] identities = cacheAnnotation.identityBy();

        List mentionedParams = new ArrayList();

        for (Object param : params) {
            boolean isMentioned = false;
            //System.out.println("OOO" + param);

            for (int i = 0; i < identities.length; i++) {
                if (identities[i] == param.getClass()) {
                    isMentioned = true;
                }
            }

            if (isMentioned){
                mentionedParams.add(param);
            }
        }

        //System.out.println(mentionedParams + "PPPPPP");


        Object vals = cache.get(mentionedParams);

        //System.out.println("keys : " + cache.keySet());
        //System.out.println("pars : " + mentionedParams);

        //boolean cacheContainsKey = false;

        /*for (Object object : cache.keySet()) {
            if (object == mentionedParams){
                cacheContainsKey = true;
            }
        }
        */

        //System.out.println(cache.containsKey(mentionedParams));
        //System.out.println("CONTAINTS + " + cacheContainsKey);

        if (vals == null || !cache.containsKey(mentionedParams)) {
            //System.out.println("RES : " + (vals == null || !cache.containsKey(mentionedParams)));
            System.out.println("Counting new value");
            vals = method.invoke(object, args);
            cache.put(mentionedParams, vals);
            //for (Object param : mentionedParams){
              //  System.out.println(param.getClass());
            //}
            //System.out.println(cache.keySet());
            //System.out.println(vals);
        } else {
            System.out.println("Take result from cache");
        }

        /*for (Object objects : cacheMap.entrySet()) {
            System.out.println(objects);
        }*/
        return vals;
    }



    private Map getCacheValue(Method method){

        Map cache = (Map) cacheMap.get(method);

        if (cache == null) {
            cache = new HashMap();
            cacheMap.put(method, cache);
        }

        return cache;
    }
}
