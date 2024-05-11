package Entities;


import java.util.List;

public class WarenkorbArtikel {
    private Artikel Artikel;
    private int Anzahl;


    public WarenkorbArtikel(Artikel artikel, int anzahl) {
        this.Artikel = artikel;
        this.Anzahl = anzahl;
    }

    public Entities.Artikel getArtikel() {
        return Artikel;
    }

    public void setArtikel(Entities.Artikel artikel) {
        Artikel = artikel;
    }

    public int getAnzahl() {
        return Anzahl;
    }

    public void setAnzahl(int anzahl) {
        Anzahl = anzahl;
    }

    @Override
    public String toString() {
        return
                "artikel=" + Artikel +
                ", quantity=" + Anzahl +
                '}';
    }
}
