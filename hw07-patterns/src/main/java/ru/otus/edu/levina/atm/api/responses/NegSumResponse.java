package ru.otus.edu.levina.atm.api.responses;

public class NegSumResponse extends GetMoneyResponse {

    public NegSumResponse(int sum) {
        super("Not able to gather negative sum: " + sum, null);
    }

}
