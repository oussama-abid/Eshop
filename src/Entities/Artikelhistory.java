package Entities;

import java.time.LocalDate;
import java.util.Objects;

public class Artikelhistory {
    private LocalDate date;
    private Artikel article;
    public int totalQuantity;

    public Artikelhistory(LocalDate date, Artikel article, int totalQuantity) {
        this.date = date;
        this.article = article;
        this.totalQuantity = totalQuantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public Artikel getArticle() {
        return article;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
    public void addQuantity(int quantity) {
        this.totalQuantity += quantity;
    }
    @Override
    public String toString() {
        return "Date=" + date + ", Artikel=" + article + ", TotalQuantity=" + totalQuantity;
    }
}
