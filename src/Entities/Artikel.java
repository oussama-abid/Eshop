package Entities;

public class Artikel
{
    private int Artikelnummer;
    private String Bezeichnung;
    private String Bestand;
    private float Preis;



    public Artikel(int artikelnummer, String bezeichnung, String bestand, float preis) {
        super();
        Artikelnummer = artikelnummer;
        Bezeichnung = bezeichnung;
        Bestand = bestand;
        this.Preis = preis;
    }

    public int getArtikelnummer() {
        return Artikelnummer;
    }
    public void setArtikelnummer(int artikelnummer) {
        Artikelnummer = artikelnummer;
    }
    public String getBezeichnung() {
        return Bezeichnung;
    }
    public void setBezeichnung(String bezeichnung) {
        Bezeichnung = bezeichnung;
    }
    public String getBestand() {
        return Bestand;
    }
    public void setBestand(String bestand) {
        Bestand = bestand;
    }
    public float getPreis() { return Preis;}
    public void setPreis(float preis) { this.Preis = preis;}















}
