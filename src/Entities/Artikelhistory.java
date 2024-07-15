package Entities;

import java.time.LocalDate;

/**
 * This class represents the history of an article, capturing the date, article details, and total quantity involved.
 * Used to track changes in the quantity of an article on a specific date.
 */
public class Artikelhistory {
    private LocalDate date;
    private Artikel article;
    public int totalQuantity;

    /**
     * Constructs an Artikelhistory object with the specified date, article, and total quantity.
     *
     * @param date the date
     * @param article the article
     * @param totalQuantity the total quantity change of the article
     */
    public Artikelhistory(LocalDate date, Artikel article, int totalQuantity) {
        this.date = date;
        this.article = article;
        this.totalQuantity = totalQuantity;
    }

    /**
     * Retrieves the date.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves the article.
     *
     * @return the article
     */
    public Artikel getArticle() {
        return article;
    }

    /**
     * Retrieves the total quantity change of the article.
     *
     * @return the total quantity change of the article
     */
    public int getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * Adds quantity to the total quantity of the article.
     * This method is used to calculate the quantity change for the specified date:
     * - After "auslagerung" , quantity will decrease (negative value).
     * - After "kaufen" , quantity will decrease.
     * - After "einlagerung"  quantity will increase.
     *
     * @param quantity the quantity to add
     */
    public void addQuantity(int quantity) {
        this.totalQuantity += quantity;
    }

    /**
     * Returns a string representation of the article's  history on a specific date.
     * It displays the initial quantity of the article before any operations were performed by users,
     * followed by the quantity after all operations on the same article on the same date.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Date=" + date +
                ", Artikel =" + article.getArtikelnummer() +
                ", Am Anfang des Tages der Bestand von Artikel " + article.getArtikelnummer() + " war =" + article.getBestand() +
                ", und am Ende des Tages: " + (article.getBestand() + totalQuantity);
    }
}
