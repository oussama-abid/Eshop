package Entities;


import java.util.List;

public class WarenkorbArtikel {
    private Artikel Artikel;
    private int Anzahl;
    private double Preis;


    public WarenkorbArtikel(Artikel artikel, int anzahl) {
        this.Artikel = artikel;
        this.Anzahl = anzahl;
        this.Preis = artikel.getPreis();
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

    public void setPreis(double preis) {
        Preis = preis;
    }

    public double getPreis() {
        return Preis;
    }

    public double getGesamtPreis() {
        double gesamtPreis = Artikel.getPreis() * Anzahl;
        return Math.round(gesamtPreis * 100.0) / 100.0;
    }
    @Override
    public String toString() {
        return
                "artikel=" + Artikel +
                ", quantity=" + Anzahl +
                        ", preis=" + Preis +
                '}';
    }
}
