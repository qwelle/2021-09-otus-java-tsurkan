package atm;

public class MoneyCell implements Money {
    private final Nominal nominal;
    private int qnty;

    public MoneyCell(Nominal nominal) {
        this.nominal = nominal;
    }

    public MoneyCell(Nominal nominal, int qnty) {
        this.nominal = nominal;
        this.qnty = qnty;
    }

    @Override
    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public int getQnty() {
        return qnty;
    }

    @Override
    public void addSomeQnty(int qnty) {
        this.qnty += qnty;
    }

    @Override
    public void getSomeQnty(int qnty) {
        this.qnty -= qnty;
    }

}
