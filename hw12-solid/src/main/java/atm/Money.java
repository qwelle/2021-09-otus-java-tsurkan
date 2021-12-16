package atm;

public class Money {
    private final Nominal nominal;
    private final Integer qnty;

    public Money(Nominal nominal, Integer qnty) {
        this.nominal = nominal;
        this.qnty = qnty;
    }

    public Integer getQnty() {
        return this.qnty;
    }

    public Nominal getNominal() {
        return nominal;
    }
}
