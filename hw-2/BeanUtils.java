import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
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

        HashMap<Method, Method> correspondence = corellateGettersAndSetters(gettersList, settersList);

        for (Map.Entry<Method, Method> methods : correspondence.entrySet()) {
            Method getter = methods.getKey();
            Method setter = methods.getValue();

            // to do: возможно, в другом месте это лучше переписать
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
        if ( (method.getName().startsWith("get")) &&
                (method.getParameterTypes().length == 0)) {
            // добавить в if про возвращаемое значение -- должно совпадать с нужным
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
                (method.getParameterTypes().length == 1)) {
            // возможно, в if про возвращаемое значение добавить
            methodIsSetter = true;
        }

        return methodIsSetter;
    }

    private static boolean parameterCorrectness(Method getter, Method setter){

        /**
         * The parameter type in setter should be the same or be superclass
         * of the return type of the getter.
         */

        boolean correctness = false;

        TypeVariable<Method>[] setterParameters = setter.getTypeParameters();

        // correct setters have only one parameter
        //TypeVariable<Method> setterParameter = setterParameters[0];

        Class<?> getterParameter = getter.getReturnType();

        if ((getterParameter.equals(setter.getParameterTypes()[0])) ||
                (getterParameter.isAssignableFrom(setter.getParameterTypes()[0]))){

            correctness = true;
        }

        return correctness;
    }

    private static String methodName(Method method){
        /**
         * For appropriate methods (getters and setters) this method returns their names.
         */

        // name with the beginning "get" or "set"
        String fullname = method.getName();

        // pure name
        String name = fullname.substring(3);

        return name;
    }

    private static HashMap<Method, Method> corellateGettersAndSetters(List<Method> getters, List<Method> setters){
        HashMap<Method, Method> correspondence = new HashMap<>();
        for ( Method getter : getters) {
            for ( Method setter : setters) {
                if (methodName(getter).equals(methodName(setter)) &&
                        parameterCorrectness(getter, setter)) {
                    correspondence.put(getter, setter);
                }
            }
        }
        return correspondence;
    }
}
