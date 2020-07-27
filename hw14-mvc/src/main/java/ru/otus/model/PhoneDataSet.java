package ru.otus.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = "user")
@Getter
@Setter
@EqualsAndHashCode(exclude = "user")
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class PhoneDataSet {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Column
    private String number;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    public PhoneDataSet(String number, User user) {
        super();
        this.number = number;
        this.user = user;
    }
    
    
}