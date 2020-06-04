package ru.otus.edu.levina.atm.api;

import java.util.Map;

public interface ATM {
 
    /**
     * Puts suitable banknotes into ATM and updates their quantity in the source batch
     * 
     * @param batch
     *            batch of banknotes
     */
    void putMoney(Map<Banknote, Integer> batch);

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
