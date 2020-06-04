package ru.otus.edu.levina.serialization.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import ru.otus.edu.levina.serialization.json.MyGson;
import ru.otus.edu.levina.serialization.model.Catalog;
import ru.otus.edu.levina.serialization.model.Person;

public class SerializationTest {
    private Gson gson;
    private MyGson myGson;

    @Before
    public void before() {
        gson = new Gson();
        myGson = new MyGson();
    }

    @Test
    public void _01_full_filled() {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(8, "p1", 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, null));
        c1.getPersons().add(new Person(1, "p2", 13, false, System.currentTimeMillis(),
                0.5f, Double.MAX_VALUE, null));
        c1.setCode("anycode".getBytes());
        String myJson = myGson.toJson(c1);
        Catalog c2 = gson.fromJson(myJson, Catalog.class);
        Assert.assertEquals(c1, c2);
    }

    @Test
    public void _02_root_is_list() {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(null, "p1", 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, null));
        c1.getPersons().add(new Person(1, "p2", 13, false, System.currentTimeMillis(),
                0.5f, Double.MAX_VALUE, null));
        c1.setCode("anycode".getBytes());
        List<Catalog> cats1 = new ArrayList<>();
        cats1.add(c1);

        Catalog c2 = new Catalog("c2");
        c2.setPersons(new ArrayList<>());
        cats1.add(c2);

        cats1.add(new Catalog("c3"));

        String myJson = myGson.toJson(cats1);
        Catalog[] cats2 = gson.fromJson(myJson, Catalog[].class);
        Assert.assertEquals(cats1, Arrays.asList(cats2));
    }

    @Test
    public void _03_nulls() {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(null, null, 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, null));

        List<Catalog> cats1 = new ArrayList<>();
        cats1.add(c1);
        cats1.add(new Catalog("c2"));

        String myJson = myGson.toJson(cats1);
        Catalog[] cats2 = gson.fromJson(myJson, Catalog[].class);
        Assert.assertEquals(cats1, Arrays.asList(cats2));
    }

    @Test
    public void _04_recursion_depth() {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(null, "p1", 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, c1));
        String myJson = null;
        try {
            myJson = myGson.toJson(c1);
            Assert.fail("No exception");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().toLowerCase().contains("recursion"));
        }
        Assert.assertNull(myJson);
    }

    @Test
    public void _05_root_is_null() {
        Catalog c1 = null;
        String myJson = myGson.toJson(c1);
        Assert.assertNull(myJson);
        Catalog c2 = gson.fromJson(myJson, Catalog.class);
        Assert.assertNull(c2);
    }
}
