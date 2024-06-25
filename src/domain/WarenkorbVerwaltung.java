package domain;


import Entities.*;
import Exceptions.AnzahlException;
import Exceptions.Artikelnamenichtgefunden;
import Persistence.FilePersistenceManager;

import java.util.Date;


public class WarenkorbVerwaltung {

    private Rechnung Rechnung;
    private Date date;

    FilePersistenceManager FPM = new FilePersistenceManager();

    public WarenkorbVerwaltung() {

        FilePersistenceManager fpm = new FilePersistenceManager();

    }

    public Warenkorb getWarenkorb(Nutzer authuser) {

        Kunde kunde = (Kunde) authuser;
        return kunde.getWarenkorb();

    }

    public void inWarenKorbLegen(Artikel artikel, int anzahl, Nutzer authuser) throws AnzahlException {
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        boolean artikelImWarenkorb = istArtikelImWarenkorb(warenkorb, artikel);

        if (artikel instanceof Massenartikel) {
            Massenartikel massenartikel = (Massenartikel) artikel;
            int packungsGrosse = massenartikel.getPackungsGrosse();
            if (anzahl % packungsGrosse != 0) {
                throw new AnzahlException("Die Anzahl muss ein Vielfaches der Packungsgröße von " + packungsGrosse + " sein.");
            }
        }

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
                if (artikel instanceof Massenartikel) {
                    Massenartikel massenartikel = (Massenartikel) artikel;
                    int packungsGrosse = massenartikel.getPackungsGrosse();
                    if (gesamtAnzahl % packungsGrosse != 0) {
                        throw new AnzahlException("Die Anzahl muss ein Vielfaches der Packungsgröße von " + packungsGrosse + " sein.");
                    }
                }
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

    public void artikelMengeaendern(int artikelNummer, int neueAnzahl, Nutzer authuser) throws AnzahlException, Artikelnamenichtgefunden {
        Warenkorb warenkorb = getWarenkorb(authuser);
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().getArtikelnummer() == artikelNummer) {
                Artikel artikel = warenkorbArtikel.getArtikel();
                if (artikel instanceof Massenartikel) {
                    Massenartikel massenartikel = (Massenartikel) artikel;
                    int packungsGrosse = massenartikel.getPackungsGrosse();
                    if (neueAnzahl % packungsGrosse != 0) {
                        throw new AnzahlException("Die Anzahl muss ein Vielfaches der Packungsgröße von " + packungsGrosse + " sein.");
                    }
                }
                if (artikel.getBestand() >= neueAnzahl) {
                    warenkorbArtikel.setAnzahl(neueAnzahl);
                    if (neueAnzahl == 0) {
                        warenkorb.removeItem(warenkorbArtikel);
                    }
                } else {
                    throw new AnzahlException(artikel.getBezeichnung());
                }

                return; // Artikel gefunden und Menge geändert, Methode beenden
            }
        }
        throw new Artikelnamenichtgefunden("Artikelnummer: " + artikelNummer);
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


    public void aktualisiereArtikelbestandInDatei(Warenkorb warenkorb) {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            Artikel artikel = warenkorbArtikel.getArtikel();
            int artikelnummer = artikel.getArtikelnummer();
            int neuerBestand = artikel.getBestand();

            //
            if (!FPM.aendereArtikelInDatei(artikelnummer, neuerBestand)) {
                System.out.println("Fehler beim Aktualisieren des Bestands in der Datei für Artikelnummer: " + artikelnummer);
                // Hier könntest du entsprechende Maßnahmen ergreifen, z.B. eine Rückgängig-Funktion implementieren.
            }
        }
    }


}



