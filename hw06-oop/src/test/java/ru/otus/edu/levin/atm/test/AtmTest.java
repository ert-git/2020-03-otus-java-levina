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
    private final static int CELL_CAPACITY = 10;
    private final static int INIT_B100 = 3;
    private final static int INIT_B500 = 4;
    private final static int INIT_B1000 = 2;

    private ATM atm;

    @Before
    public void before() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(B100, CELL_CAPACITY));
        cells.add(new CellImpl(B500, CELL_CAPACITY));
        cells.add(new CellImpl(B1000, CELL_CAPACITY));
        atm = new ATMImpl(cells);

        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), INIT_B100);
        batch.put(new Banknote(B500), INIT_B500);
        batch.put(new Banknote(B1000), INIT_B1000);
        atm.putMoney(batch);
    }

    @Test
    public void _00_test_put_no_rest() {
        int availableBefore = atm.getAvailableMoney();
        int cb100 = 3;
        int cb500 = 1;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), cb100);
        batch.put(new Banknote(B500), cb500);
        int sum = cb100 * B100.getValue() + cb500 * B500.getValue();

        atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(2, batch.size());
        Assert.assertEquals(0, batch.get(new Banknote(B100)).intValue());
        Assert.assertEquals(0, batch.get(new Banknote(B500)).intValue());
    }

    
    @Test
    public void _01_test_put_rest_by_nominal() {
        int availableBefore = atm.getAvailableMoney();
        int cb100 = 3;
        int cb500 = 1;
        int cb200 = 2;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), cb100);
        batch.put(new Banknote(B500), cb500);
        batch.put(new Banknote(B200), cb200);
        int sum = cb100 * B100.getValue() + cb500 * B500.getValue();

        atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(3, batch.size());
        Assert.assertEquals(0, batch.get(new Banknote(B100)).intValue());
        Assert.assertEquals(0, batch.get(new Banknote(B500)).intValue());
        Assert.assertNotNull(batch.get(new Banknote(B200)));
        Assert.assertEquals(cb200, batch.get(new Banknote(B200)).intValue());
    }

    @Test
    public void _01_test_put_rest_by_cell_capacity() {
        int availableBefore = atm.getAvailableMoney();
        int cb100 = 1;
        int cb500 = 11;
        int cb200 = 1;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(B100), cb100);
        batch.put(new Banknote(B500), cb500);
        batch.put(new Banknote(B200), cb200);
        int sum = cb100 * B100.getValue() + (CELL_CAPACITY - INIT_B500) * B500.getValue();

        atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(3, batch.size());
        Assert.assertNotNull(batch.get(new Banknote(B100)));
        Assert.assertNotNull(batch.get(new Banknote(B200)));
        Assert.assertNotNull(batch.get(new Banknote(B500)));
        Assert.assertEquals(0, batch.get(new Banknote(B100)).intValue());
        Assert.assertEquals(cb200, batch.get(new Banknote(B200)).intValue());
        Assert.assertEquals(cb500 - (CELL_CAPACITY - INIT_B500), batch.get(new Banknote(B500)).intValue());
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
    public void _04_test_get_failed_NO_SUIT_NOM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 2750;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NO_SUIT_NOM, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _05_test_get_failed_NOT_ENOUGHT_MONEY() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 10000;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NOT_ENOUGHT_MONEY, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _06_test_get_failed_NOT_ENOUGHT_NOM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 900;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NOT_ENOUGHT_NOM, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _07_test_get_failed_NEG_SUM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = -1;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertEquals(GetMoneyResponse.NEG_SUM, resp.getCode());
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

}
