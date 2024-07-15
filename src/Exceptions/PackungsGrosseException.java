package Exceptions;

/**
 * Exception thrown when the quantity is not a multiple of the packaging size for an article.
 * This exception indicates that the quantity provided does not adhere to the packaging size requirement.
 */
public class PackungsGrosseException extends Exception {

    private int packungsGrosse;

    /**
     * Constructs a PackungsGrosseException with the specified packaging size.
     * @param packungsGrosse The packaging size that the quantity should be a multiple of.
     */
    public PackungsGrosseException(int packungsGrosse) {
        super("Fehler: Die Anzahl muss ein Vielfaches der Packungsgröße von " + packungsGrosse + " sein.");
        this.packungsGrosse = packungsGrosse;
    }

    /**
     * Retrieves the packaging size associated with this exception.
     * @return The packaging size that caused the exception.
     */
    public int getPackungsGrosse() {
        return packungsGrosse;
    }
}
