package ru.otus.edu.levina.atm.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.ToString;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.Nominal;

@ToString
public class CellImpl implements Cell {
    private final Nominal nominal;

    private AtomicInteger available = new AtomicInteger();

    public CellImpl(Nominal nominal) {
        this.nominal = nominal;
    }

    @Override
    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public int getAvailableBanknoteCount() {
        return available.get();
    }

    @Override
    public int getAvailableMoney() {
        return available.get() * getNominal().getValue();
    }

    @Override
    public void process(Banknote bn, int quantity) throws IllegalArgumentException {
        if (bn.getNominal() == null || !bn.getNominal().equals(nominal)) {
            throw new IllegalArgumentException(String.format("Not suitable nominal: %s. Only % is allowed.", bn.getNominal(), nominal));
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity of banknotes cannot be negative.");
        }
        this.available.addAndGet(quantity);
    }

    @Override
    public void process(Map<Banknote, Integer> bns) {
        for (Iterator<Entry<Banknote, Integer>> iterator = bns.entrySet().iterator(); iterator.hasNext();) {
            Entry<Banknote, Integer> entry = iterator.next();
            if (this.nominal.equals(entry.getKey().getNominal())) {
                process(entry.getKey(), entry.getValue());
                iterator.remove();
            }
        }
    }

    @Override
    public int provideMoney(int sum) {
        int nominal = this.nominal.getValue();
        int requieredBnCnt = (sum - sum % nominal) / nominal;
        int availableBnCnt = available.get();
        int min = Math.min(availableBnCnt, requieredBnCnt);
        // Здесь бы следовало зарезервировать деньги, а "выдать" только
        // после того, как транзакция будет утверждена.
        // Но не хочется переусложнять.
        available.addAndGet(-min);
        return min;
    }

}
