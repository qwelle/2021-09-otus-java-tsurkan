package atm.internal;

import atm.Money;

import java.util.List;

public class Atm {
    private static Atm instance;
    private static final Counter counter = new Counter();

    public synchronized static Atm getInstance() {
        if (instance == null) instance = new Atm();
        return instance;
    }

    public void takeMoney(List<Money> packOfMoney) {
        System.out.println(counter.processPackOfMoney(packOfMoney));
    }

    public void giveMoney(int summ) {
        System.out.println(counter.processRequestedSumm(summ));
    }

    public void printAtmBalance() {
        System.out.println(counter.printBalance());
    }
}
