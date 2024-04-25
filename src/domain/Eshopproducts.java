package domain;

import Entities.Artikel;

import java.util.ArrayList;
import java.util.List;

public class Eshopproducts {

    private List<Artikel> artikels = new ArrayList<>();




    public Eshopproducts() {
        super();
    }

    public List<Artikel> getArticles() {
        return artikels;
    }

    public void addArticle(Artikel artikel) {
        artikels.add(artikel);
    }










}
