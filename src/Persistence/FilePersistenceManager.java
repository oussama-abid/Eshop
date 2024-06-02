package Persistence;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Entities.*;


public class FilePersistenceManager implements PersistenceManager {
    private BufferedReader reader = null;
    private PrintWriter writer = null;

    @Override
    public void openForReading(String datei) throws IOException {
        reader = new BufferedReader(new FileReader(datei));
    }

    @Override
    public void openForWriting(String datei) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei, true)));  // true indicates append mode
    }

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



    public Artikel ladeArtikel() throws IOException {
        String artikelnummerString = liesZeile();
        if (artikelnummerString == null) {
            return null;
        }

        int artikelnummer = Integer.parseInt(artikelnummerString);
        String bezeichnung = liesZeile();
        int bestand = Integer.parseInt(liesZeile());
        float preis = Float.parseFloat(liesZeile());

        return new Artikel(artikelnummer, bezeichnung, bestand, preis);
    }

    public boolean speichereArtikel(Artikel artikel) {
        schreibeZeile(String.valueOf(artikel.getArtikelnummer()));
        schreibeZeile(artikel.getBezeichnung());
        schreibeZeile(String.valueOf(artikel.getBestand()));
        schreibeZeile(String.valueOf(artikel.getPreis()));

        return true;
    }




    public Event ladeEvent() throws IOException {
        String operation = liesZeile();
        if (operation == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(liesZeile()); // Assuming date is stored in ISO_LOCAL_DATE format
        Artikel article = ladeArtikel();
        int quantity = Integer.parseInt(liesZeile());
        Nutzer nutzer = ladeNutzer();

        return new Event(operation, date, article, quantity, nutzer);
    }

    public boolean speichereevent(Event event) {
        schreibeZeile(event.getOperation());
        schreibeZeile(event.getDate().toString());
        speichereArtikel(event.getArticle());
        schreibeZeile(String.valueOf(event.getQuantity()));
        speichereNutzer(event.getUser());

        return true;
    }










    private String liesZeile() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    private void schreibeZeile(String daten) {
        if (writer != null)
            writer.println(daten);
    }
}