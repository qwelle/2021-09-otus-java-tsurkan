package atm;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class CounterMoney {
    private static Map<Integer, Integer> storedMoney = new TreeMap<>(); //k:nominal, v:qnty

    String processPackOfMoney(List<Money> pack) {
        int summ = 0;
        Map<Integer, Integer> someMoney = new TreeMap<>();
        for (Money money : pack) {
            var qnty = money.getQnty();
            var nominal = money.getNominal();
            storedMoney.merge(nominal, qnty, (k, v) -> v + qnty);
            summ += nominal * qnty;
            someMoney.put(nominal, qnty);
        }
        return "Внесено " + summ + " рублей:\n" + printDecomposition(someMoney);
    }

    String processRequestSumm(int summ) {
        Map<Integer, Integer> someMoney = calcSumm(summ);
        if (someMoney.size() == 0) return "Невозможно выдать запрошенную сумму " + summ + " рублей";
        else {
            for (Integer key : someMoney.keySet()) {
                storedMoney.computeIfPresent(key, (k, v) -> v - someMoney.get(key));
            }
            return "Выдано " + summ + " рублей:\n" + printDecomposition(someMoney);
        }
    }

    String printBalance() {
        return "Доступный баланс для снятия " + countBalance() + " рублей:\n" + printDecomposition(storedMoney);
    }

    private int countBalance() {
        int summ = 0;
        for (Integer k : storedMoney.keySet()) {
            summ += k * storedMoney.get(k);
        }
        return summ;
    }

    private Map<Integer, Integer> calcSumm(int summ) {
        //подсчет выдачи нужной суммы доступными купюрами, иначе - пустая мапа
        Map<Integer, Integer> someMoney = new TreeMap<>();
        if (countBalance() < summ) return someMoney;

        //алгоритм корректный лишь наполовину, но сказали, что это не суть
        for (var k : ((TreeMap) storedMoney).descendingKeySet()) {
            var nominal = (Integer) k;
            var qnty = storedMoney.get(nominal);
            if (nominal > summ || qnty == 0) {
                continue;
            }
            var cnt = Math.min(qnty, summ / nominal);
            someMoney.put(nominal, cnt);
            summ = summ - nominal * cnt;
            if (summ == 0) break;
        }
        if (summ != 0) someMoney.clear();
        return someMoney;
    }

    private String printDecomposition(Map<Integer, Integer> someMoney) {
        String text = "";
        int qnty = 0;
        for (Integer k : someMoney.keySet()) {
            qnty = someMoney.get(k);
            if (qnty > 0)
                text += "Купюр номиналом " + k + " рублей - " + someMoney.get(k) + "\n";
        }
        return text;
    }
}