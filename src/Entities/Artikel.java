package Entities;

/**
 * This class represents an article in the shop inventory system, containing details such as
 * the article number, description, stock, price, and whether it is a mass article.
 *@author Ben Abid Oussama
 */
public class Artikel {

    private int Artikelnummer;
    private String Bezeichnung;
    private int Bestand;
    private float Preis;
    private boolean istMassenartikel;

    /**
     * constructor to create a new Artikel .
     *
     * @param Artikelnummer the unique identifier for the article
     * @param bezeichnung the name of the article
     * @param bestand the stock quantity of the article
     * @param preis  the price per piece of the article
     * @param istMassenartikel true if the article is a mass article, false otherwise
     */
    public Artikel(int Artikelnummer, String bezeichnung, int bestand, float preis, boolean istMassenartikel) {
        super();
        this.Artikelnummer = Artikelnummer;
        this.Bezeichnung = bezeichnung;
        this.istMassenartikel = istMassenartikel;
        this.Bestand = bestand;
        this.Preis = preis;
    }

    /**
     * Checks if the article is a mass article.
     *
     * @return true if the article is a mass article, false otherwise
     */
    public boolean isIstMassenartikel() {
        return istMassenartikel;
    }

    /**
     * Sets the mass article status of the article.
     *
     * @param istMassenartikel true if the article is a mass article, false otherwise
     */
    public void setIstMassenartikel(boolean istMassenartikel) {
        this.istMassenartikel = istMassenartikel;
    }

    /**
     * Gets the article number.
     *
     * @return the article number
     */
    public int getArtikelnummer() {
        return Artikelnummer;
    }

    /**
     * Sets the article number.
     *
     * @param artikelnummer the article number to set
     */
    public void setArtikelnummer(int artikelnummer) {
        Artikelnummer = artikelnummer;
    }

    /**
     * Gets the name of the article.
     *
     * @return the name of the article
     */
    public String getBezeichnung() {
        return Bezeichnung;
    }

    /**
     * Sets the name of the article.
     *
     * @param bezeichnung the name to set
     */
    public void setBezeichnung(String bezeichnung) {
        Bezeichnung = bezeichnung;
    }

    /**
     * Gets the stock quantity of the article.
     *
     * @return the stock quantity
     */
    public int getBestand() {
        return Bestand;
    }

    /**
     * Sets the stock quantity of the article.
     *
     * @param bestand the stock quantity to set
     */
    public void setBestand(int bestand) {
        Bestand = bestand;
    }

    /**
     * Gets the price per piece of the article.
     *
     * @return the price per piece
     */
    public float getPreis() {
        return Preis;
    }

    /**
     * Sets the price per piece of the article.
     *
     * @param preis the price per piece to set
     */
    public void setPreis(float preis) {
        this.Preis = preis;
    }

    /**
     * Returns a string representation of the article.
     *
     * @return a string representation of the article
     */
    @Override
    public String toString() {
        return "Artikelnummer=" + Artikelnummer +
                ", Bezeichnung='" + Bezeichnung + '\'' +
                ", Bestand=" + Bestand +
                ", Preis=" + Preis;
    }
}
