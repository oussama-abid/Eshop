package Entities;

public class Artikel
{
    private int Artikelnummer;
    private String Bezeichnung;
    private int Bestand;
    private float Preis;



    public Artikel(int artikelnummer, String bezeichnung,int bestand, float preis) {
        super();
        this.Artikelnummer = artikelnummer;
        this.Bezeichnung = bezeichnung;
        this.Bestand = bestand;
        this.Preis = preis;
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
