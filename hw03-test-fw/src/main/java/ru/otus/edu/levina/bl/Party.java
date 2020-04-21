package ru.otus.edu.levina.bl;

/**
 * Parties are forbidden now. Let's make it here!
 *
 */
public class Party {

    private int guestsNumber;
    private int pizzaCount;

    public void prepareParty(int pizzaCount) {
        this.pizzaCount = pizzaCount;
        System.out.println("Pizza is ready!");
    }

    public boolean isReady() {
        return pizzaCount > 0;
    }

    public void join(String name) {
        guestsNumber++;
        System.out.println("Hello, " + name + "!");
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public void finishParty() {
        this.pizzaCount = 0;
        this.guestsNumber = 0;
        System.out.println("Bye-bye, please, don't remember put back my silverware!");
    }

}
