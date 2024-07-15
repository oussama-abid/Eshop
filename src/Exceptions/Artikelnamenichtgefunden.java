package Exceptions;

/**
 * Exception thrown when a requested article name is not found.
 */
public class Artikelnamenichtgefunden extends Exception {

    /**
     * Constructs an Artikelnamenichtgefunden with the specified article name.
     *
     * @param artikelname the name of the article that was not found
     */
    public Artikelnamenichtgefunden(String artikelname) {
        super("Der Artikel " + artikelname + " wurde nicht gefunden.");
    }
}
