import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */

    public static void assign(Object to, Object from) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?> toClass = to.getClass();
        Class<?> fromClass = from.getClass();

        List<Method> gettersList = getGettersList(fromClass);
        List<Method> settersList = getSettersList(toClass);

        HashMap<Method, Method> correspondence = correlateGettersAndSetters(gettersList, settersList);

        for (Map.Entry<Method, Method> methods : correspondence.entrySet()) {
            Method getter = methods.getKey();
            Method setter = methods.getValue();

            getter.setAccessible(true);
            setter.setAccessible(true);

            setter.invoke(to, getter.invoke(from));
        }
    }

    private static List<Method> getGettersList(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> gettersList = new ArrayList<>();

        for(Method method : methods) {
            if (isGetter(method)) {
                gettersList.add(method);
            }
        }
        return gettersList;
    }

    private static boolean isGetter(Method method){
        boolean methodIsGetter = false;
        if ((method.getName().startsWith("get")) &&
                (method.getParameterTypes().length == 0) &&
                !(void.class.equals(method.getReturnType()))) {
            methodIsGetter = true;
        }

        return methodIsGetter;
    }

    private static List<Method> getSettersList(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> settersList = new ArrayList<>();

        for(Method method : methods) {
            if (isSetter(method)) {
                settersList.add(method);
            }
        }
        return settersList;
    }

    private static boolean isSetter(Method method) {
        boolean methodIsSetter = false;
        if ((method.getName().startsWith("set")) &&
                (method.getParameterTypes().length == 1) &&
                (void.class.equals(method.getReturnType()))) {
            methodIsSetter = true;
        }

        return methodIsSetter;
    }

    /**
     * This function checks that the parameter type in setter should be the same or be superclass
     * of the return type of the getter.
     * @return returns the feasibility of this condition
     */
    private static boolean parameterCorrectness(Method getter, Method setter){
        boolean correctness = false;

        Class<?> getterParameter = getter.getReturnType();

        if ((getterParameter.equals(setter.getParameterTypes()[0])) ||
                (getterParameter.isAssignableFrom(setter.getParameterTypes()[0]))){

            correctness = true;
        }

        return correctness;
    }

    /**
     * This function for appropriate methods (getters and setters) finds their names.
     * @return returns the name of the method
     */
    private static String methodName(Method method){

        // name with the beginning "get" or "set"
        String fullname = method.getName();

        // pure name
        String name = fullname.substring(3);

        return name;
    }

    /**
     * This function correlates methods from getters and setters lists.
     */
    private static HashMap<Method, Method> correlateGettersAndSetters(List<Method> getters, List<Method> setters){

        HashMap<Method, Method> correspondence = new HashMap<>();
        for (Method getter : getters) {
            for (Method setter : setters) {
                if (methodName(getter).equals(methodName(setter)) &&
                        parameterCorrectness(getter, setter)) {
                    correspondence.put(getter, setter);
                }
            }
        }
        return correspondence;
    }
}
