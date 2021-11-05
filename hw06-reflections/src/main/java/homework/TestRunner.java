package homework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestRunner {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestRunner.run(DemoTest.class);
    }

    static void run(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<String> arrTests = new ArrayList<>();
        ArrayList<String> arrBefore = new ArrayList<>();
        ArrayList<String> arrAfter = new ArrayList<>();

        ArrayList<String> successArr = new ArrayList<>();
        ArrayList<String> failedArr = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();

        for(Method method : methods) {
            String methodName = method.getName();
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                switch (annotation.annotationType().getSimpleName()) {
                    case ("Before"):
                        arrBefore.add(methodName);
                        break;
                    case ("After"):
                        arrAfter.add(methodName);
                        break;
                    case ("Test"):
                        arrTests.add(methodName);
                        break;
                }
            }
        }

        for (String testMethod : arrTests) {
            System.out.println("------------------------------------");
            //для каждого класса свой объект
            Object testClass = clazz.getDeclaredConstructor().newInstance();

            //вызываем методы Before
            for (String beforeMethod : arrBefore) {
                Method method = clazz.getMethod(beforeMethod);
                method.invoke(testClass);
            }
            //запуск теста
            try {
                Method method = clazz.getMethod(testMethod);
                method.invoke(testClass);
                successArr.add(testMethod);
            } catch (Exception e) {
                failedArr.add(testMethod);
                e.printStackTrace();
            } finally {
                //методы After
                for (String afterMethod : arrAfter) {
                    try {
                        Method method = clazz.getMethod(afterMethod);
                        method.invoke(testClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.printf("\nУспешно: %d\nУпало: %d\nВсего: %d", successArr.size(), failedArr.size(), arrTests.size());
    }
}
