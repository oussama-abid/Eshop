package domain;

import Entities.Artikel;
import Entities.User;
import ui.EShop;

import java.util.ArrayList;
import java.util.List;


public class ArtikelVerwaltung {

    private List<Artikel> artikelListe= new ArrayList<>();



    public ArtikelVerwaltung() {
        ArtikelHinzufuegen(new Artikel("tisch",123, 39.99F));// Fügt den neuen Artikel hinzu
        ArtikelHinzufuegen(new Artikel("lampe,", 21, 19.99F));
        ArtikelHinzufuegen(new Artikel("Stift",31,  5.99F));
        ArtikelHinzufuegen(new Artikel("Tacker",21,  4.99F));
        ArtikelHinzufuegen(new Artikel("Laptop",32,  1119.99F));

    }

    public void ArtikelHinzufuegen(Artikel artikel) {

        artikelListe.add(artikel);

        System.out.println("Neuer Artikel hinzugefügt: " + artikel.getBezeichnung() + ", Artikelnummer: " + artikel.getArtikelnummer());
    }

    public void EntferneArtikel(Artikel artikel) {

        artikelListe.remove(artikel);
    }

    public void BestandAendern(int artikelnummer, int newBestand) {
        Artikel veraenderterArtikel = null;
        for (Artikel artikel : artikelListe) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                veraenderterArtikel = artikel;
                break;
            }
        }

        veraenderterArtikel.setBestand(veraenderterArtikel.getBestand() + newBestand);

    }

    public Artikel SucheArtikelPerID(int artikelnummer) {
        for (Artikel artikel : artikelListe) { //Artikelliste
            if (artikel.getArtikelnummer() == artikelnummer) {  //überprüft ob die Artikelnummer vorhanden ist
                return artikel;    //Falls gefunden wird Artikel zurückgegeben
            }
        }
        return null;       //Falls nicht wird null zurückgegeben
    }

    public List<Artikel> getArtikelListe() {
                                            //Um die Artikelliste auszugeben
        return artikelListe;
    }


}

