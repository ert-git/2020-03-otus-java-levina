package ru.otus.edu.levina.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.edu.levina.jdbc.annotations.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private long id;
    private String name;
    private int age;

}