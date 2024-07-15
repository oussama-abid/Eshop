package Entities;

/**
 * This class represents a mass article in the shop inventory system, extending the {@link Artikel} class
 * to include additional details for mass article  such as the package size.
 *
 * @see Artikel
 */
public class Massenartikel extends Artikel {

    private int packungsGrosse;

    /**
     * Constructs a new Massenartikel with the specified details.
     *
     * @param artikelnummer the unique identifier for the article
     * @param bezeichnung the name of the article
     * @param bestand the stock quantity of the article
     * @param preis the price per piece of the article
     * @param istMassenartikel true if the article is a mass article, false otherwise
     * @param packungsGrosse the size of the package
     */
    public Massenartikel(int artikelnummer, String bezeichnung, int bestand, float preis, boolean istMassenartikel, int packungsGrosse) {
        super(artikelnummer, bezeichnung, bestand, preis, istMassenartikel);
        this.packungsGrosse = packungsGrosse;
    }

    /**
     * Gets the size of the package.
     *
     * @return the size of the package
     */
    public int getPackungsGrosse() {
        return packungsGrosse;
    }

    /**
     * Sets the size of the package.
     *
     * @param packungsGrosse the size of the package to set
     */
    public void setPackungsGrosse(int packungsGrosse) {
        this.packungsGrosse = packungsGrosse;
    }

    /**
     * Returns a string representation of the mass article.
     *
     * @return a string representation of the mass article
     */
    @Override
    public String toString() {
        return super.toString() + ", Packungsgröße=" + packungsGrosse;
    }
}
