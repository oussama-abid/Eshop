package Exceptions;

public class FalscheLoginDaten extends Exception {


    public FalscheLoginDaten() {
        super("Falscher Benutzerkennung oder Passwort.");

    }

}