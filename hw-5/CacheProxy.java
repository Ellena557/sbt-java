import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CacheProxy implements InvocationHandler, Serializable {

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
        //System.out.println(method);
        List params = Arrays.asList(args);

        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        Class[] identities = cacheAnnotation.identityBy();
        //System.out.println(identities);
        String nameOfKey = cacheAnnotation.keyName();
        CacheType cachType = cacheAnnotation.cacheType();
        boolean zip = cacheAnnotation.zip();

        if (nameOfKey == ""){
            nameOfKey = method.getName();
        }

        List mentionedParams = new ArrayList();

        for (Object param : params) {
            boolean isMentioned = false;
            //System.out.println("OOO" + param);

            for (int i = 0; i < identities.length; i++) {
                if (identities[i] == param.getClass()) {
                    isMentioned = true;
                    //System.out.println(identities[i] + " III");
                }
            }

            if (isMentioned){
                mentionedParams.add(param);
            }
        }

        String fileName = nameOfKey + ".txt";
        String zipFileName = nameOfKey + ".zip";
        Object vals = null;
        Map cache = null;

        //Class[] defaultClass = new Class[1];
        //defaultClass[0] = Class.class;

        // if we have default identities
        if (identities[0] == Class.class) {
            mentionedParams = params;
        }

        if (cachType == CacheType.IN_MEMORY) {
            cache = getCacheValue(nameOfKey);
            vals = cache.get(mentionedParams);

        } else {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                cache = (HashMap<Object,Object>)ois.readObject();

                vals = cache.get(mentionedParams);
                ois.close();
                /*String mentionedParamsStr = "";
                for (Object param : mentionedParams) {

                }
                ArrayList<ArrayList<Object>> cache = (ArrayList<ArrayList<Object>>) ois.readObject();
                for (ArrayList<Object> obj : cache) {
                    if
                }*/
            } catch (FileNotFoundException e) {
                File file = new File(fileName);
                cache = new HashMap();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        //System.out.println(mentionedParams + "PPPPPP");

        //System.out.println(vals.getClass());


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
            //System.out.println(method.getReturnType());

            if (method.getReturnType() == List.class) {
                //System.out.println("LIST CASE");
                List valsList = (List) method.invoke(object, args);
                //System.out.println("okay");
                int maxListSize = cacheAnnotation.listList();
                if (valsList.size() > maxListSize) {
                    vals = valsList.subList(0, maxListSize);
                }

            }

            cache.put(mentionedParams, vals);

            if (cachType == CacheType.FILE) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                    FileInputStream fis = new FileInputStream(fileName);
                    oos.writeObject(cache);
                    oos.flush();

                    if (zip) {
                        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
                        ZipEntry entry = new ZipEntry(fileName);
                        zos.putNextEntry(entry);
                        byte[] byteCode = new byte[fis.available()];
                        fis.read(byteCode);
                        zos.write(byteCode);
                        zos.closeEntry();
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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


    private Map getCacheValue(String method){

        Map cache = (Map) cacheMap.get(method);

        if (cache == null) {
            cache = new HashMap();
            cacheMap.put(method, cache);
        }

        return cache;
    }
}
