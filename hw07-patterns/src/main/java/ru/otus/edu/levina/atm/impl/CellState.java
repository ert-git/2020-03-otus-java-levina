package ru.otus.edu.levina.atm.impl;

import lombok.Data;
import lombok.Setter;

@Data
public class CellState {

    @Setter
    private int available;

    public CellState(CellState state) {
        this.available = state.getAvailable();
    }

    public CellState(int available) {
        this.available = available;
    }
    

}
