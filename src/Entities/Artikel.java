package Entities;

public class Artikel
{
    private int Artikelnummer;
    private String Bezeichnung;
    private int Bestand;
    private float Preis;
    private boolean istMassenartikel;


    public Artikel(int Artikelnummer,String bezeichnung,int bestand, float preis, boolean istMassenartikel) {
        super();
        this.Artikelnummer = Artikelnummer;
        this.Bezeichnung = bezeichnung;
        this.istMassenartikel=istMassenartikel;
        this.Bestand = bestand;
        this.Preis = preis;
    }

    public boolean isIstMassenartikel() {
        return istMassenartikel;
    }

    public void setIstMassenartikel(boolean istMassenartikel) {
        this.istMassenartikel = istMassenartikel;
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

    @Override
    public String toString() {
        return
                "Artikelnummer=" + Artikelnummer +
                ", Bezeichnung='" + Bezeichnung + '\'' +
                ", Bestand=" + Bestand +
                ", Preis=" + Preis ;
    }
}
