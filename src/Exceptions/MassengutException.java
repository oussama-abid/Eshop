package Exceptions;

public class MassengutException extends Exception {


    public MassengutException() {
        super("Fehler: Der Bestand muss durch die Packungsgröße teilbar sein..");
    }
}
