package atm.internal;

import atm.MoneyCell;
import atm.Nominal;

import java.util.*;

class CounterImpl implements Counter {

    private static final StorageImpl storage = new StorageImpl();

    @Override
    public String processPackOfMoney(List<MoneyCell> pack) {
        long summ = 0;
        Map<Nominal, Integer> someMoney = new HashMap<>();
        for (var money : pack) {
            someMoney.put(money.getNominal(), money.getQnty());
            summ += money.getNominal().getValue() * money.getQnty();
        }
        storage.putIntoCells(someMoney);
        return "Внесено " + summ + " рублей:\n" + printDecomposition(someMoney);
    }

    @Override
    public String processRequestedSumm(int summ) {
        Map<Nominal, Integer> someMoney = calcSumm(summ);
        if (availableBalance() < summ || someMoney.size() == 0) return "Невозможно выдать запрошенную сумму " + summ + " рублей";
        else {
            storage.getFromCells(someMoney);
            return "Выдано " + summ + " рублей:\n" + printDecomposition(someMoney);
        }
    }

    @Override
    public String decomposeBalance() {
        var allMoney = storage.getBalance();
        return "Доступный баланс для снятия " + availableBalance() + " рублей:\n" + printDecomposition(allMoney);
    }

    private int availableBalance() {
        Map<Nominal, Integer> allMoney = storage.getBalance();
        int summ = 0;
        for (var entry : allMoney.entrySet()) {
            summ += entry.getKey().getValue() * entry.getValue();
        }
        return summ;
    }

    private Map<Nominal, Integer> calcSumm(int summ) {
        //подсчет выдачи нужной суммы доступными купюрами
        Map<Nominal, Integer> allMoney = storage.getBalance();
        Map<Nominal, Integer> someMoney = new HashMap<>();

        //алгоритм корректный лишь наполовину, но это не суть
        for (var nominal : new TreeSet<>(allMoney.keySet()).descendingSet()) {
            var nominalValue = nominal.getValue();
            var qnty = allMoney.get(nominal);
            if (nominalValue > summ || nominalValue == 0) {
                continue;
            }
            var cnt = Math.min(qnty, summ / nominalValue);
            someMoney.put(nominal, cnt);
            summ -= nominalValue * cnt;
            if (summ == 0) break;
        }
        if (summ != 0) someMoney.clear();
        return someMoney;
    }

    private String printDecomposition(Map<Nominal, Integer> someMoney) {
        String text = "";
        int qnty = 0;
        for (Nominal k : new TreeSet<>(someMoney.keySet())) {
            qnty = someMoney.get(k);
            if (qnty > 0)
                text += "Купюр номиналом " + k.getValue() + " рублей - " + qnty + "\n";
        }
        return text;
    }
}