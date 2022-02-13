package atm;

public enum Nominal {
    n100(100),
    n200(200),
    n500(500),
    n1000(1000),
    n2000(2000),
    n5000(5000);

    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
