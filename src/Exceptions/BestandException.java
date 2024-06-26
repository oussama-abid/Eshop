package Exceptions;

public class BestandException extends Exception {


    public BestandException() {
        super("Bestand muss ein Vielfaches der Packungsgröße sein");
    }
}
