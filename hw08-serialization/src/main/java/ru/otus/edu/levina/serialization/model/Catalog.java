package ru.otus.edu.levina.serialization.model;

import java.util.Collection;

import lombok.Data;
import lombok.NonNull;

@Data
public class Catalog {
    @NonNull
    private String name;
    private byte[] code;
    private Collection<Person> persons;
}
