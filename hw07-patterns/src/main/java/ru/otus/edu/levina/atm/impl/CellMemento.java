package ru.otus.edu.levina.atm.impl;

import lombok.Data;
import lombok.NonNull;

@Data
public class CellMemento {
    @NonNull
    private final CellState state;

}
