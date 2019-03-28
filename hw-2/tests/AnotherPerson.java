public class AnotherPerson {
    public String name;
    private int age;
    private String profession;
    private boolean gender;
    /* gender = true if person is a man*/

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public boolean isGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
