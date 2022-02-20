package atm.internal;

import atm.Nominal;

import java.util.Map;

public interface Storage {
    void putIntoCells(Map<Nominal, Integer> someMoney);
    void getFromCells(Map<Nominal, Integer> someMoney);
    Map<Nominal, Integer> getBalance();
}
