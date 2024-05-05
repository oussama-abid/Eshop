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
    public void updateStock(int artikelnummer, int newBestand) {
        Artikel artikelToUpdate = null;
        for (Artikel artikel : artikels) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                artikelToUpdate = artikel;
                break;
            }
        }

            artikelToUpdate.setBestand(newBestand);

    }









}
