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
import ru.otus.edu.levina.atm.api.Nominal;
import ru.otus.edu.levina.atm.api.responses.GetMoneyResponse;
import ru.otus.edu.levina.atm.api.responses.NegSumResponse;
import ru.otus.edu.levina.atm.api.responses.NoSuitNominalResponse;
import ru.otus.edu.levina.atm.api.responses.NotEnoughMoneyResponse;
import ru.otus.edu.levina.atm.api.responses.NotEnoughNominalResponse;
import ru.otus.edu.levina.atm.api.responses.SuccessGetMoneyResponse;
import ru.otus.edu.levina.atm.impl.ATMImpl;
import ru.otus.edu.levina.atm.impl.CellImpl;

public class AtmManFuncTest {
    private final static int CELL_CAPACITY = 10;
    private final static int INIT_B100 = 3;
    private final static int INIT_B500 = 4;
    private final static int INIT_B1000 = 2;

    private ATM atm;

    @Before
    public void before() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new CellImpl(Nominal.B100, CELL_CAPACITY));
        cells.add(new CellImpl(Nominal.B500, CELL_CAPACITY));
        cells.add(new CellImpl(Nominal.B1000, CELL_CAPACITY));

        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), INIT_B100);
        batch.put(new Banknote(Nominal.B500), INIT_B500);
        batch.put(new Banknote(Nominal.B1000), INIT_B1000);

        atm = new ATMImpl(1, cells, batch);

    }

    @Test
    public void _00_test_put_no_rest() {
        int availableBefore = atm.getAvailableMoney();
        int cB100 = 3;
        int cB500 = 1;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), cB100);
        batch.put(new Banknote(Nominal.B500), cB500);
        int sum = cB100 * Nominal.B100.getValue() + cB500 * Nominal.B500.getValue();

        Map<Banknote, Integer> rest = atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(2, rest.size());
        Assert.assertEquals(0, rest.get(new Banknote(Nominal.B100)).intValue());
        Assert.assertEquals(0, rest.get(new Banknote(Nominal.B500)).intValue());
    }

    @Test
    public void _01_test_put_rest_by_nominal() {
        int availableBefore = atm.getAvailableMoney();
        int cB100 = 3;
        int cB500 = 1;
        int cB200 = 2;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), cB100);
        batch.put(new Banknote(Nominal.B500), cB500);
        batch.put(new Banknote(Nominal.B200), cB200);
        int sum = cB100 * Nominal.B100.getValue() + cB500 * Nominal.B500.getValue();

        Map<Banknote, Integer> rest = atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(3, rest.size());
        Assert.assertEquals(0, rest.get(new Banknote(Nominal.B100)).intValue());
        Assert.assertEquals(0, rest.get(new Banknote(Nominal.B500)).intValue());
        Assert.assertNotNull(rest.get(new Banknote(Nominal.B200)));
        Assert.assertEquals(cB200, rest.get(new Banknote(Nominal.B200)).intValue());
    }

    @Test
    public void _01_test_put_rest_by_cell_capacity() {
        int availableBefore = atm.getAvailableMoney();
        int cB100 = 1;
        int cB500 = 11;
        int cB200 = 1;
        Map<Banknote, Integer> batch = new HashMap<>();
        batch.put(new Banknote(Nominal.B100), cB100);
        batch.put(new Banknote(Nominal.B500), cB500);
        batch.put(new Banknote(Nominal.B200), cB200);
        int sum = cB100 * Nominal.B100.getValue() + (CELL_CAPACITY - INIT_B500) * Nominal.B500.getValue();

        Map<Banknote, Integer> rest = atm.putMoney(batch);
        Assert.assertEquals(availableBefore + sum, atm.getAvailableMoney());
        Assert.assertEquals(3, rest.size());
        Assert.assertNotNull(rest.get(new Banknote(Nominal.B100)));
        Assert.assertNotNull(rest.get(new Banknote(Nominal.B200)));
        Assert.assertNotNull(rest.get(new Banknote(Nominal.B500)));
        Assert.assertEquals(0, rest.get(new Banknote(Nominal.B100)).intValue());
        Assert.assertEquals(cB200, rest.get(new Banknote(Nominal.B200)).intValue());
        Assert.assertEquals(cB500 - (CELL_CAPACITY - INIT_B500), rest.get(new Banknote(Nominal.B500)).intValue());
    }

    @Test
    public void _02_test_get_success() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 2700;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertTrue(resp instanceof SuccessGetMoneyResponse);
        Map<Banknote, Integer> bns = resp.getBanknotes();
        Assert.assertEquals(3, bns.size());
        Assert.assertNotNull(bns.get(new Banknote(Nominal.B1000)));
        Assert.assertNotNull(bns.get(new Banknote(Nominal.B500)));
        Assert.assertNotNull(bns.get(new Banknote(Nominal.B100)));
        Assert.assertEquals(2, bns.get(new Banknote(Nominal.B1000)).intValue());
        Assert.assertEquals(1, bns.get(new Banknote(Nominal.B500)).intValue());
        Assert.assertEquals(2, bns.get(new Banknote(Nominal.B100)).intValue());
        Assert.assertEquals(availableBefore - sum, atm.getAvailableMoney());
    }

    @Test
    public void _04_test_get_failed_NO_SUIT_NOM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 2750;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertTrue(resp instanceof NoSuitNominalResponse);
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _05_test_get_failed_NOT_ENOUGHT_MONEY() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 10000;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertTrue(resp instanceof NotEnoughMoneyResponse);
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _06_test_get_failed_NOT_ENOUGHT_NOM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = 900;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertTrue(resp instanceof NotEnoughNominalResponse);
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

    @Test
    public void _07_test_get_failed_NEG_SUM() {
        int availableBefore = atm.getAvailableMoney();
        int sum = -1;
        GetMoneyResponse resp = atm.getMoney(sum);
        Assert.assertTrue(resp instanceof NegSumResponse);
        Assert.assertEquals(availableBefore, atm.getAvailableMoney());
    }

}
