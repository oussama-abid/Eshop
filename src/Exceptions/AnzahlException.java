package Exceptions;

/**
 * An exception thrown when there is an insufficient quantity of an article in stock.
 */
public class AnzahlException extends Exception {



    /**
     * Constructs an AnzahlException with the specified article name.
     *
     * @param artikel the name of the article for which the quantity is insufficient
     */
    public AnzahlException(String artikel) {
        super("Unzureichender Bestand für " + artikel + ". Bitte überprüfen Sie den Bestand im Shop.");

    }


}
