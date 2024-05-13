package Entities;

import java.util.ArrayList;
import java.util.List;
import Entities.Kunde;

public class Warenkorb    {

    private List<WarenkorbArtikel> WarenkorbListe = new ArrayList<>();


    public List<WarenkorbArtikel> getWarenkorbListe() {
        return WarenkorbListe;
    }

    public void setWarenkorbListe(List<WarenkorbArtikel> warenkorbListe) {
        WarenkorbListe = warenkorbListe;
    }

    public void addItem(WarenkorbArtikel artikel) {
        WarenkorbListe.add(artikel);
    }
    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (WarenkorbArtikel artikel : WarenkorbListe) {
            totalPrice += artikel.getAnzahl() * artikel.getArtikel().getPreis();
        }
        return totalPrice;
    }
    public void removeItem(WarenkorbArtikel artikel) {
        WarenkorbListe.remove(artikel);
    }
    @Override
    public String toString() {
        if (WarenkorbListe.isEmpty()) {
            return "Der Warenkorb ist leer.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s %-10s %-10s%n", "Artikel", "Anzahl", "Preis"));
            sb.append("------------------------------------\n");
            for (WarenkorbArtikel artikel : WarenkorbListe) {
                sb.append(String.format("%-20s %-10d %-15s%n", artikel.getArtikel().getBezeichnung(), artikel.getAnzahl(), (artikel.getArtikel().getPreis() + " * " + artikel.getAnzahl())));
            }
            sb.append("------------------------------------\n");
            sb.append(String.format("Gesamtpreis: %.2f%n", calculateTotalPrice()));
            return sb.toString();
        }
    }

    public void Warenkorbleeren() {
        WarenkorbListe.clear();
        System.out.println("Warenkorb geleert.");
    }
}
