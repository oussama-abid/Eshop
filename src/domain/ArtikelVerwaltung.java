package domain;

import Entities.*;
import Exceptions.Artikelnichtgefunden;
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

    public Artikel ArtikelHinzufuegen(String Bezeichnung, int bestand, float Preis, boolean istMassenartikel, int packungsGrosse) {
        int nummer = Eindeutigenummer();
        Artikel artikel;

        if(istMassenartikel){
            artikel = new Massenartikel(nummer, Bezeichnung, bestand, Preis, true, packungsGrosse);
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


}

