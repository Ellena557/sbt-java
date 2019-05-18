import java.lang.annotation.*;

@Documented
//Аннотация может быть использована во время выполнения через Reflection
@Retention(RetentionPolicy.RUNTIME)
//Аннотация  направлена именно на метод (не класс, не переменная, не поле, а именно метод)
@Target(ElementType.METHOD)
public @interface Cache {
    CacheType cacheType() default CacheType.IN_MEMORY;
    boolean zip() default false;
    Class[] identityBy() default Class.class;
    String keyName() default "";
    int listList() default Integer.MAX_VALUE;
}
