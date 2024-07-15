package Entities;

/**
 * This class Represents an item in the shopping cart, consisting of an article, quantity added to the cart , and price per piece.
 */
public class WarenkorbArtikel {
    private Artikel artikel;
    private int anzahl;
    private double preis;

    /**
     * Constructs a new item in the cart with the specified article and quantity. and getting the price per piece for the specefeid article
     *
     * @param artikel the article added to the cart
     * @param anzahl the quantity of the article in the shopping cart
     */
    public WarenkorbArtikel(Artikel artikel, int anzahl) {
        this.artikel = artikel;
        this.anzahl = anzahl;
        this.preis = artikel.getPreis();
    }

    /**
     * Retrieves the shopping cart item.
     *
     * @return the article
     */
    public Artikel getArtikel() {
        return artikel;
    }

    /**
     * Sets the  shopping cart item.
     *
     * @param artikel the article to set
     */
    public void setArtikel(Artikel artikel) {
        this.artikel = artikel;
    }

    /**
     * Retrieves the quantity of the article in the shopping cart.
     *
     * @return the quantity of the article
     */
    public int getAnzahl() {
        return anzahl;
    }

    /**
     * Sets the quantity of the article in the shopping cart.
     *
     * @param anzahl the quantity to set
     */
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    /**
     * Retrieves the price per piece of the article.
     *
     * @return the price per piece
     */
    public double getPreis() {
        return preis;
    }

    /**
     * Sets the price per piece of the article.
     *
     * @param preis the price per piece to set
     */
    public void setPreis(double preis) {
        this.preis = preis;
    }

    /**
     * Calculates the total price of this item in the shopping cart.
     *
     * @return the total price of this item (price per piece * quantity)
     */
    public double getGesamtPreis() {
        double gesamtPreis = artikel.getPreis() * anzahl;
        return Math.round(gesamtPreis * 100.0) / 100.0;
    }

    /**
     * Returns a string representation of the WarenkorbArtikel object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "WarenkorbArtikel{" +
                "artikel=" + artikel +
                ", anzahl=" + anzahl +
                ", preis=" + preis +
                '}';
    }
}
