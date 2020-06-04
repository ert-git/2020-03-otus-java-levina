package ru.otus.edu.levina.serialization;

import java.util.ArrayList;

import com.google.gson.Gson;

import ru.otus.edu.levina.serialization.json.MyGson;
import ru.otus.edu.levina.serialization.model.Catalog;
import ru.otus.edu.levina.serialization.model.Person;

public class Demo {
    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        Catalog c1 = new Catalog("c1");
        c1.setPersons(new ArrayList<Person>());
        c1.getPersons().add(new Person(null, "p1", 33, true, System.currentTimeMillis(),
                0.9f, Double.MAX_VALUE, c1));
        c1.getPersons().add(new Person(1, "p2", 13, false, System.currentTimeMillis(),
                0.5f, Double.MAX_VALUE, null));
        c1.setCode("anycode".getBytes());
        String myJson = new MyGson().toJson(c1);
        System.out.println(new MyGson().toJson(c1.getPersons()));
        System.out.println(myJson);
        System.out.println(gson.newBuilder().setPrettyPrinting().create().toJson(c1));
        Catalog c2 = gson.fromJson(myJson, Catalog.class);
        System.out.println(c1.equals(c2));
    }
}
