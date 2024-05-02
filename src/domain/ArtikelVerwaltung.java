package domain;

import Entities.Artikel;

import java.util.ArrayList;
import java.util.List;


public class ArtikelVerwaltung {

    private List<Artikel> artikelListe;

    public ArtikelVerwaltung() {

        artikelListe = new ArrayList<>();
        ArtikelHinzufuegen(new Artikel(111,"Tisch", 7, 39.99F));// Fügt den neuen Artikel hinzu
        ArtikelHinzufuegen(new Artikel(222,"Lampe", 21, 19.99F));
        ArtikelHinzufuegen(new Artikel(333,"Stift", 100, 5.99F));
        ArtikelHinzufuegen(new Artikel(444,"Tacker", 49, 4.99F));
        ArtikelHinzufuegen(new Artikel(555,"Laptop", 10, 1119.99F));
    }

    public void ArtikelHinzufuegen(Artikel artikel) {
        for (Artikel neuerArtikel : artikelListe) {
            if (neuerArtikel.getArtikelnummer() == artikel.getArtikelnummer()) {
                return;  // Beendet die Methode, wenn ein Artikel mit der gleichen Nummer gefunden wird
            }
        }
        artikelListe.add(artikel);  // Fügt den Artikel hinzu, wenn kein Duplikat gefunden wurde
    }

    public void EntferneArtikel(Artikel artikel) {

        artikelListe.remove(artikel);
    }

    public void ArtikelBestandVeraendern(Artikel artikel, int AenderungsMenge) {

        if (artikel != null && (artikel.getBestand() + AenderungsMenge >= 0)) {                 // Überprüfen, ob der Artikel existiert und ob die Menge den Bestand nicht negativ macht
            artikel.setBestand(artikel.getBestand() + AenderungsMenge);                 // + - ist immer noch minus (wenn man -3 als AenderungsMenge eingibt) 5 + (-3) = 2 neuer Bestand
        }
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

