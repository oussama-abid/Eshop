package domain;

import Entities.*;
import Exceptions.Artikelnichtgefunden;
import Exceptions.MassengutException;
import Exceptions.PackungsGrosseException;
import Persistence.FilePersistenceManager;
import ui.EShop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ArtikelVerwaltung {

    private List<Artikel> artikelListe= new ArrayList<>();
    private FilePersistenceManager fileManager= new FilePersistenceManager();
    private EShop shop;
    private static int letzteArtikelnummer = 1000;

    public Artikel ArtikelHinzufuegen(String Bezeichnung, int bestand, float Preis, boolean istMassenartikel, int packungsGrosse) throws MassengutException {
        int nummer = Eindeutigenummer();
        Artikel artikel;

        if(istMassenartikel){
            if (bestand % packungsGrosse == 0){
                artikel = new Massenartikel(nummer, Bezeichnung, bestand, Preis, true, packungsGrosse);
            }
            else {
                throw new MassengutException();
            }

        } else {
            artikel = new Artikel(nummer, Bezeichnung, bestand, Preis,false);
        }

        artikelListe.add(artikel);
        speicherArtikel(artikel);
        return artikel;
    }



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

    private void speicherArtikel(Artikel artikel) {
        try {
            fileManager.openForWriting("artikel.txt");
            fileManager.speichereArtikel(artikel);
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void EntferneArtikel(Artikel artikel) {

        artikelListe.remove(artikel);
    }

    public void BestandAendern(int artikelnummer, int newBestand,int neuePackungsGrosse) throws Exception {
        Artikel veraenderterArtikel = SucheArtikelPerID(artikelnummer);

        int aktualisierterBestand = veraenderterArtikel.getBestand() + newBestand;


        if (veraenderterArtikel instanceof Massenartikel) {
            Massenartikel massenartikel = (Massenartikel) veraenderterArtikel;

            if (massenartikel.getPackungsGrosse() != neuePackungsGrosse ) {
                if (aktualisierterBestand % neuePackungsGrosse != 0 ) {
                    throw new Exception("Bestand muss ein Vielfaches der Packungsgröße sein");
                }
                else {
                    PackungsGrosseanderen(veraenderterArtikel.getArtikelnummer(),neuePackungsGrosse);
                }
            }
           else {
                if (aktualisierterBestand % massenartikel.getPackungsGrosse() != 0 ) {
                    throw new Exception("Bestand muss ein Vielfaches der Packungsgröße sein");
                }
            }
        }
        if (aktualisierterBestand < 0) {
            throw new Exception("Bestand kann nicht negativ sein");
        }


        veraenderterArtikel.setBestand(aktualisierterBestand);
        fileManager.aendereArtikelInDatei(artikelnummer, aktualisierterBestand);
    }

    public Artikel SucheArtikelPerID(int artikelnummer) throws Artikelnichtgefunden {
        for (Artikel artikel : artikelListe) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                return artikel;
            }
        }
        throw new Artikelnichtgefunden(artikelnummer);
    }


    public List<Artikel> getArtikelListe() {
                                            //Um die Artikelliste auszugeben
        return artikelListe;
    }

    public void articlebestandanderen(Nutzer authuser) {
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> warenkorbListe = warenkorb.getWarenkorbListe();

        for (Artikel artikel : artikelListe) {
            for (WarenkorbArtikel item : warenkorbListe) {
                if (artikel.getArtikelnummer() == item.getArtikel().getArtikelnummer()) {
                    int anzahl = item.getAnzahl();

            //        if (artikel instanceof Massenartikel) {
            //            Massenartikel massenartikel = (Massenartikel) artikel;
            //            int packungsGrosse = massenartikel.getPackungsGrosse();
            //            int packungen = anzahl / packungsGrosse;  // Anzahl der gekauften Packungen

                        artikel.setBestand(artikel.getBestand() - anzahl); // Bestand um die gekaufte Anzahl reduzieren
                        fileManager.aendereArtikelInDatei(artikel.getArtikelnummer(),artikel.getBestand());

            //        } else {
            //            artikel.setBestand(artikel.getBestand() - anzahl);
            //        }
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


    public void PackungsGrosseanderen(int artikel, int neuePackungsGrosse) throws Artikelnichtgefunden,PackungsGrosseException {
        Artikel veraenderterArtikel = SucheArtikelPerID(artikel);
        Massenartikel massenartikel = (Massenartikel) veraenderterArtikel;
        massenartikel.setPackungsGrosse(neuePackungsGrosse);

    }

    public void checkpackunggrosse(int anzahl, Artikel artikel) throws PackungsGrosseException {
        Massenartikel massenArtikel = (Massenartikel) artikel;
        int packungsGrosse = massenArtikel.getPackungsGrosse();

        if (anzahl % packungsGrosse != 0) {
           throw new PackungsGrosseException(packungsGrosse);
        }
    }
}

