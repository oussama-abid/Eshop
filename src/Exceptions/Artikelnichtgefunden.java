package Exceptions;

/**
 * Exception thrown when a requested article number is not found.

 */
public class Artikelnichtgefunden extends Exception {

    /**
     * Constructs an Artikelnichtgefunden with the specified article number.
     *
     * @param artikelnummer the number of the article that was not found
     */
    public Artikelnichtgefunden(int artikelnummer) {
        super("Der Artikel " + artikelnummer + " wurde nicht gefunden.");
    }
}
