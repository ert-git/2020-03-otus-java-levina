package ru.otus.edu.levina.atm.impl;

import lombok.Data;
import lombok.Setter;

@Data
public class AtmState {

    @Setter(lombok.AccessLevel.NONE)
    private int operationCount;

    public AtmState(AtmState state) {
        this.operationCount = state.getOperationCount();
    }

    public AtmState(int operationCount) {
        this.operationCount = operationCount;
    }
    
    public int incOperationCount() {
        return ++operationCount; 
    }
}
