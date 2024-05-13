package domain;



import java.util.ArrayList;

import Entities.*;

import java.util.Date;


public class WarenkorbVerwaltung {

    private Rechnung Rechnung;
    private Date date;

    public WarenkorbVerwaltung() {

    }

    public Warenkorb getWarenkorb(Nutzer authuser) {

        Kunde kunde = (Kunde) authuser;
        return kunde.getWarenkorb();

    }

    public void inWarenKorbLegen(Artikel artikel, int anzahl, Nutzer authuser) {

        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        boolean artikelImWarenkorb = istArtikelImWarenkorb(warenkorb, artikel);

        WarenkorbArtikel warenkorbArtikel = new WarenkorbArtikel(artikel, anzahl);
        if (artikelImWarenkorb) {
            aktualisiereArtikelImWarenkorb(warenkorb, artikel, anzahl);
        } else {
            fuegeNeuenArtikelZumWarenkorbHinzu(warenkorb, artikel, anzahl);
        }
    }

    private boolean istArtikelImWarenkorb(Warenkorb warenkorb, Artikel artikel) {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().equals(artikel)) {
                return true;
            }
        }
        return false;
    }

    private void aktualisiereArtikelImWarenkorb(Warenkorb warenkorb, Artikel artikel, int anzahl) {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().equals(artikel)) {
                int gesamtAnzahl = warenkorbArtikel.getAnzahl() + anzahl;
                if (artikel.getBestand() >= gesamtAnzahl) {
                    warenkorbArtikel.setAnzahl(gesamtAnzahl);
                } else {
                    System.out.println("Unzureichender Bestand für " + artikel.getBezeichnung() +  ",bitte überprüfen sie den Bestand im Shop" );
                }
                break;
            }
        }
    }

    private void fuegeNeuenArtikelZumWarenkorbHinzu(Warenkorb warenkorb, Artikel artikel, int anzahl) {
        if (artikel.getBestand() >= anzahl) {
            WarenkorbArtikel warenkorbArtikel = new WarenkorbArtikel(artikel, anzahl);
            warenkorb.addItem(warenkorbArtikel);
        } else {
            System.out.println("Unzureichender Bestand für " + artikel.getBezeichnung() +  ",bitte überprüfen sie den Bestand im Shop");
        }
    }

    public void kaufen(Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        Kunde kunde = (Kunde) authuser;
        date = new Date();
        Rechnung = new Rechnung(date, warenkorb.calculateTotalPrice(), kunde);

        System.out.println(Rechnung);
        warenkorb.Warenkorbleeren();

    }

    public void Warenkorbleeren(Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        warenkorb.Warenkorbleeren();
    }

    public void artikelMengeaendern(String Artikelname, int neueAnzahl, Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().getBezeichnung().toLowerCase().equals(Artikelname)) {
                Artikel artikel = warenkorbArtikel.getArtikel();
                if (artikel.getBestand() >= neueAnzahl) {
                    warenkorbArtikel.setAnzahl(neueAnzahl);
                    if (neueAnzahl == 0) {
                        warenkorb.removeItem(warenkorbArtikel);
                    }
                } else {
                    System.out.println("Unzureichender Bestand für " + artikel.getBezeichnung() +  ",bitte überprüfen sie den Bestand im Shop");
                }
                break;
            }
        }
    }

    public boolean checkArtikelwarenkorb(String artikelname, Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {

            if (warenkorbArtikel.getArtikel().getBezeichnung().toLowerCase().equals(artikelname)) {
                return true;
            }
        }
        return false;
    }
}



