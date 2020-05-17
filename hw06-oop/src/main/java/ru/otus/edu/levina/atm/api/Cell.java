package ru.otus.edu.levina.atm.api;

import java.util.Map;

/**
 * Keeps banknotes of the same nominal
 * 
 * @author levina
 *
 */
public interface Cell {

    /**
     * Returns a nominal of this cell
     * 
     * @return
     */
    Nominal getNominal();

    /**
     * Returns a number of banknotes in the cell
     * 
     * @return
     */
    int getAvailableBanknoteCount();

    /**
     * Puts a banknote into the cell
     * 
     * @param bn
     *            a banknote
     * @param count
     *            count of banknotes
     * @throws AtmException
     *             if the banknote is not valid (has not suitable nominal, etc)
     */
    void process(Banknote bn, int quantity) throws IllegalArgumentException;

    /**
     * Selects (and removes) suitable banknotes from the batch
     * 
     * @param bns a batch of banknotes
     */
    void process(Map<Banknote, Integer> bns);

    /**
     * Returns available amount of money
     * 
     * @return
     */
    int getAvailableMoney();

    /**
     * Provides required money if possible
     * 
     * @param sum
     *            required amount of money
     * @return number of banknotes of this nominal for this sum
     */
    int provideMoney(int sum);
}