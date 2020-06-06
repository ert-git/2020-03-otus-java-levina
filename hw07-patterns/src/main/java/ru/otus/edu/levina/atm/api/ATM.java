package ru.otus.edu.levina.atm.api;

import java.util.Map;

import ru.otus.edu.levina.atm.api.cmd.CmdResponseListener;
import ru.otus.edu.levina.atm.api.cmd.Command;
import ru.otus.edu.levina.atm.api.responses.GetMoneyResponse;

public interface ATM {
    /**
     * executes the command (Command pattern)
     * @param cmd
     */
    void process(Command cmd);
    
    /**
     * Obsever pattern
     * @param listener
     */
    void addCmdListener(CmdResponseListener listener);
    void removeCmdListener(CmdResponseListener listener);
    
    int getId();
    
    /**
     * resets the state to initial values (Memento pattern)
     */
    void reset();
    
    /**
     * return total number of executed operations
     * @return
     */
    int getOperationsCount();
 
    /**
     * Puts suitable banknotes into ATM and removes them from source batch
     * 
     * @param batch
     *            batch of banknotes
     */
    Map<Banknote, Integer> putMoney(Map<Banknote, Integer> batch);

    /**
     * Provides money
     * 
     * @param sum
     *            required sum
     * @return a batch of banknotes of available nominals or error description
     */
    GetMoneyResponse getMoney(int sum);

    /**
     * Returns total amount of money in ATM
     * @return
     */
    int getAvailableMoney();
    
}
