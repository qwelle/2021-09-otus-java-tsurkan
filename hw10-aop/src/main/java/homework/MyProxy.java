package homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class MyProxy {

    static LogInterface createLog() {
        InvocationHandler handler = new LogInvocationHandler(new LogImpl());
        return (LogInterface) Proxy.newProxyInstance(MyProxy.class.getClassLoader(), new Class<?>[]{LogInterface.class}, handler);
    }

    static class LogInvocationHandler implements InvocationHandler {
        private final LogInterface logIntrfc;

        LogInvocationHandler(LogInterface logIntrfc) {
            this.logIntrfc = logIntrfc;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodInterface = method.toString();
            String methodInterfaceShort = methodInterface.substring(methodInterface.lastIndexOf('.') + 1);

            Class<?> clazz = LogImpl.class;
            Method[] methods = clazz.getDeclaredMethods();

            for(Method m : methods) {
                String methodClass = m.toString();
                String methodClassShort = methodClass.substring(methodClass.lastIndexOf('.') + 1);

                //сравниваем метод интерфейса с методами класса по текстовой сигнатуре
                if (methodClassShort.equals(methodInterfaceShort)) {
                    //если у метода класса есть аннотация Log, то
                    String params = "";
                    if (m.isAnnotationPresent(Log.class)) {
                        for (int i = 0; i < args.length; i++) {
                            params = params.concat(args[i] + " ");
                        }
                        System.out.println("executed method: " + method.getName() + ", param: " + params);
                    }
                }
            }
            //для всех методов
            return method.invoke(logIntrfc, args);
        }
    }
}
