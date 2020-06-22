package ru.otus.edu.levina.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {

    private Integer id;
    private String name;
    private int age;
    private boolean married;
    private long created;
    private float activityIndex;
    private double rate;
    
    private Catalog catalog;
    
}
