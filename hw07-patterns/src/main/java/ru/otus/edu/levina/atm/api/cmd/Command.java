package ru.otus.edu.levina.atm.api.cmd;

import ru.otus.edu.levina.atm.api.ATM;

public interface Command {
    
    CmdResponse execute(ATM atm);
}
