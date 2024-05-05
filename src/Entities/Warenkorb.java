package Entities;

import java.util.ArrayList;
import java.util.List;
import Entities.Kunde;

public class Warenkorb    {

     private List<WarenkorbArtikel> WarenkorbListe = new ArrayList<>();
    private Kunde kunde;


    public Warenkorb(Kunde kunde) {
                                             //spezifisch für jeden Kunden einen Warenkorb
        WarenkorbListe = new ArrayList<>();   //eigene Liste für jeden Kunden
    }
    public List<WarenkorbArtikel> getWarenKorbArtikel(){
        return WarenkorbListe;
    }

    public void WarenkorbLeeren(){
        WarenkorbListe.clear();
    }

    public Kunde getKunde() {
        return kunde;
    }

    public int getGesamtAnzahl(){
       int GesamteAnzahl = 0;
        for (WarenkorbArtikel warenkorbArtikel : WarenkorbListe) {
            GesamteAnzahl += warenkorbArtikel.getAnzahl();
        }
        return GesamteAnzahl;
}

}
