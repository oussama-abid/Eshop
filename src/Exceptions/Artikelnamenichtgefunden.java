package Exceptions;

public class Artikelnamenichtgefunden extends Exception {

    private String artikelname;

    public Artikelnamenichtgefunden(String artikelname) {
        super("Der artikel " + artikelname + " nicht gefunden .");
        this.artikelname = artikelname;
    }

}
