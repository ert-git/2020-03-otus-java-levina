package ru.otus.edu.levina.serialization.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import ru.otus.edu.levina.serialization.json.JsonSerializationException;
import ru.otus.edu.levina.serialization.json.MyGson;
import ru.otus.edu.levina.serialization.model.Catalog;
import ru.otus.edu.levina.serialization.model.Person;

public class SerializationTest {
    private Gson gson;
    private MyGson myGson;

    @BeforeEach
    public void before() {
        gson = new Gson();
        myGson = new MyGson();
    }

    @Test
    public void _01_full_filled() throws JsonSerializationException {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(8, "p1", 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, null));
        c1.getPersons().add(new Person(1, "p2", 13, false, System.currentTimeMillis(),
                0.5f, Double.MAX_VALUE, null));
        c1.setCode("anycode".getBytes());
        String myJson = myGson.toJson(c1);
        Catalog c2 = gson.fromJson(myJson, Catalog.class);
        assertEquals(c1, c2);
    }

    @Test
    public void _02_root_is_list() throws JsonSerializationException {
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
        assertEquals(cats1, Arrays.asList(cats2));
    }

    @Test
    public void _03_nulls() throws JsonSerializationException {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(null, null, 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, null));

        List<Catalog> cats1 = new ArrayList<>();
        cats1.add(c1);
        cats1.add(new Catalog("c2"));

        String myJson = myGson.toJson(cats1);
        Catalog[] cats2 = gson.fromJson(myJson, Catalog[].class);
        assertEquals(cats1, Arrays.asList(cats2));
    }

    @Test
    public void _04_recursion_depth() {
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(null, "p1", 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, c1));
        Throwable exception = assertThrows(JsonSerializationException.class, () -> myGson.toJson(c1));
        assertEquals("Recursion overflow: max = " + MyGson.MAX_RECURSION_DEPTH, exception.getMessage());
    }

    @Test
    public void _05_other() throws JsonSerializationException {
        assertEquals(gson.toJson((short) 2f), myGson.toJson((short) 2f));
        assertEquals(gson.toJson('b'), myGson.toJson('b'));
        assertEquals(gson.toJson(null), myGson.toJson(null));
    }

    @Test
    public void _06_transient() throws JsonSerializationException {
        Catalog c = new Catalog("c1");
        String json = myGson.toJson(c);
        assertEquals(gson.toJson((c)), json);
        assertAll(
                () -> assertFalse(json.contains("\"C1\":")),
                () -> assertFalse(json.contains("\"C2\":")),
                () -> assertFalse(json.contains("\"C3\":")),
                () -> assertFalse(json.contains("\"C4\":")));
    }

}
