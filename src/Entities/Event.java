package Entities;



import java.time.LocalDate; // Improved API ,  It offers better support for date  ,represents a date without time information

public class Event {

    private String operation;
    private LocalDate date;
    private Artikel article;
    private int quantity;
    private Nutzer nutzer;


    public Event(String operation, LocalDate date, Artikel article, int quantity, Nutzer nutzer) {
        this.operation = operation;
        this.date = date;
        this.article = article;
        this.quantity = quantity;
        this.nutzer = nutzer;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Artikel getArticle() {
        return article;
    }

    public void setArticle(Artikel article) {
        this.article = article;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Nutzer getUser() {
        return nutzer;
    }

    public void setUser(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    @Override
    public String toString() {
        return
                "operation='" + operation + '\'' +
                ", date=" + date +
                ", article=" + article.getArtikelnummer() +
                ", quantity=" + quantity +
                ", user=" + nutzer.getName()
                ;
    }
}
