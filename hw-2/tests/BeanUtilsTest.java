import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class BeanUtilsTest {

    @Test
    public void beanUtilsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Person alex = new Person();
        AnotherPerson dean = new AnotherPerson();

        alex.setAge(18);
        alex.setName("Alex");
        alex.setHairColor("blond");
        alex.setGender("male");

        dean.setName("Dean");
        dean.setAge(10);
        dean.setProfession("pupil");
        dean.setGender(true);

        BeanUtils.assign(alex, dean);

        assertEquals(alex.getName(), "Dean");
        assertEquals(alex.getAge(), 10);
        assertEquals(alex.getHairColor(), "blond");
        assertEquals(alex.getGender(), "male");
    }
}
