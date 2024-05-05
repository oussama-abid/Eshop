package domain;

import Entities.Artikel;

import java.util.ArrayList;
import java.util.List;


public class ArtikelVerwaltung {

    private List<Artikel> artikelListe;

    public ArtikelVerwaltung() {

        artikelListe = new ArrayList<>();
        ArtikelHinzufuegen(new Artikel("tisch",123, 39.99F));// Fügt den neuen Artikel hinzu
        ArtikelHinzufuegen(new Artikel("lampe,", 21, 19.99F));
        ArtikelHinzufuegen(new Artikel("Stift",31,  5.99F));
        ArtikelHinzufuegen(new Artikel("Tacker",21,  4.99F));
        ArtikelHinzufuegen(new Artikel("Laptop",32,  1119.99F));
    }

    public void ArtikelHinzufuegen(Artikel artikel) {
        // Überprüfe, ob ein Artikel mit der gleichen Artikelnummer bereits vorhanden ist
        for (Artikel vorhandenerArtikel : artikelListe) {
            if (vorhandenerArtikel.getArtikelnummer() == artikel.getArtikelnummer()) {
                System.out.println("Artikel mit der Artikelnummer " + artikel.getArtikelnummer() + " existiert bereits.");
                return;  // Beende die Methode, wenn ein Artikel mit der gleichen Nummer gefunden wird
            }
        }
        // Füge den Artikel zur Liste hinzu
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

        veraenderterArtikel.setBestand(newBestand);

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

