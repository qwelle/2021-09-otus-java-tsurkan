package homework;

class Demo {
    public static void main(String[] args) {
        LogInterface logIntrfc = MyProxy.createLog();
        logIntrfc.calculation("строка");
        logIntrfc.calculation(6);
        logIntrfc.calculation(2, 3);
    }
}
