package ru.otus.edu.levina.atm.api.responses;

import java.util.Map;

import ru.otus.edu.levina.atm.api.Banknote;

public class SuccessGetMoneyResponse extends GetMoneyResponse {

    public SuccessGetMoneyResponse(Map<Banknote, Integer> bns) {
        super(null, bns);
    }

}
