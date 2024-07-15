package Entities;

import java.time.LocalDate;

/**
 * This class Represents an event in the e-shop system, capturing operations performed by a customer or employee on an article.
 */
public class Event {

    private String operation;
    private LocalDate date;
    private Artikel article;
    private int quantity;
    private Nutzer user;

    /**
     * Constructs a new event with the specified operation, date, article, quantity, and user.
     *
     * @param operation the operation performed (e.g., "einlagerung", "auslagerung")
     * @param date the date when the operation occurred
     * @param article the article involved in the operation
     * @param quantity the quantity of the article involved
     * @param user the user who performed the operation
     */
    public Event(String operation, LocalDate date, Artikel article, int quantity, Nutzer user) {
        this.operation = operation;
        this.date = date;
        this.article = article;
        this.quantity = quantity;
        this.user = user;
    }

    /**
     * Retrieves the operation type
     *
     * @return the operation performed
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the operation type.
     *
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Retrieves the date when the operation occurred.
     *
     * @return the date of the operation
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date when the operation occurred.
     *
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves the article involved in the operation.
     *
     * @return the article
     */
    public Artikel getArticle() {
        return article;
    }

    /**
     * Sets the article involved in the operation.
     *
     * @param article the article
     */
    public void setArticle(Artikel article) {
        this.article = article;
    }

    /**
     * Retrieves the quantity of the article involved in the operation.
     *
     * @return the quantity of the article
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the article involved in the operation.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the user who performed the operation.
     *
     * @return the user who performed the operation
     */
    public Nutzer getUser() {
        return user;
    }

    /**
     * Sets the user who performed the operation.
     *
     * @param user the user to set
     */
    public void setUser(Nutzer user) {
        this.user = user;
    }

    /**
     * Returns a string representation of the operation details.
     *
     * @return a string representation of the operation details
     */
    @Override
    public String toString() {
        return "Event{" +
                "operation='" + operation + '\'' +
                ", date=" + date +
                ", article=" + article.getArtikelnummer() +
                ", quantity=" + quantity +
                ", user=" + user.getName() +
                '}';
    }
}
