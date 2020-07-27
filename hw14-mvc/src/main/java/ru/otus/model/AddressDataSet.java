package ru.otus.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@Table(name = "address")
public class AddressDataSet {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String street;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    public AddressDataSet(String street, User user) {
        super();
        this.street = street;
        this.user = user;
    }

    
}
