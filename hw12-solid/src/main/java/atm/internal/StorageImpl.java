package atm.internal;

import atm.MoneyCell;
import atm.Nominal;

import java.util.*;
import java.util.stream.Collectors;

class StorageImpl implements Storage {

    private static Set<MoneyCell> storedMoney;

    StorageImpl() {
        storedMoney = Arrays.stream(Nominal.values()).map(MoneyCell::new)
                .collect(Collectors.toCollection(() -> new HashSet<>()));
    }

    @Override
    public void putIntoCells(Map<Nominal, Integer> someMoney) {
        for (var entry : someMoney.entrySet()) {
            storedMoney.stream().filter(moneyCell -> moneyCell.getNominal() == entry.getKey()).forEach(moneyCell -> moneyCell.addSomeQnty(entry.getValue()));
        }
    }

    @Override
    public void getFromCells(Map<Nominal, Integer> someMoney) {
        for (var entry : someMoney.entrySet()) {
            storedMoney.stream().filter(moneyCell -> moneyCell.getNominal() == entry.getKey()).forEach(moneyCell -> moneyCell.getSomeQnty(entry.getValue()));
        }
    }

    @Override
    public Map<Nominal, Integer> getBalance() {
        Map<Nominal, Integer> money = new HashMap<>();
        storedMoney.stream().filter(moneyCell -> moneyCell.getQnty() > 0).forEach(moneyCell -> money.put(moneyCell.getNominal(), moneyCell.getQnty()));
        return Collections.unmodifiableMap(money);
    }
}
