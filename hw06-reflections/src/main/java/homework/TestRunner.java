package homework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List arrTests = new ArrayList<>();
        List arrBefore = new ArrayList<>();
        List arrAfter = new ArrayList<>();
        List arrSuccess = new ArrayList<>();
        List arrFailed = new ArrayList<>();

        Class clazz = DemoTest.class;

        prepareData(clazz, arrBefore, arrAfter, arrTests);
        runTests(clazz, arrBefore, arrAfter, arrTests, arrSuccess, arrFailed);
        printResult(arrSuccess.size(), arrFailed.size(), arrTests.size());
    }

    private static void prepareData(Class<?> clazz, List arrBefore, List arrAfter, List arrTests) {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            String methodName = method.getName();
            if (method.isAnnotationPresent(Before.class))
                arrBefore.add(methodName);
            else if (method.isAnnotationPresent(After.class))
                arrAfter.add(methodName);
            else if (method.isAnnotationPresent(Test.class))
                arrTests.add(methodName);
        }
    }

    private static void runTests(Class<?> clazz, List arrBefore, List arrAfter, List arrTests, List arrSuccess, List arrFailed) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Object testMethod : arrTests) {
            System.out.println("------------------------------------");
            //для каждого теста свой класс
            Object testClass = clazz.getDeclaredConstructor().newInstance();

            //методы Before
            for (Object beforeMethod : arrBefore) {
                Method method = clazz.getMethod(beforeMethod.toString());
                method.invoke(testClass);
            }
            //запуск теста
            try {
                Method method = clazz.getMethod(testMethod.toString());
                method.invoke(testClass);
                arrSuccess.add(testMethod);
            } catch (Exception e) {
                arrFailed.add(testMethod);
                e.printStackTrace();
            } finally {
                //методы After
                for (Object afterMethod : arrAfter) {
                    try {
                        Method method = clazz.getMethod(afterMethod.toString());
                        method.invoke(testClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void printResult(int sizeArrSuccess, int sizeArrFailed, int sizeArrTests) {
        System.out.printf("\nУспешно: %d\nУпало: %d\nВсего: %d", sizeArrSuccess, sizeArrFailed, sizeArrTests);
    }
}
