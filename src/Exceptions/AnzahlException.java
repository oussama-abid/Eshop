package Exceptions;

public class AnzahlException extends Exception {

    private String artikel;

    public AnzahlException(String artikel) {
        super("Unzureichender Bestand für  " + artikel + " bitte überprüfen sie den Bestand im Shop .");
        this.artikel = artikel;
    }

}
