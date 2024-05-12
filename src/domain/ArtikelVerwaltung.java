package domain;

import Entities.*;

import java.util.ArrayList;
import java.util.List;


public class ArtikelVerwaltung {

    private List<Artikel> artikelListe= new ArrayList<>();

    public void ersteArtikel() {
        Artikel artikel1 = new Artikel("Tisch", 12, 139.99F);
        Artikel artikel2 = new Artikel("Lampe", 21, 29.99F);
        Artikel artikel3 = new Artikel("Stift",31,  5.99F);
        Artikel artikel4 = new Artikel("Regenschirm",15,  19.99F);
        Artikel artikel5 = new Artikel("Laptop",11,  1119.99F);
        artikelListe.add(artikel1);
        artikelListe.add(artikel2);
        artikelListe.add(artikel3);
        artikelListe.add(artikel4);
        artikelListe.add(artikel5);
    }



 /**   Artikelverwaltung-Konstruktor gelöscht, ersteArtikel() als Ersatz eingefügt
  * public ArtikelVerwaltung() {
        ArtikelHinzufuegen(new Artikel("Tisch",12, 39.99F));// Fügt den neuen Artikel hinzu
        ArtikelHinzufuegen(new Artikel("Lampe", 21, 19.99F));
        ArtikelHinzufuegen(new Artikel("Stift",31,  5.99F));
        ArtikelHinzufuegen(new Artikel("Stift",31,  5.99F));
        ArtikelHinzufuegen(new Artikel("Laptop",32,  1119.99F));
    }
**/
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

    public void articlebestandanderen(Nutzer authuser) {
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> WarenkorbListe=warenkorb.getWarenkorbListe();
        for (Artikel artikel : artikelListe) {
            for (WarenkorbArtikel item : WarenkorbListe) {
                if (artikel.getArtikelnummer() == item.getArtikel().getArtikelnummer()) {
                    artikel.setBestand(artikel.getBestand()- item.getAnzahl());

                }
            }
            }

    }
}

