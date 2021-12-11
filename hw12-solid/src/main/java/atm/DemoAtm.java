package atm;

import java.util.ArrayList;
import java.util.List;

public class DemoAtm {

    public static void main(String[] args) {
        Atm atm = Atm.getInstance();

        Money m1 = new Money(100, 2);
        Money m2 = new Money(500, 4);
        List<Money> packOfMoney = new ArrayList<>();
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
        Money m3 = new Money(500, 2);
        packOfMoney.add(m3);
        atm.takeMoney(packOfMoney);
        atm.printAtmBalance();

        System.out.println("---------");

        packOfMoney.clear();
        Money m4 = new Money(2000, 3);
        Money m5 = new Money(5000, 2);
        Money m6 = new Money(100, 1);
        Money m7 = new Money(1000, 1);
        packOfMoney.add(m4);
        packOfMoney.add(m5);
        packOfMoney.add(m6);
        packOfMoney.add(m7);
        atm.takeMoney(packOfMoney);
        atm.printAtmBalance();

        System.out.println("---------");

        atm.giveMoney(18500); //6400  15000
        atm.printAtmBalance();

        System.out.println("---------");
    }
}
