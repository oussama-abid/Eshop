package Entities;

import java.util.Date;

public class Events {
    private Date datum;
    private Artikel artikel;
    private int anzahl;
    private Entities.Mitarbeiter Mitarbeiter;





    public Events(Date datum, Artikel artikel, int anzahl, Mitarbeiter mitarbeiter) {
        super();
        this.datum = datum;
        this.artikel = artikel;
        this.anzahl = anzahl;
        Mitarbeiter = mitarbeiter;
    }
    public Date getDatum() {
        return datum;
    }
    public void setDatum(Date datum) {
        this.datum = datum;
    }
    public Artikel getArtikel() {
        return artikel;
    }
    public void setArtikel(Artikel artikel) {
        this.artikel = artikel;
    }
    public int getAnzahl() {
        return anzahl;
    }
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    public Mitarbeiter getMitarbeiter() {
        return Mitarbeiter;
    }
    public void setMitarbeiter(Mitarbeiter mitarbeiter) {
        Mitarbeiter = mitarbeiter;
    }







}
