package Exceptions;

public class NutzernameExistiertBereits extends Exception {

    private String nutzerName;

    public NutzernameExistiertBereits(String nutzerName) {
        super("der Benutzername " + nutzerName + " existiert bereits .");
        this.nutzerName = nutzerName;
    }

    public String getNutzerName() {
        return nutzerName;
    }
}
