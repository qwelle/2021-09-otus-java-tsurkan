package homework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class Tests {
    List<String> arrTests = new ArrayList<>();
    List<String> arrBefore = new ArrayList<>();
    List<String> arrAfter = new ArrayList<>();
}

class TestsResults {
    List<String> arrSuccess = new ArrayList<>();
    List<String> arrFailed = new ArrayList<>();
}

public class TestRunner {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = DemoTest.class;

        TestRunner runner = new TestRunner();
        Tests testArrays = new Tests();
        TestsResults resultArrays = new TestsResults();

        runner.prepareData(clazz, testArrays);
        runner.runTests(clazz, testArrays, resultArrays);
        runner.printResult(testArrays, resultArrays);
    }

    private void prepareData(Class<?> clazz, Tests testArrays) {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            String methodName = method.getName();
            if (method.isAnnotationPresent(Before.class))
                testArrays.arrBefore.add(methodName);
            else if (method.isAnnotationPresent(After.class))
                testArrays.arrAfter.add(methodName);
            else if (method.isAnnotationPresent(Test.class))
                testArrays.arrTests.add(methodName);
        }
    }

    private void runTests(Class<?> clazz, Tests testArrays, TestsResults resultArrays) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (String testMethod : testArrays.arrTests) {
            System.out.println("------------------------------------");
            //для каждого теста свой класс
            Object testClass = clazz.getDeclaredConstructor().newInstance();

            //методы Before
            for (String beforeMethod : testArrays.arrBefore) {
                Method method = clazz.getMethod(beforeMethod);
                method.invoke(testClass);
            }
            //запуск теста
            try {
                Method method = clazz.getMethod(testMethod);
                method.invoke(testClass);
                resultArrays.arrSuccess.add(testMethod);
            } catch (Exception e) {
                resultArrays.arrFailed.add(testMethod);
                e.printStackTrace();
            } finally {
                //методы After
                for (String afterMethod : testArrays.arrAfter) {
                    try {
                        Method method = clazz.getMethod(afterMethod);
                        method.invoke(testClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void printResult(Tests testArrays, TestsResults resultArrays) {
        System.out.printf("\nУспешно: %d\nУпало: %d\nВсего: %d", resultArrays.arrSuccess.size(), resultArrays.arrFailed.size(), testArrays.arrTests.size());
    }
}
