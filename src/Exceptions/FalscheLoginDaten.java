package Exceptions;

/**
 * Exception thrown when incorrect login credentials are provided.
 * This exception indicates that the provided username or password is incorrect.
 */
public class FalscheLoginDaten extends Exception {

    /**
     * Constructs a FalscheLoginDaten exception.
     * The message indicates that the provided username or password is incorrect.
     */
    public FalscheLoginDaten() {
        super("Falscher Benutzerkennung oder Passwort.");
    }
}
