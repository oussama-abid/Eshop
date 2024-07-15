package domain;

import Entities.*;
import Exceptions.AnzahlException;
import Exceptions.Artikelnamenichtgefunden;
import Exceptions.PackungsGrosseException;
import Persistence.FilePersistenceManager;

import java.util.Date;

/**
 * Manages the shopping cart operations for customers.
 */
public class WarenkorbVerwaltung {

    private Rechnung Rechnung;
    private Date date;
    private FilePersistenceManager FPM = new FilePersistenceManager();

    /**
     * Retrieves the shopping cart for a given authenticated user (customer).
     *
     * @param authuser the authenticated user
     * @return the shopping cart of the customer
     */
    public Warenkorb getWarenkorb(Nutzer authuser) {
        Kunde kunde = (Kunde) authuser;
        return kunde.getWarenkorb();
    }

    /**
     * Adds an article to the shopping cart.
     *
     * @param artikel   the article to add
     * @param anzahl    the quantity of the article to add
     * @param authuser  the authenticated user (customer)
     * @throws PackungsGrosseException if the quantity does not match packaging size for mass articles
     * @throws AnzahlException         if the quantity exceeds the available stock
     */
    public void inWarenKorbLegen(Artikel artikel, int anzahl, Nutzer authuser) throws PackungsGrosseException, AnzahlException {
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        boolean artikelImWarenkorb = istArtikelImWarenkorb(warenkorb, artikel);

        if (artikel instanceof Massenartikel) {
            Massenartikel massenartikel = (Massenartikel) artikel;
            int packungsGrosse = massenartikel.getPackungsGrosse();
            if (anzahl % packungsGrosse != 0) {
                throw new PackungsGrosseException(packungsGrosse);
            }
        }

        if (artikelImWarenkorb) {
            aktualisiereArtikelImWarenkorb(warenkorb, artikel, anzahl);
        } else {
            fuegeNeuenArtikelZumWarenkorbHinzu(warenkorb, artikel, anzahl);
        }
    }

    /**
     * Checks if an article is already in the shopping cart.
     *
     * @param warenkorb the shopping cart to check
     * @param artikel   the article to check
     * @return true if the article is already in the shopping cart, false otherwise
     */
    private boolean istArtikelImWarenkorb(Warenkorb warenkorb, Artikel artikel) {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().equals(artikel)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the quantity of an existing article in the shopping cart.
     *
     * @param warenkorb the shopping cart
     * @param artikel   the article to update
     * @param anzahl    the new quantity of the article
     * @throws AnzahlException if the new quantity exceeds the available stock
     */
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

    /**
     * Adds a new article to the shopping cart.
     *
     * @param warenkorb the shopping cart
     * @param artikel   the article to add
     * @param anzahl    the quantity of the article to add
     * @throws AnzahlException if the quantity exceeds the available stock
     */
    private void fuegeNeuenArtikelZumWarenkorbHinzu(Warenkorb warenkorb, Artikel artikel, int anzahl) throws AnzahlException {
        if (artikel.getBestand() >= anzahl) {
            WarenkorbArtikel warenkorbArtikel = new WarenkorbArtikel(artikel, anzahl);
            warenkorb.addItem(warenkorbArtikel);
        } else {
            throw new AnzahlException(artikel.getBezeichnung());
        }
    }

    /**
     * Completes the purchase.
     *
     * @param authuser the authenticated user (customer)
     */
    public void kaufen(Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        Kunde kunde = (Kunde) authuser;
        date = new Date();
        Rechnung = new Rechnung(date, warenkorb.calculateTotalPrice(), kunde);
        aktualisiereArtikelbestandInDatei(warenkorb);
        System.out.println(Rechnung);
        warenkorb.Warenkorbleeren();
    }

    /**
     * Empties the shopping cart of the customer.
     *
     * @param authuser the authenticated user (customer)
     */
    public void Warenkorbleeren(Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        warenkorb.Warenkorbleeren();
    }

    /**
     * Changes the quantity of an article in the shopping cart.
     *
     * @param artikelNummer the article number to change
     * @param neueAnzahl    the new quantity of the article
     * @param authuser      the authenticated user (customer)
     * @throws AnzahlException           if the new quantity exceeds the available stock
     * @throws Artikelnamenichtgefunden if the article with specified number is not found
     */
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
                return; // Article found and quantity changed, exit method
            }
        }
        throw new Artikelnamenichtgefunden("Artikelnummer: " + artikelNummer);
    }

    /**
     * Checks if an article with specified name is in the shopping cart.
     *
     * @param artikelname the name of the article to check
     * @param authuser    the authenticated user (customer)
     * @return true if the article is in the shopping cart, false otherwise
     */
    public boolean checkArtikelwarenkorb(String artikelname, Nutzer authuser) {
        Warenkorb warenkorb = getWarenkorb(authuser);
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().getBezeichnung().equalsIgnoreCase(artikelname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the stock levels of articles in the file after purchase.
     *
     * @param warenkorb the shopping cart
     */
    public void aktualisiereArtikelbestandInDatei(Warenkorb warenkorb) {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            Artikel artikel = warenkorbArtikel.getArtikel();
            int artikelnummer = artikel.getArtikelnummer();
            int neuerBestand = artikel.getBestand();

            if (!FPM.aendereArtikelInDatei(artikelnummer, neuerBestand)) {
                System.out.println("Fehler beim Aktualisieren des Bestands in der Datei für Artikelnummer: " + artikelnummer);

            }
        }
    }
}
