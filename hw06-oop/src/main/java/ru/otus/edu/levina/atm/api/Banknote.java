package ru.otus.edu.levina.atm.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Banknote {

    private final Nominal nominal;

    @Override
    public String toString() {
        return "Banknote " + nominal;
    }

}
