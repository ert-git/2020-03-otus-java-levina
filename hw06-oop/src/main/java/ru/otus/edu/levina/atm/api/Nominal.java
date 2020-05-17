package ru.otus.edu.levina.atm.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class Nominal implements Comparable<Nominal> {
    private final int value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Nominal o) {
        return this.value - o.value;
    }
   
}
