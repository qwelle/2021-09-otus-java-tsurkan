package homework;

class LogImpl implements LogInterface {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("calculation(int)): " + param);
    };

    @Override
    public void calculation(String param) {
        System.out.println("calculation(String): " + param);
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("calculation(int,int): " + param1 + ", " + param2);
    }
}
