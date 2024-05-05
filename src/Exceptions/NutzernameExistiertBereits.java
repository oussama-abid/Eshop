package Exceptions;

public class NutzernameExistiertBereits extends Exception {

    public NutzernameExistiertBereits() {super();  }

    public String getMessage() {
        return "Nutzername existiert bereits, wählen Sie einen neuen Benutzernamen!";
    }
}