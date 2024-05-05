package Entities;

public class Artikel
{
    private int Artikelnummer;
    private String Bezeichnung;
    private int Bestand;
    private float Preis;
    private static int letzteArtikelnummer = 1000;


    public Artikel(String bezeichnung,int bestand, float preis) {
        super();
        this.Artikelnummer = ++letzteArtikelnummer;
        this.Bezeichnung = bezeichnung;
        this.Bestand = bestand;
        this.Preis = preis;
    }
    private static int EindeutigeArtikelnummer() {
        return ++letzteArtikelnummer;
    }
    public int getArtikelnummer() {return Artikelnummer;}
    public void setArtikelnummer(int artikelnummer) {
        Artikelnummer = artikelnummer;
    }
    public String getBezeichnung() {
        return Bezeichnung;
    }
    public void setBezeichnung(String bezeichnung) {
        Bezeichnung = bezeichnung;
    }
    public int getBestand() {
        return Bestand;
    }
    public void setBestand(int bestand) {
        Bestand = bestand;
    }
    public float getPreis() { return Preis;}
    public void setPreis(float preis) { this.Preis = preis;}

}
