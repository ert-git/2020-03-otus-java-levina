package ru.otus.edu.levina.atm.api.responses;

public class NotEnoughMoneyResponse extends GetMoneyResponse {

    public NotEnoughMoneyResponse(int total) {
        super( "Not enought money: available " + total, null);
    }

}
