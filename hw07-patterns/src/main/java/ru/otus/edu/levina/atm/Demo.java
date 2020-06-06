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
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), 3);
        batch.put(new Banknote(Nominal.B500), 2);
        batch.put(new Banknote(Nominal.B200), 1);
        batch.put(new Banknote(Nominal.B1000), 3);

        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(Nominal.B100, 10, 3));
        cells.add(new CellImpl(Nominal.B500, 10, 2));
        cells.add(new CellImpl(Nominal.B1000, 10, 3));
        ATM a = new ATMImpl(1, cells);

        Map<Banknote, Integer> rest = a.putMoney(batch);
        System.out.println("PUT: rest is: " + rest);
        System.out.println("ATM available: " + a.getAvailableMoney());

        System.out.println("GET: " + a.getMoney(1700));
        System.out.println("ATM available: " + a.getAvailableMoney());

        System.out.println("GET: " + a.getMoney(300));
        System.out.println("ATM available: " + a.getAvailableMoney());

    }
}
