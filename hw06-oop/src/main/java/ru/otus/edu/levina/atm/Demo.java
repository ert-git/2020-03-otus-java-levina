package ru.otus.edu.levina.atm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.Nominal;
import ru.otus.edu.levina.atm.impl.ATMImpl;
import ru.otus.edu.levina.atm.impl.CellImpl;

public class Demo {

    public static void main(String[] args) {
        Nominal B100 = new Nominal(100);
        Nominal B500 = new Nominal(500);
        Nominal B200 = new Nominal(200);
        Nominal B1000 = new Nominal(1000);

        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(B100));
        cells.add(new CellImpl(B500));
        cells.add(new CellImpl(B1000));
        ATM a = new ATMImpl(cells);
        
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), 3);
        batch.put(new Banknote(B500), 2);
        batch.put(new Banknote(B200), 1);
        batch.put(new Banknote(B1000), 3);
        a.putMoney(batch);
        System.out.println("return: " + batch);
        System.out.println("total: " + a.getAvailableMoney());

        try {
            System.out.println(a.getMoney(1700));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("total: " + a.getAvailableMoney());

        try {
            System.out.println(a.getMoney(300));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("total: " + a.getAvailableMoney());
        
    }
}
