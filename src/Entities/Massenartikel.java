package Entities;

public class Massenartikel extends Artikel {

    private int packungsGrosse;

    public Massenartikel(int artikelnummer, String bezeichnung, int bestand, float preis, boolean istMassenartikel, int packungsGrosse) {
        super(artikelnummer, bezeichnung, bestand, preis, istMassenartikel);
        this.packungsGrosse = packungsGrosse;
    }

    public int getPackungsGrosse() {
        return packungsGrosse;
    }

    public void setPackungsGrosse(int packungsGrosse) {
        this.packungsGrosse = packungsGrosse;
    }

    @Override
    public String toString() {
        return super.toString() + ", Packungsgröße=" + packungsGrosse;
    }
}
