package atm.internal;

import atm.MoneyCell;

import java.util.List;

public interface Atm {
    void takeMoney(List<MoneyCell> packOfMoney);
    void giveMoney(int summ);
    void printAtmBalance();
}
