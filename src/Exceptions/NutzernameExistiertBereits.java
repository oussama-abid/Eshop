package Exceptions;

/**
 * Exception thrown when attempting to register a user with a username that already exists.
 * This exception indicates that the provided username is already taken in the system.
 */
public class NutzernameExistiertBereits extends Exception {

    private String nutzerName;

    /**
     * Constructs a NutzernameExistiertBereits exception with the given username.
     * @param nutzerName The username that already exists in the system.
     */
    public NutzernameExistiertBereits(String nutzerName) {
        super("Der Benutzername '" + nutzerName + "' existiert bereits.");
        this.nutzerName = nutzerName;
    }

    /**
     * Retrieves the username that already exists in the system.
     * @return The username that caused the exception.
     */
    public String getNutzerName() {
        return nutzerName;
    }
}
