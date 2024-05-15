package Exceptions;

public class NutzernameExistiertBereits extends Exception {
    public NutzernameExistiertBereits() {
        super("der Benutzername existiert bereits .");
    }
}
