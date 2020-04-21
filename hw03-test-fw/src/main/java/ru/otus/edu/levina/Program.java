package ru.otus.edu.levina;

import ru.otus.edu.levina.tests.PartyTest;
import ru.otus.edu.levina.fw.TestRunner;

public class Program {

    public static void main(String[] args) throws Exception {
        TestRunner.runTest(PartyTest.class);
    }

}
