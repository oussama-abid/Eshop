
public class Article {
    private int Artikelnummer;
    private String Bezeichnung;
    private String Bestand;
    private float price;


    public Article(int artikelnummer, String bezeichnung, String bestand, float price) {
        super();
        Artikelnummer = artikelnummer;
        Bezeichnung = bezeichnung;
        Bestand = bestand;
        this.price = price;
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
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }















}
