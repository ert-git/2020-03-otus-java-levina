package ru.otus.edu.levina;

import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

public class HelloOtus {
    public static void main(String... args) {
        ImmutableSet<Person> persons = ImmutableSet.of(
            new Person("name1", "lastname1"),
            new Person("name2", "lastname2"));
        persons.forEach(System.out::println);
    }
}
