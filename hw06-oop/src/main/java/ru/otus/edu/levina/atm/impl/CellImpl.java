package ru.otus.edu.levina.atm.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.ToString;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.Nominal;

@ToString
public class CellImpl implements Cell {
    private final Nominal nominal;
    private final int capacity;

    private int available = 0;

    public CellImpl(Nominal nominal, int capacity) {
        this.nominal = nominal;
        this.capacity = capacity;
    }

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
    public void process(Map<Banknote, Integer> bns) {
        // Процессинг не является потокобезопасным. Но здесь исходим из того, что
        // а) банкомат (и ячейка) физически не многопоточные (т.е. одновременно два человека не могут получать и класть деньги)
        // б) если все-таки закладываться на многопоточность, то надо сперва определить порядок нагрузки, время ожидания и т.п.
        for (Iterator<Entry<Banknote, Integer>> iterator = bns.entrySet().iterator(); iterator.hasNext();) {
            Entry<Banknote, Integer> entry = iterator.next();
            Integer wantPut = entry.getValue();
            if (wantPut == null || wantPut < 0) {
                System.out.println(
                        String.format("%s: Not valid banknote quntity: %s. Only positive is allowed.", this, wantPut));
                continue;
            }
            if (!this.nominal.equals(entry.getKey().getNominal())) {
                continue;
            }
            int canPut = capacity - available;
            if (canPut <= 0) {
                System.out.println(String.format("%s: Cannot process banknotes: the cell is full.", this));
                continue;
            }
            int rest = wantPut > canPut ? wantPut - canPut : 0;
            if (rest > 0) {
                System.out.println(
                        String.format("%s: Can process only %s banknotes: the cell is almost full.", this, canPut));
            }
            // Спорный вопрос, стоит ли менять входные данные или нужно вернуть новую коллекцию.
            // Изменение входных данных выбрано в силу более точного отражения действительности:
            // деньги из наших рук в банкомат именно перемещаются, а не копируются (к сожалению).
            // Изменяя входные данные, мы, тем самым, не разрешаем использовать исходную пачку повторно
            // (например, если захотим сэмулировать докладку непринятого остатка в другой банкомат).
            bns.put(entry.getKey(), rest);
            available += Math.min(canPut, wantPut);
        }
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
