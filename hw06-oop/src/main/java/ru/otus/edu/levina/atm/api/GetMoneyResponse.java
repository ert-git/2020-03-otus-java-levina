package ru.otus.edu.levina.atm.api;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GetMoneyResponse {
    public static final int SUCCESS = 1;
    public static final int NEG_SUM = 5;
    public static final int NOT_ENOUGHT_MONEY = 2;
    public static final int NO_SUIT_NOM = 3;
    public static final int NOT_ENOUGHT_NOM = 4;

    /**
     * response code
     */
    private int code;
    /**
     * code description (may be null, optional)
     */
    private String descr;
    /**
     * your money
     */
    private Map<Banknote, Integer> banknotes;
}
