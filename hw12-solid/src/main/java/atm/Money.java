package atm;

public interface Money {
    Nominal getNominal();
    int getQnty();
    void addSomeQnty(int qnty);
    void getSomeQnty(int qnty);
}
