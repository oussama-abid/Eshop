package domain;

import Entities.*;
import Exceptions.Artikelnichtgefunden;
import Exceptions.BestandException;
import Exceptions.MassengutException;
import Exceptions.PackungsGrosseException;
import Persistence.FilePersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the articles in the EShop, including adding, updates, searching, removing articles ,
 * and throwing specific exceptions related to article management.
 *
 */
public class ArtikelVerwaltung {

    private List<Artikel> artikelListe = new ArrayList<>();
    private FilePersistenceManager fileManager = new FilePersistenceManager();
    private static int letzteArtikelnummer = 1000;

    /**
     * Adds a new article to the EShop inventory.
     *
     * @param Bezeichnung      the article name
     * @param bestand          the stock quantity
     * @param Preis            the article price per piece
     * @param istMassenartikel true if it's a mass article, false otherwise
     * @param packungsGrosse   the packaging size for mass article
     * @return the added article
     * @throws MassengutException if the article quantity and the packaging size are not compatible
     */
    public Artikel ArtikelHinzufuegen(String Bezeichnung, int bestand, float Preis, boolean istMassenartikel, int packungsGrosse) throws MassengutException {
        int nummer = Eindeutigenummer();
        Artikel artikel;

        if (istMassenartikel) {
            if (bestand % packungsGrosse == 0) {
                artikel = new Massenartikel(nummer, Bezeichnung, bestand, Preis, true, packungsGrosse);
            } else {
                throw new MassengutException();
            }
        } else {
            artikel = new Artikel(nummer, Bezeichnung, bestand, Preis, false);
        }

        artikelListe.add(artikel);
        speicherArtikel(artikel);
        return artikel;
    }

    /**
     * Retrieves the list of all articles in the EShop.
     *
     * @return a list of articles
     */
    public List<Artikel> getArtikelListe() {
        return artikelListe;
    }

    /**
     * Changes the stock or packaging size of an article.
     *
     * @param artikelnummer     the article number
     * @param newBestand        the new stock quantity
     * @param neuePackungsGrosse the new packaging size
     * @throws Exception if an error occurs while updating the article stock
     */
    public void BestandAendern(int artikelnummer, int newBestand, int neuePackungsGrosse) throws Exception {
        Artikel veraenderterArtikel = SucheArtikelPerID(artikelnummer);

        int aktualisierterBestand = veraenderterArtikel.getBestand() + newBestand;

        if (veraenderterArtikel instanceof Massenartikel) {
            Massenartikel massenartikel = (Massenartikel) veraenderterArtikel;

            if (massenartikel.getPackungsGrosse() != neuePackungsGrosse) {
                if (aktualisierterBestand % neuePackungsGrosse != 0) {
                    throw new BestandException();
                } else {
                    PackungsGrosseanderen(veraenderterArtikel.getArtikelnummer(), neuePackungsGrosse);
                }
            } else {
                if (aktualisierterBestand % massenartikel.getPackungsGrosse() != 0) {
                    throw new BestandException();
                }
            }
        }

        if (aktualisierterBestand < 0) {
            throw new Exception("Bestand kann nicht negativ sein");
        }

        veraenderterArtikel.setBestand(aktualisierterBestand);
        fileManager.aendereArtikelInDatei(artikelnummer, aktualisierterBestand);
    }

    /**
     * Searches for an article by its ID.
     *
     * @param artikelnummer the article number
     * @return the found article
     * @throws Artikelnichtgefunden if the article is not found
     */
    public Artikel SucheArtikelPerID(int artikelnummer) throws Artikelnichtgefunden {
        for (Artikel artikel : artikelListe) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                return artikel;
            }
        }
        throw new Artikelnichtgefunden(artikelnummer);
    }

    /**
     * Updates the article stock after a purchase.
     *
     * @param authuser the authenticated user
     */
    public void articlebestandanderen(Nutzer authuser) {
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> warenkorbListe = warenkorb.getWarenkorbListe();

        for (Artikel artikel : artikelListe) {
            for (WarenkorbArtikel item : warenkorbListe) {
                if (artikel.getArtikelnummer() == item.getArtikel().getArtikelnummer()) {
                    int anzahl = item.getAnzahl();
                    artikel.setBestand(artikel.getBestand() - anzahl);
                    fileManager.aendereArtikelInDatei(artikel.getArtikelnummer(), artikel.getBestand());
                }
            }
        }
    }



    /**
     * Searches for articles by name.
     *
     * @param suchbegriff the search term
     * @return a list of articles matching the search term
     */
    public List<Artikel> suchemitname(String suchbegriff) {
        List<Artikel> gefundeneArtikel = new ArrayList<>();

        for (Artikel artikel : getArtikelListe()) {
            if (artikel.getBezeichnung().toLowerCase().startsWith(suchbegriff) || String.valueOf(artikel.getArtikelnummer()).startsWith(suchbegriff)) {
                gefundeneArtikel.add(artikel);
            }
        }
        return gefundeneArtikel;
    }

    /**
     * Loads articles from the txt file into the EShop inventory.
     *
     * @param file the file containing articles
     */
    public void loadartikel(String file) {
        try {
            fileManager.openForReading(file);
            Artikel artikel;
            while ((artikel = fileManager.ladeArtikel()) != null) {
                artikelListe.add(artikel);
            }
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the packaging size of a mass article.
     *
     * @param artikel          the article number
     * @param neuePackungsGrosse the new packaging size
     * @throws Artikelnichtgefunden  if the article is not found
     * @throws PackungsGrosseException if the packaging size is invalid
     */
    public void PackungsGrosseanderen(int artikel, int neuePackungsGrosse) throws Artikelnichtgefunden, PackungsGrosseException {
        Artikel veraenderterArtikel = SucheArtikelPerID(artikel);
        Massenartikel massenartikel = (Massenartikel) veraenderterArtikel;
        massenartikel.setPackungsGrosse(neuePackungsGrosse);
    }

    /**
     * Checks if the packaging size of an article is valid.
     *
     * @param anzahl  the quantity
     * @param artikel the article
     * @throws PackungsGrosseException if the packaging size is invalid
     */
    public void checkpackunggrosse(int anzahl, Artikel artikel) throws PackungsGrosseException {
        Massenartikel massenArtikel = (Massenartikel) artikel;
        int packungsGrosse = massenArtikel.getPackungsGrosse();

        if (anzahl % packungsGrosse != 0) {
            throw new PackungsGrosseException(packungsGrosse);
        }
    }

    /**
     * Generates a unique article number for a new article.
     *
     * @return a unique article number
     */
    private int Eindeutigenummer() {
        int lastNummer = letzteArtikelnummer;

        for (Artikel artikel : artikelListe) {
            int nummer = artikel.getArtikelnummer();
            if (nummer > lastNummer) {
                lastNummer = nummer;
            }
        }

        return lastNummer + 1;
    }

    /**
     * Stores an article in a file for persistence.
     *
     * @param artikel the article to store
     */
    private void speicherArtikel(Artikel artikel) {
        try {
            fileManager.openForWriting("artikel.txt");
            fileManager.speichereArtikel(artikel);
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an article from the EShop inventory.
     *
     * @param artikel the article to remove
     */
    public void EntferneArtikel(Artikel artikel) {
        artikelListe.remove(artikel);
    }
}
