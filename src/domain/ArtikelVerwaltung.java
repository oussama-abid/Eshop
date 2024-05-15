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

        int aktualisierterBestand = veraenderterArtikel.getBestand() + newBestand;
        veraenderterArtikel.setBestand(aktualisierterBestand);

        if (aktualisierterBestand <= 0) {
            veraenderterArtikel.setBestand(0);

        }
    }

    public Artikel SucheArtikelPerID(int artikelnummer) {
        for (Artikel artikel : artikelListe) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                return artikel;
            }
        }
        return null;
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

    public Artikel sucheartiklemitname(String suchbegriff) {
        for (Artikel artikel : artikelListe) {
            if (artikel.getBezeichnung().toLowerCase().equals(suchbegriff)) {
                return artikel;
            }
        }
        return null;

    }

    public List<Artikel> suchemitname(String suchbegriff) {
        List<Artikel> gefundeneArtikel = new ArrayList<>();

        for (Artikel artikel : getArtikelListe()) {
            if (artikel.getBezeichnung().toLowerCase().startsWith(suchbegriff) || String.valueOf(artikel.getArtikelnummer()).startsWith(suchbegriff) ) {
                gefundeneArtikel.add(artikel);
            }
        }
        return gefundeneArtikel;
    }
}

