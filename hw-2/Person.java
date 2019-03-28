public class Person {
    public String name;
    private int age;
    private String hairColor;

    /*public Person(String name, int age, String hairColor) {
        this.name = name;
        this.age = age;
        this.hairColor = hairColor;
    }
    */

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public void setName(String name) {
        this.name = name;
    }
}
