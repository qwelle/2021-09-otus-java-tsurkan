package atm.internal;

import atm.MoneyCell;

import java.util.List;

public interface Counter {
    String processPackOfMoney(List<MoneyCell> pack);
    String processRequestedSumm(int summ);
    String decomposeBalance();
}
