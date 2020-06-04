package ru.otus.edu.levina.atm.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.Banknote;
import ru.otus.edu.levina.atm.api.Cell;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;
import ru.otus.edu.levina.atm.api.cmd.CmdResponseListener;
import ru.otus.edu.levina.atm.api.cmd.Command;
import ru.otus.edu.levina.atm.api.responses.GetMoneyResponse;
import ru.otus.edu.levina.atm.api.responses.NegSumResponse;
import ru.otus.edu.levina.atm.api.responses.NoSuitNominalResponse;
import ru.otus.edu.levina.atm.api.responses.NotEnoughMoneyResponse;
import ru.otus.edu.levina.atm.api.responses.NotEnoughNominalResponse;
import ru.otus.edu.levina.atm.api.responses.SuccessGetMoneyResponse;

public class ATMImpl implements ATM {
    @Getter
    private final Integer id;
    private final List<Cell> cells;
    private final int minNominal;

    private AtmMemento memento;
    private Set<CmdResponseListener> listeners = new HashSet<>();
    private AtmState state;
    
    private Cell firstCell;

    public ATMImpl(Integer id, List<Cell> cells, Map<Banknote, Integer> batch) {
        this.id = id;
        this.cells = cells.stream()
                .sorted((c1, c2) -> c2.getNominal().compareTo(c1.getNominal()))
                .collect(Collectors.toList());
        for (int i = 0; i < cells.size() - 1; i++) {
            cells.get(i).setNext(cells.get(i + 1));
        }
        firstCell = cells.get(0);
        firstCell.process(batch);
        minNominal = cells.stream().mapToInt(c -> c.getNominal().getValue()).min().getAsInt();
        state = new AtmState(0);
        memento = new AtmMemento(new AtmState(state));
    }

    // ----------------- command pattern
    @Override
    public void process(Command cmd) {
        CmdResponse response = cmd.execute(this);
        listeners.forEach(l -> {
            try {
                // ----------------- observer pattern
                l.onResponse(response);
            } catch (Exception e) {
                System.err.println(String.format("Failed to complete cmd % for listener %s", l, cmd));
                e.printStackTrace();
            }
        });
    }

    @Override
    public void reset() {
        // ----------------- memento pattern
        state = memento.getState();
    }

    @Override
    public void addCmdListener(CmdResponseListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeCmdListener(CmdResponseListener listener) {
        listeners.remove(listener);
    }

    @Override
    public int getOperationsCount() {
        return state.getOperationCount();
    }

    // ----------------------------------------------------------
    // previous home work implementation
    // ----------------------------------------------------------

    @Override
    public Map<Banknote, Integer> putMoney(Map<Banknote, Integer> batch) {
        state.incOperationCount();
        return firstCell.process(batch);
    }

    @Override
    public GetMoneyResponse getMoney(int sum) {
        state.incOperationCount();
        int total = getAvailableMoney();
        if (sum < 0) {
            return new NegSumResponse(sum);
        }
        if (total < sum) {
            return new NotEnoughMoneyResponse(total);
        }
        if (sum % minNominal != 0) {
            return new NoSuitNominalResponse(sum);
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
            return new SuccessGetMoneyResponse(bns);
        } else {
            // rollback
            firstCell.process(bns);
        }
        return new NotEnoughNominalResponse();
    }

    @Override
    public int getAvailableMoney() {
        return cells.stream().mapToInt(c -> c.getAvailableMoney()).sum();
    }

}
