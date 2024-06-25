package Exceptions;

public class PackungsGrosseException extends Exception {

    private int packungsGrosse;

    public PackungsGrosseException(int packungsGrosse) {
        super("Fehler: Die Anzahl muss ein Vielfaches der Packungsgröße von " + packungsGrosse + " sein.");
        this.packungsGrosse = packungsGrosse;
    }
}
