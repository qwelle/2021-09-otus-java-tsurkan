package atm.internal;

import atm.Nominal;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

class Storage {
    private static Map<Nominal, Integer> storedMoney;

    Storage() {
        storedMoney = new TreeMap<>();
    }

    void putIntoCells(Map<Nominal, Integer> someMoney) {
        for (Nominal nominal : someMoney.keySet()) {
            Integer qnty = someMoney.get(nominal);
            storedMoney.merge(nominal, qnty, (oldV, v) -> oldV + qnty);
        }
    }

    void getFromCells(Map<Nominal, Integer> someMoney) {
        for (Nominal nominal : someMoney.keySet()) {
            var qnty = someMoney.get(nominal);
            storedMoney.computeIfPresent(nominal, (k, v) -> v - qnty);
        }
    }

    Map<Nominal, Integer> getBalance() {
        return Collections.unmodifiableMap(storedMoney);
    }
}
