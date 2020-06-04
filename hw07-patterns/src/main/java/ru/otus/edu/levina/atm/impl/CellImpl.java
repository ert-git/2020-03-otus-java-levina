package ru.otus.edu.levina.atm.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.ToString;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.Nominal;

@ToString
public class CellImpl implements Cell {
    private Cell nextCell;
    private final Nominal nominal;
    private final int capacity;

    private int available = 0;

    public CellImpl(Nominal nominal, int capacity) {
        this.nominal = nominal;
        this.capacity = capacity;
    }

    @Override
    public void setNext(Cell cell) {
        this.nextCell = cell;
    }

    @Override
    public Cell getNext() {
        return nextCell;
    }

    @Override
    public Map<Banknote, Integer> process(Map<Banknote, Integer> bns) {
        Map<Banknote, Integer> rest = new HashMap<>();
        rest.putAll(bns);
        // банкноты введены как ООП сущности согласно заданию, 
        // хотя здесь никакой полезной нагрузки не несут
        Banknote myBanknote = new Banknote(nominal);
        Integer wantPut = rest.get(myBanknote);
        if (wantPut == null || wantPut < 0) {
            System.out.println(
                    String.format("%s: Not valid banknote quntity: %s. Only positive is allowed.", this, wantPut));
            return rest;
        }
        int canPut = capacity - available;
        if (canPut <= 0) {
            System.out.println(String.format("%s: Cannot process banknotes: the cell is full.", this));
            return rest;
        }
        int restSum = wantPut > canPut ? wantPut - canPut : 0;
        if (restSum > 0) {
            System.out.println(
                    String.format("%s: Can process only %s banknotes: the cell is almost full.", this, canPut));
        }
        rest.put(myBanknote, restSum);
        available += Math.min(canPut, wantPut);
        // --------------- chain of responsibility
        if (nextCell != null) {
            rest = nextCell.process(rest);
        }
        return rest;
    }

    // ----------------------------------------------------------
    // previous home work implementation
    // ----------------------------------------------------------

    @Override
    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public int getAvailableBanknoteCount() {
        return available;
    }

    @Override
    public int getAvailableMoney() {
        return available * getNominal().getValue();
    }

    @Override
    public int provideMoney(int sum) {
        int nominal = this.nominal.getValue();
        int requieredBnCnt = (sum - sum % nominal) / nominal;
        int min = Math.min(available, requieredBnCnt);
        // Здесь бы следовало сначала зарезервировать деньги, а "выдать" только
        // после того, как транзакция будет утверждена.
        available -= min;
        return min;
    }

}
