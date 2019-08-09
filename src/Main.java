import java.lang.annotation.*;
import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) {
        Human t1 = new Human();
        t1.setAge(1000);
        t1.setName("Tony");
        System.out.println("before\t" + t1.getAge());
        System.out.println("before\t" + t1.getName());
        try {
            Human t = new Human(t1);
            System.out.println("after\t" + t1.getAge());
            System.out.println("after\t" + t1.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}


class Human {
    @Initialize(name = "Antony")
    private String name;

    @Initialize(age = 99999)
    private int age;


    Human() {
    }

    Human(String name, int age) {
        this.age = age;
        this.name = name;
    }

    Human(Object object) throws IllegalAccessException {
        Class<?> cl = object.getClass();
        Field[] fields = cl.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getName().equals("Initialize")) {
                    if (f.getName().equals("age")){
                        f.set(object, f.getAnnotation(Initialize.class).age());
                    }
                    if (f.getName().equals("name")){
                        f.set(object, f.getAnnotation(Initialize.class).name());
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@interface Initialize {
    int age() default 0;
    String name() default "";
}