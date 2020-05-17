package ru.otus.edu.levin.atm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.GetMoneyResponse;
import ru.otus.edu.levina.atm.api.Nominal;
import ru.otus.edu.levina.atm.impl.ATMImpl;
import ru.otus.edu.levina.atm.impl.CellImpl;

public class AtmTest {
    private final static Nominal B100 = new Nominal(100);
    private final static Nominal B200 = new Nominal(200);
    private final static Nominal B500 = new Nominal(500);
    private final static Nominal B1000 = new Nominal(1000);

    private ATM atm;

    @Before
    public void before() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(B100));
        cells.add(new CellImpl(B500));
        cells.add(new CellImpl(B1000));
        atm = new ATMImpl(cells);

        int b100 = 3;
        int b500 = 4;
        int b1000 = 2;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), b100);
        batch.put(new Banknote(B500), b500);
        batch.put(new Banknote(B1000), b1000);
        atm.putMoney(batch);
    }

    @Test
    public void _01_test_put_with_rest() {
        int availableBefore = atm.getAvailableMoney();
        int b100 = 3;
        int b500 = 1;
        int b200 = 2;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), b100);
        batch.put(new Banknote(B500), b500);
        batch.put(new Banknote(B200), b200);
        int sum = b100*B100.getValue()  + b500*B500.getValue();
        
        atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(1, batch.size());
        Assert.assertNotNull(batch.get(new Banknote(B200)));
        Assert.assertEquals(b200, batch.get(new Banknote(B200)).intValue());
    }

    @Test
    public void _02_test_get_success() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 2700;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.SUCCESS, resp.getCode());
        Map<Banknote, Integer> bns = resp.getBanknotes();
        Assert.assertEquals(3, bns.size());
        Assert.assertNotNull(bns.get(new Banknote(B1000)));
        Assert.assertNotNull(bns.get(new Banknote(B500)));
        Assert.assertNotNull(bns.get(new Banknote(B100)));
        Assert.assertEquals(2, bns.get(new Banknote(B1000)).intValue());
        Assert.assertEquals(1, bns.get(new Banknote(B500)).intValue());
        Assert.assertEquals(2, bns.get(new Banknote(B100)).intValue());
        Assert.assertEquals(availableBefore - sum, atm.getAvailableMoney());
    }

    @Test
    public void _03_test_get_failed_NO_SUIT_NOM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 2750;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NO_SUIT_NOM, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _03_test_get_failed_NOT_ENOUGHT_MONEY() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 10000;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NOT_ENOUGHT_MONEY, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _03_test_get_failed_NOT_ENOUGHT_NOM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 900;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NOT_ENOUGHT_NOM, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _03_test_get_failed_NEG_SUM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = -1;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NEG_SUM, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

}
