package domain;


import Entities.*;
import Exceptions.AnzahlException;
import Exceptions.Artikelnamenichtgefunden;

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

    public void inWarenKorbLegen(Artikel artikel, int anzahl, Nutzer authuser) throws AnzahlException {

        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        boolean artikelImWarenkorb = istArtikelImWarenkorb(warenkorb, artikel);

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

    private void aktualisiereArtikelImWarenkorb(Warenkorb warenkorb, Artikel artikel, int anzahl) throws AnzahlException {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().equals(artikel)) {
                int gesamtAnzahl = warenkorbArtikel.getAnzahl() + anzahl;
                if (artikel.getBestand() >= gesamtAnzahl) {
                    warenkorbArtikel.setAnzahl(gesamtAnzahl);
                } else {
                    throw new AnzahlException(artikel.getBezeichnung());
                }
                break;
            }
        }
    }

    private void fuegeNeuenArtikelZumWarenkorbHinzu(Warenkorb warenkorb, Artikel artikel, int anzahl) throws AnzahlException  {
        if (artikel.getBestand() >= anzahl) {
            WarenkorbArtikel warenkorbArtikel = new WarenkorbArtikel(artikel, anzahl);
            warenkorb.addItem(warenkorbArtikel);
        } else {
            throw new AnzahlException(artikel.getBezeichnung());
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

    public void artikelMengeaendern(String Artikelname, int neueAnzahl, Nutzer authuser) throws AnzahlException, Artikelnamenichtgefunden {
        Warenkorb warenkorb = getWarenkorb(authuser);
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().getBezeichnung().toLowerCase().equals(Artikelname.toLowerCase())) {
                Artikel artikel = warenkorbArtikel.getArtikel();
                if (artikel.getBestand() >= neueAnzahl) {
                    warenkorbArtikel.setAnzahl(neueAnzahl);
                    if (neueAnzahl == 0) {
                        warenkorb.removeItem(warenkorbArtikel);
                    }
                } else {
                    throw new AnzahlException(artikel.getBezeichnung());
                }
                break;
            }
            else {
                throw new Artikelnamenichtgefunden(Artikelname);
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



