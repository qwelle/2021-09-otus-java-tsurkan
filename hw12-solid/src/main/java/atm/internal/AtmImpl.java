package atm.internal;

import atm.MoneyCell;

import java.util.List;

public class AtmImpl implements Atm {
    private static final CounterImpl counter = new CounterImpl();

    public static AtmImpl getInstance() {
        return AtmHolder.instance;
    }

    private static class AtmHolder {
        static final AtmImpl instance = new AtmImpl();
    }

    @Override
    public void takeMoney(List<MoneyCell> packOfMoney) {
        System.out.println(counter.processPackOfMoney(packOfMoney));
    }

    @Override
    public void giveMoney(int summ) {
        System.out.println(counter.processRequestedSumm(summ));
    }

    @Override
    public void printAtmBalance() {
        System.out.println(counter.decomposeBalance());
    }
}
