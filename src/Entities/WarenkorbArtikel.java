package Entities;


import java.util.List;

public class WarenkorbArtikel {
    private Artikel Artikel;
    private int Anzahl;
    private List<WarenkorbArtikel> WarenkorbArtikelListe;

    public WarenkorbArtikel(Artikel artikel, int anzahl) {
        super();
        this.Artikel = artikel;
        this.Anzahl = anzahl;
    }

    public Artikel getArtikel() {
        return Artikel;
    }
    public void setArtikel(Artikel artikel) {
        this.Artikel = artikel;
    }
    public int getAnzahl() {
        return Anzahl;
    }
    public void setAnzahl(int anzahl) {this.Anzahl = anzahl;
    }

}
