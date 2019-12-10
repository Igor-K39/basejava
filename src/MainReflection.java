import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws Exception {
        Resume resume = new Resume();
        Field field = resume.getClass().getDeclaredFields()[0];
        Method method = resume.getClass().getMethod("toString");

        field.setAccessible(true);
        field.set(resume, "На этом собеседование окончено!");
        System.out.println(method.invoke(resume));
    }
}
