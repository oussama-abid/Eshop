package Exceptions;

/**
 * Exception thrown when a change in stock does not meet packaging size requirements.
 * This exception indicates that a change in stock quantity was attempted,
 * but the quantity does not natch with the packaging size .
 */
public class BestandException extends Exception {

    /**
     * Constructs a BestandException with a default error message.
     * The message indicates that the stock quantity must be a multiple of the packaging size.
     */
    public BestandException() {
        super("Bestand muss ein Vielfaches der Packungsgröße sein");
    }
}
