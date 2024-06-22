package Exceptions;

public class Artikelnichtgefunden extends Exception {

    private int artikelnummer;

    public Artikelnichtgefunden(int artikelnummer) {
        super("Der artikel " + artikelnummer + " nicht gefunden .");
        this.artikelnummer = artikelnummer;
    }

}