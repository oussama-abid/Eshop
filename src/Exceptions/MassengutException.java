package Exceptions;

/**
 * Exception thrown when the quantity of article is not divisible by the packaging size.
 * This exception indicates that there is an error because the quantity in stock must be divisible by the packaging size.
 */
public class MassengutException extends Exception {

    /**
     * Constructs a MassengutException with a default error message.
     * The message indicates that the quantity in stock must be divisible by the packaging size.
     */
    public MassengutException() {
        super("Fehler: Der Bestand muss durch die Packungsgröße teilbar sein.");
    }
}
