package atm.internal;

import atm.Money;
import atm.Nominal;

import java.util.*;

class Counter {
    private static final Storage storage = new Storage();

    String processPackOfMoney(List<Money> pack) {
        int summ = 0;
        Map<Nominal, Integer> someMoney = new HashMap<>();
        for (Money money : pack) {
            var qnty = money.getQnty();
            var nominal = money.getNominal();
            someMoney.put(nominal, qnty);
            summ += nominal.getId() * qnty;
        }
        storage.putIntoCells(someMoney);
        return "Внесено " + summ + " рублей:\n" + printDecomposition(someMoney);
    }

    String processRequestedSumm(int summ) {
        Map<Nominal, Integer> someMoney = calcSumm(summ);
        if (someMoney.size() == 0) return "Невозможно выдать запрошенную сумму " + summ + " рублей";
        else {
            storage.getFromCells(someMoney);
        }
        return "Выдано " + summ + " рублей:\n" + printDecomposition(someMoney);
    }

    String printBalance() {
        var allMoney = storage.getBalance();
        return "Доступный баланс для снятия " + countBalance() + " рублей:\n" + printDecomposition(allMoney);
    }

    private int countBalance() {
        int summ = 0;
        Map<Nominal, Integer> allMoney = storage.getBalance();
        for (Nominal k : allMoney.keySet()) {
            summ += k.getId() * allMoney.get(k);
        }
        return summ;
    }

    private Map<Nominal, Integer> calcSumm(int summ) {
        //подсчет выдачи нужной суммы доступными купюрами, иначе - пустая мапа
        Map<Nominal, Integer> someMoney = new TreeMap<>();
        if (countBalance() < summ) return someMoney;

        Map<Nominal, Integer> allMoney = storage.getBalance();
        var keys = new TreeSet<>(allMoney.keySet());

        //алгоритм корректный лишь наполовину, но сказали, что это не суть
        for (var nominal : keys.descendingSet()) {
            var nominalId = nominal.getId();
            var qnty = allMoney.get(nominal);
            if (nominalId > summ || nominalId == 0) {
                continue;
            }
            var cnt = Math.min(qnty, summ / nominalId);
            someMoney.put(nominal, cnt);
            summ = summ - nominalId * cnt;
            if (summ == 0) break;
        }
        if (summ != 0) someMoney.clear();
        return someMoney;
    }

    private String printDecomposition(Map<Nominal, Integer> someMoney) {
        String text = "";
        int qnty = 0;
        var keys = new TreeSet<>(someMoney.keySet());
        for (Nominal k : keys) {
            qnty = someMoney.get(k);
            if (qnty > 0)
                text += "Купюр номиналом " + k.getId() + " рублей - " + qnty + "\n";
        }
        return text;
    }
}