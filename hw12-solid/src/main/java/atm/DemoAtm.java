package atm;

import atm.internal.AtmImpl;

import java.util.ArrayList;
import java.util.List;

import static atm.Nominal.*;

public class DemoAtm {

    public static void main(String[] args) {
        AtmImpl atm = AtmImpl.getInstance();

        MoneyCell m1 = new MoneyCell(n100, 2);
        MoneyCell m2 = new MoneyCell(n500, 4);
        List<MoneyCell> packOfMoney = new ArrayList<>();
        packOfMoney.add(m1);
        packOfMoney.add(m2);

        System.out.println("---------");

        atm.takeMoney(packOfMoney);
        atm.printAtmBalance();

        System.out.println("---------");

        atm.giveMoney(600);
        atm.printAtmBalance();

        System.out.println("---------");

        packOfMoney.clear();
        MoneyCell m3 = new MoneyCell(n500, 2);
        packOfMoney.add(m3);
        atm.takeMoney(packOfMoney);
        atm.printAtmBalance();

        System.out.println("---------");

        packOfMoney.clear();
        MoneyCell m4 = new MoneyCell(n2000, 3);
        MoneyCell m5 = new MoneyCell(n5000, 2);
        MoneyCell m6 = new MoneyCell(n100, 1);
        MoneyCell m7 = new MoneyCell(n1000, 1);
        packOfMoney.add(m4);
        packOfMoney.add(m5);
        packOfMoney.add(m6);
        packOfMoney.add(m7);
        atm.takeMoney(packOfMoney);
        atm.printAtmBalance();

        System.out.println("---------");

        atm.giveMoney(6400); //6400  15000 18500
        atm.printAtmBalance();

        System.out.println("---------");
    }
}
