package atm;

import java.util.List;

class Atm {
    private static Atm instance = new Atm();
    private static CounterMoney counter = new CounterMoney();

    static Atm getInstance() {
        return instance;
    }

    void takeMoney(List<Money> packOfMoney) {
        System.out.println(counter.processPackOfMoney(packOfMoney));
    }

    void giveMoney(int summ) {
        System.out.println(counter.processRequestSumm(summ));
    }

    void printAtmBalance() {
        System.out.println(counter.printBalance());
    }
}
