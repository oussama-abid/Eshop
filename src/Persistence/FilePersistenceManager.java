package Persistence;

import Entities.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages reading from and writing to files for entities like Nutzer, Artikel, and Event.
 */
public class FilePersistenceManager implements PersistenceManager {
    private BufferedReader reader = null;
    private PrintWriter writer = null;

    /**
     * Opens a file for reading.
     *
     * @param datei The file to be opened for reading.
     * @throws IOException If an I/O error occurs while opening the file.
     */
    @Override
    public void openForReading(String datei) throws IOException {
        reader = new BufferedReader(new FileReader(datei));
    }

    /**
     * Opens a file for writing.
     *
     * @param datei The file to be opened for writing.
     * @throws IOException If an I/O error occurs while opening the file.
     */
    @Override
    public void openForWriting(String datei) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei, true)));
    }

    /**
     * Closes the currently open reader and writer.
     *
     * @return True if the close operations were successful, false otherwise.
     */
    @Override
    public boolean close() {
        if (writer != null)
            writer.close();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Loads  user  from the  file.
     *
     * @return The loaded Nutzer object.
     * @throws IOException If an I/O error occurs while reading the Nutzer information.
     */
    public Nutzer ladeNutzer() throws IOException {
        String type = liesZeile();
        if (type == null) {
            return null;
        }

        int nutzerNummer = Integer.parseInt(liesZeile());
        String name = liesZeile();
        String benutzerkennung = liesZeile();
        String passwort = liesZeile();

        if (type.equals("Mitarbeiter")) {
            return new Mitarbeiter(nutzerNummer, name, benutzerkennung, passwort);
        } else if (type.equals("Kunde")) {
            String straße = liesZeile();
            String stadt = liesZeile();
            String bundesland = liesZeile();
            int postleitzahl = Integer.parseInt(liesZeile());
            String land = liesZeile();
            Adresse adresse = new Adresse(straße, stadt, bundesland, postleitzahl, land);
            return new Kunde(nutzerNummer, name, benutzerkennung, passwort, adresse);
        }
        return null;
    }

    /**
     * Saves a user  to the file.
     *
     * @param nutzer The Nutzer object to be saved.
     * @return True if the saving operation was successful, false otherwise.
     */
    public boolean speichereNutzer(Nutzer nutzer) {
        if (nutzer instanceof Kunde) {
            Kunde kunde = (Kunde) nutzer;
            Adresse adresse = kunde.getAdresse();

            writer.println("Kunde");
            schreibeZeile(String.valueOf(kunde.getNutzerNummer()));
            schreibeZeile(kunde.getName());
            schreibeZeile(kunde.getBenutzerkennung());
            schreibeZeile(kunde.getPasswort());
            schreibeZeile(adresse.getStraße());
            schreibeZeile(adresse.getStadt());
            schreibeZeile(adresse.getBundesland());
            schreibeZeile(String.valueOf(adresse.getPostleitzahl()));
            schreibeZeile(adresse.getLand());
        } else if (nutzer instanceof Mitarbeiter) {
            Mitarbeiter mitarbeiter = (Mitarbeiter) nutzer;
            writer.println("Mitarbeiter");
            schreibeZeile(String.valueOf(mitarbeiter.getNutzerNummer()));
            schreibeZeile(mitarbeiter.getName());
            schreibeZeile(mitarbeiter.getBenutzerkennung());
            schreibeZeile(mitarbeiter.getPasswort());
        }
        return true;
    }

    /**
     * Loads an Artikel from  file.
     *
     * @return The loaded Artikel object.
     * @throws IOException If an I/O error occurs while reading the Artikel information.
     */
    public Artikel ladeArtikel() throws IOException {
        String artikelTyp = liesZeile();
        if (artikelTyp == null) {
            return null;
        }

        try {
            int artikelnummer = Integer.parseInt(liesZeile());
            String bezeichnung = liesZeile();
            int bestand = Integer.parseInt(liesZeile());
            float preis = Float.parseFloat(liesZeile());

            if (artikelTyp.equals("massenartikel")) {
                int packungsGrosse = Integer.parseInt(liesZeile());
                return new Massenartikel(artikelnummer, bezeichnung, bestand, preis, true, packungsGrosse);
            } else if (artikelTyp.equals("artikel")) {
                return new Artikel(artikelnummer, bezeichnung, bestand, preis, false);
            } else {
                throw new IOException("Unbekannter Artikeltyp: " + artikelTyp);
            }
        } catch (NumberFormatException e) {
            throw new IOException("Fehler beim Parsen der Artikelinformationen: " + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new IOException("Fehler beim Lesen der Artikelinformationen: Eine erforderliche Zeile fehlt", e);
        }
    }

    /**
     * Saves an Artikel object to the file.
     *
     * @param artikel The Artikel object to be saved.
     * @return True if the saving operation was successful, false otherwise.
     */
    public boolean speichereArtikel(Artikel artikel) {
        if (artikel instanceof Massenartikel) {
            Massenartikel massenartikel = (Massenartikel) artikel;
            writer.println("massenartikel");
            schreibeZeile(String.valueOf(massenartikel.getArtikelnummer()));
            schreibeZeile(massenartikel.getBezeichnung());
            schreibeZeile(String.valueOf(massenartikel.getBestand()));
            schreibeZeile(String.valueOf(massenartikel.getPreis()));
            schreibeZeile(String.valueOf(massenartikel.getPackungsGrosse()));
        } else {
            writer.println("artikel");
            schreibeZeile(String.valueOf(artikel.getArtikelnummer()));
            schreibeZeile(artikel.getBezeichnung());
            schreibeZeile(String.valueOf(artikel.getBestand()));
            schreibeZeile(String.valueOf(artikel.getPreis()));
        }

        return true;
    }

    /**
     * Loads an Event object from the currently open file.
     *
     * @return The loaded Event object.
     * @throws IOException If an I/O error occurs while reading the Event information.
     */
    public Event ladeEvent() throws IOException {
        String operation = liesZeile();
        if (operation == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(liesZeile());
        Artikel article = ladeArtikel();
        int quantity = Integer.parseInt(liesZeile());
        Nutzer nutzer = ladeNutzer();

        return new Event(operation, date, article, quantity, nutzer);
    }

    /**
     * Saves an Event object to the file.
     *
     * @param event The Event object to be saved.
     * @return True if the saving operation was successful, false otherwise.
     */
    public boolean speichereevent(Event event) {
        schreibeZeile(event.getOperation());
        schreibeZeile(event.getDate().toString());
        speichereArtikel(event.getArticle());
        schreibeZeile(String.valueOf(event.getQuantity()));
        speichereNutzer(event.getUser());

        return true;
    }

    /**
     * Changes the stock of an article in the file.
     *
     * @param artikelnummer The article number of the article whose stock is to be changed.
     * @param neuerBestand  The new stock quantity to be set for the article.
     * @return True if the stock change operation was successful, false otherwise.
     */
    public boolean aendereArtikelInDatei(int artikelnummer, int neuerBestand) {
        List<Artikel> artikelListe = new ArrayList<>();
        boolean artikelGefunden = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("artikel.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("artikel")) {
                    int nummer = Integer.parseInt(reader.readLine());
                    String bezeichnung = reader.readLine();
                    int bestand = Integer.parseInt(reader.readLine());
                    float preis = Float.parseFloat(reader.readLine());
                    Artikel artikel = new Artikel(nummer, bezeichnung, bestand, preis, false);
                    artikelListe.add(artikel);
                } else if (line.equals("massenartikel")) {
                    int nummer = Integer.parseInt(reader.readLine());
                    String bezeichnung = reader.readLine();
                    int bestand = Integer.parseInt(reader.readLine());
                    float preis = Float.parseFloat(reader.readLine());
                    int packungsGrosse = Integer.parseInt(reader.readLine());
                    Massenartikel massenartikel = new Massenartikel(nummer, bezeichnung, bestand, preis, true, packungsGrosse);
                    artikelListe.add(massenartikel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        for (Artikel art : artikelListe) {
            if (art.getArtikelnummer() == artikelnummer) {
                art.setBestand(neuerBestand);
                artikelGefunden = true;
                break;
            }
        }

        if (!artikelGefunden) {
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("artikel.txt"))) {
            for (Artikel art : artikelListe) {
                if (art.isIstMassenartikel()) {
                    writer.println("massenartikel");
                    writer.println(art.getArtikelnummer());
                    writer.println(art.getBezeichnung());
                    writer.println(art.getBestand());
                    writer.println(art.getPreis());
                    writer.println(((Massenartikel) art).getPackungsGrosse());
                } else {
                    writer.println("artikel");
                    writer.println(art.getArtikelnummer());
                    writer.println(art.getBezeichnung());
                    writer.println(art.getBestand());
                    writer.println(art.getPreis());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Reads a line from the currently open reader.
     *
     * @return The read line.
     * @throws IOException If an I/O error occurs while reading the line.
     */
    private String liesZeile() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    /**
     * Writes a line to the currently open writer.
     *
     * @param daten The data to be written as a line.
     */
    private void schreibeZeile(String daten) {
        if (writer != null)
            writer.println(daten);
    }
}
