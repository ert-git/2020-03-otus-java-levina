package ru.otus.edu.levina.hibernate.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@NamedQueries(value = {
        @NamedQuery(name="User.findByLogin", query = "Select u from User u where login=:login"),
        @NamedQuery(name="User.getAllUsers", query = "Select u from User u order by login")})
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private String password;

    @Column
    private String login;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhoneDataSet> phones = new ArrayList<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AddressDataSet address;

    public User(String name, String login, String password, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        super();
        this.name = name;
        this.login = login;
        this.password = password;
        this.age = age;
        this.address = address;
        this.phones = phones;
    }

    public User(String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        this(name, null, null, age, address, phones);
    }

    public User(String name, String login, String password) {
        this(name, login, password, 0, null, null);
    }

}