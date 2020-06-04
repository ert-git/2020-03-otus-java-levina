package ru.otus.edu.levina.atm.impl;

import lombok.Data;
import lombok.NonNull;

@Data
public class AtmMemento {
    @NonNull
    private final AtmState state;

}
