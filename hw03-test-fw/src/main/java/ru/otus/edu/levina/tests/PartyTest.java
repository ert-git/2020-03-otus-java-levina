package ru.otus.edu.levina.tests;

import ru.otus.edu.levina.bl.Party;
import ru.otus.edu.levina.fw.annotations.After;
import ru.otus.edu.levina.fw.annotations.Before;
import ru.otus.edu.levina.fw.annotations.Test;

public class PartyTest {

    private Party party;

    @Before
    public void _before() {
        party = new Party();
        party.prepareParty(3);
    }

    @After
    public void _after() {
        party.finishParty();
    }
    
    @Test
    public void test_03_failed() throws Exception {
        // fail and change system state here 
        party.join("Squirrel");
        party.join("Beaver");
        int expected = 1; // ups!
        int actual = party.getGuestsNumber();
        if (actual != expected) {
            throw new Exception(String.format("expected %s, actual %s", expected, actual));
        }
    }

    @Test
    public void test_01_before() throws Exception {
        // check if _before was executed
        boolean expected = true;
        boolean actual = party.isReady();
        if (actual != expected) {
            throw new Exception(String.format("expected %s, actual %s", expected, actual));
        }
     }
    
    @Test
    public void test_02_after() throws Exception {
        // check if _after has been executed and clear state after test_03
        int expected = 0;
        int actual = party.getGuestsNumber();
        if (actual != expected) {
            throw new Exception(String.format("expected %s, actual %s", expected, actual));
        }
    }



}
