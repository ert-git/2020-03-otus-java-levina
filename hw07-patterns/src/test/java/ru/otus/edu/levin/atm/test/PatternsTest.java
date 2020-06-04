package ru.otus.edu.levin.atm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.otus.edu.levina.atm.Department;
import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.Nominal;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;
import ru.otus.edu.levina.atm.impl.ATMImpl;
import ru.otus.edu.levina.atm.impl.CellImpl;
import ru.otus.edu.levina.atm.impl.cmd.GetRestCmdResponse;

public class PatternsTest {
    private final static int CELL_CAPACITY = 10;
    private final static int INIT_B100 = 3;
    private final static int INIT_B500 = 4;
    private final static int INIT_B1000 = 2;

    private Department department;

    @Before
    public void before() {
        List<ATM> atms = new ArrayList<>();
        atms.add(createAtm(1));
        atms.add(createAtm(2));
        atms.add(createAtm(3));
        department = new Department(atms);
        department.init();
    }

    private ATM createAtm(int id) {
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), INIT_B100);
        batch.put(new Banknote(Nominal.B500), INIT_B500);
        batch.put(new Banknote(Nominal.B1000), INIT_B1000);

        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(Nominal.B100, CELL_CAPACITY));
        cells.add(new CellImpl(Nominal.B500, CELL_CAPACITY));
        cells.add(new CellImpl(Nominal.B1000, CELL_CAPACITY));
        ATM atm = new ATMImpl(id, cells,  batch);

        return atm;
    }

    @Test
    public void _01_test_memento() {
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), INIT_B100);
        
        List<ATM> atmList = department.getAtmList();
        for (int i = 0; i < atmList.size(); i++) {
            ATM atm = atmList.get(i);
            atm.getMoney(100 * (i+1));
            atm.putMoney(batch);
        }
        for (int i = 0; i < atmList.size(); i++) {
            ATM atm = atmList.get(i);
            Assert.assertEquals(2, atm.getOperationsCount());
        }
        department.resetAll();
        for (int i = 0; i < atmList.size(); i++) {
            ATM atm = atmList.get(i);
            Assert.assertEquals(0, atm.getOperationsCount());
        }
    }
    
    @Test
    public void _02_test_observer() {
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), INIT_B100);
        
        List<ATM> atmList = department.getAtmList();
        int initAvailableMoney = 0; 
        for (int i = 0; i < atmList.size(); i++) {
            ATM atm = atmList.get(i);
            atm.getMoney(100 * (i+1));
            initAvailableMoney += atm.getAvailableMoney();
        }
        // 4200 + 4100 + 4000
        Assert.assertEquals(12300, initAvailableMoney);
        
        department.getAllRests();
        
        Set<CmdResponse> responses = department.getStore().getResponses();
        Assert.assertEquals(atmList.size(), responses.size());
        
        int totalMoney = 0;
        for (CmdResponse cmdResponse : responses) {
            totalMoney += ((GetRestCmdResponse) cmdResponse).getAvailableMoney();
        }
        Assert.assertEquals(initAvailableMoney, totalMoney);
    }
}
