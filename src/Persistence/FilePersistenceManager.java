package Persistence;

import Entities.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
                return new Artikel(artikelnummer, bezeichnung, bestand, preis,false);
            } else {
                throw new IOException("Unbekannter Artikeltyp: " + artikelTyp);
            }
        } catch (NumberFormatException e) {
            throw new IOException("Fehler beim Parsen der Artikelinformationen: " + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new IOException("Fehler beim Lesen der Artikelinformationen: Eine erforderliche Zeile fehlt", e);
        }
    }


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

    public boolean aendereArtikelInDatei(int artikelnummer, int neuerBestand) {

        // Neue Artikelliste wird erstellt und wir lesen alle Artikel aus der .txt Datei ein.
        // Dann ändern wir die eingelesene Liste mit der neuesten Bestandsänderung.
        // Zum schluss überschrieben wir die temporäre Liste in die artikel.txt rein.

        List<Artikel> artikelListe = new ArrayList<>();
        boolean artikelGefunden = false;


        // Hier wird eingelesen
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

        // Artikel in der Liste ändern
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

        // Artikel zurück in die Datei schreiben
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


}