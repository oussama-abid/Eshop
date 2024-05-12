package ui;

import Entities.Artikel;
import domain.PersonenVerwaltung;

public class  Main {

    public static void main(String[] args) {

         // ersteArtikel();
        PersonenVerwaltung user = new PersonenVerwaltung();

        EShop shop = new EShop();


        Cui cui = new Cui(shop);
    }
}
