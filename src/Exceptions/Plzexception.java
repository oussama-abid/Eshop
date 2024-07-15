package Exceptions;

/**
 * Exception thrown when an invalid postal code (PLZ) is encountered.
 * This exception indicates that the provided postal code is not valid.
 */
public class Plzexception extends Exception {

    /**
     * Constructs a Plzexception with a default error message.
     * The error message indicates that a valid postal code should be provided.
     */
    public Plzexception() {
        super("Fehler: Bitte geben Sie eine gültige PLZ ein.");
    }
}
