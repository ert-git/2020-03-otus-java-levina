package ru.otus.edu.levina.atm.api;

public enum Nominal {
    B100(100), B200(200), B500(500), B1000(1000);
    
    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }

}
