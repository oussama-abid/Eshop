package domain;



import java.util.ArrayList;
import java.util.List;
import Entities.*;




public class WarenkorbVerwaltung {


    public WarenkorbVerwaltung() {

    }

    public Warenkorb getWarenkorb(User authuser) {

            Kunde kunde = (Kunde) authuser;
            return kunde.getWarenkorb();

    }

    public void inWarenKorbLegen(Artikel artikel, int anzahl, User authuser) {
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

    private void aktualisiereArtikelImWarenkorb(Warenkorb warenkorb, Artikel artikel, int anzahl) {
        for (WarenkorbArtikel warenkorbArtikel : warenkorb.getWarenkorbListe()) {
            if (warenkorbArtikel.getArtikel().equals(artikel)) {
                int gesamtAnzahl = warenkorbArtikel.getAnzahl() + anzahl;
                if (artikel.getBestand() >= gesamtAnzahl) {
                    warenkorbArtikel.setAnzahl(gesamtAnzahl);
                } else {
                    System.out.println("Nicht genügend Bestand für " + artikel.getBezeichnung());
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
            System.out.println("Nicht genügend Bestand für " + artikel.getBezeichnung());
        }
    }



    public void ArtikelAusWarenkorbEntfernen(Artikel artikel, int anzahl, Warenkorb warenkorb) {
        for (WarenkorbArtikel warenkorbArtikel : new ArrayList<>(warenkorb.getWarenkorbListe())) {
            if (warenkorbArtikel.getArtikel().equals(artikel)) {
                // Wenn die Anzahl größer oder gleich der zu entfernenden Anzahl ist
                if (warenkorbArtikel.getAnzahl() >= anzahl) {
                    warenkorbArtikel.setAnzahl(warenkorbArtikel.getAnzahl() - anzahl);
                    // Wenn die Anzahl 0 ist, entferne den Artikel vollständig aus dem Warenkorb
                    if (warenkorbArtikel.getAnzahl() == 0) {
                        warenkorb.removeItem(warenkorbArtikel);
                    }
                } else {
                    // Wenn die Anzahl kleiner ist als die zu entfernende Anzahl, entferne den Artikel vollständig
                    warenkorb.removeItem(warenkorbArtikel);
                }
                // Sobald der Artikel gefunden und bearbeitet wurde, breche die Schleife ab
                break;
            }
        }
    }



}



