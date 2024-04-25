package Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rechnung
{
    private Date date;
    private List<WarenkorbArtikel> gekaufteArtikel = new ArrayList<>();
    private double gesamtpreis;
    private Kunde kunde;




    public Rechnung(Date date, List<WarenkorbArtikel> gekaufteArtikel, double gesamtpreis, Kunde kunde) {
        super();
        this.date = date;
        this.gekaufteArtikel = gekaufteArtikel;
        this.gesamtpreis = gesamtpreis;
        this.kunde = kunde;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public List<WarenkorbArtikel> getGekaufteArtikel() {
        return gekaufteArtikel;
    }
    public void setGekaufteArtikel(List<WarenkorbArtikel> gekaufteArtikel) {
        this.gekaufteArtikel = gekaufteArtikel;
    }
    public double getGesamtpreis() {
        return gesamtpreis;
    }
    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }
    public Kunde getKunde() {
        return kunde;
    }
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }






}
