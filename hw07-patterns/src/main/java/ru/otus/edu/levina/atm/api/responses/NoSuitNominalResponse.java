package ru.otus.edu.levina.atm.api.responses;

public class NoSuitNominalResponse extends GetMoneyResponse {

    public NoSuitNominalResponse(int sum) {
        super("No suitable nominals to gather: " + sum, null);
    }

}
