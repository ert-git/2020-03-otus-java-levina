package ru.otus.edu.levina.atm.api.responses;

public class NotEnoughNominalResponse extends GetMoneyResponse {

    public NotEnoughNominalResponse() {
        super("Failed to gather required sum", null);
    }

}
