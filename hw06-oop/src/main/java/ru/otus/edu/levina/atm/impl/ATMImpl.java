package ru.otus.edu.levina.atm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.GetMoneyResponse;

public class ATMImpl implements ATM {

    private final List<Cell> cells;
    private final int minNominal;

    public ATMImpl(List<Cell> cells) {
        this.cells = cells.stream()
                .sorted((c1, c2) -> c2.getNominal().compareTo(c1.getNominal()))
                .collect(Collectors.toList());
        minNominal = cells.stream().mapToInt(c -> c.getNominal().getValue()).min().getAsInt();
    }

    @Override
    public void putMoney(Map<Banknote, Integer> batch) {
        cells.forEach(cell -> cell.process(batch));
    }

    @Override
    public GetMoneyResponse getMoney(int sum) {
        int total = getAvailableMoney();
        if (sum < 0) {
            return new GetMoneyResponse(GetMoneyResponse.NEG_SUM, "Not able to gather negative sum: " + sum, null);
        }
        if (total < sum) {
            return new GetMoneyResponse(GetMoneyResponse.NOT_ENOUGHT_MONEY, "Not enought money: available " + total, null);
        }
        if (sum % minNominal != 0) {
            return new GetMoneyResponse(GetMoneyResponse.NO_SUIT_NOM, "No suitable nominals to gather: " + sum, null);
        }
        // "Физически" здесь следовало бы отдать коллекцию купюр. Но это имеет смысл только в том случае,
        // если бы купюры имели собственные качества (например, "мятая", "серия", "подозрительная" и т.п.).
        // Если таких признаков не подразумевается, то создание 100500 объектов-купюр выглядит избыточным
        // и можно обойтись метаинформацией.
        Map<Banknote, Integer> bns = new HashMap<>();
        for (Cell cell : cells) {
            int cnt = cell.provideMoney(sum);
            sum -= cnt * cell.getNominal().getValue();
            Banknote bn = new Banknote(cell.getNominal());
            bns.computeIfAbsent(bn, key -> cnt);
        }
        if (0 == sum) {
            return new GetMoneyResponse(GetMoneyResponse.SUCCESS, null, bns);
        } else {
            // rollback
            cells.forEach(cell -> cell.process(bns));
        }
        return new GetMoneyResponse(GetMoneyResponse.NOT_ENOUGHT_NOM, "Failed to gather required sum", null);
    }

    @Override
    public int getAvailableMoney() {
        return cells.stream().mapToInt(c -> c.getAvailableMoney()).sum();
    }

}
