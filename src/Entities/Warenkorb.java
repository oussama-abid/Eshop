package Entities;

import java.util.ArrayList;
import java.util.List;
import Entities.Kunde;

public class Warenkorb    {

    private List<WarenkorbArtikel> WarenkorbListe;


    public Warenkorb() {
        WarenkorbListe = new ArrayList<>();
    }

    public List<WarenkorbArtikel> getWarenkorbListe() {
        return WarenkorbListe;
    }

    public void setWarenkorbListe(List<WarenkorbArtikel> warenkorbListe) {
        WarenkorbListe = warenkorbListe;
    }

    public void addItem(WarenkorbArtikel artikel) {
        WarenkorbListe.add(artikel);
    }

    @Override
    public String toString() {
        if (WarenkorbListe.isEmpty()) {
            return "Der Warenkorb ist leer.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s %-10s%n", "Artikel", "Anzahl"));
            sb.append("--------------------\n");
            for (WarenkorbArtikel artikel : WarenkorbListe) {
                sb.append(String.format("%-20s %-10d%n", artikel.getArtikel().getBezeichnung(), artikel.getAnzahl()));
            }
            sb.append("--------------------\n");
            return sb.toString();
        }
    }

}
