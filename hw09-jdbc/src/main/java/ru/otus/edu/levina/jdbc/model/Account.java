package ru.otus.edu.levina.jdbc.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.edu.levina.jdbc.annotations.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
  
    @Id
    private long no;
    private String type;
    private BigDecimal rest;


}