package ru.otus.edu.levina.atm.api.responses;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.otus.edu.levina.atm.api.Banknote;

@Getter
@AllArgsConstructor
@ToString
public abstract class GetMoneyResponse {
    /**
     * code description (may be null, optional)
     */
    private String descr;
    /**
     * your money
     */
    private Map<Banknote, Integer> banknotes;
}
