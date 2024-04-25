import java.util.Date;

public class Events {
    private Date datum;
    private Article artikel;
    private int anzahl;
    private Mitarbeiter Mitarbeiter;







    public Events(Date datum, Article artikel, int anzahl, Mitarbeiter mitarbeiter) {
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
    public Article getArtikel() {
        return artikel;
    }
    public void setArtikel(Article artikel) {
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
