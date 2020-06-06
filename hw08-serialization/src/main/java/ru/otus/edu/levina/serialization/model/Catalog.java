package ru.otus.edu.levina.serialization.model;

import java.util.Collection;

import lombok.Data;
import lombok.NonNull;

@Data
public class Catalog {
    private static final int C1 = 1;
    private static int C2 = 2;
    private static transient int C3 = 3;
    private transient int C4 = 4;

    @NonNull
    private String name;
    private byte[] code;
    private Collection<Person> persons;
}
