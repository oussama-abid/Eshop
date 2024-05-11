package domain;



import java.util.ArrayList;
import java.util.List;
import Entities.*;




public class WarenkorbVerwaltung {


    private List<Warenkorb> warenkorbList = new ArrayList<>();
    private User aktuellerUser;



    public Warenkorb getWarenkorb() {
        if (aktuellerUser == null) {
            System.out.println("No user is currently logged in.");
            return null;
        }
        for (Warenkorb warenkorb : warenkorbList) {
            if (warenkorb.getKunde().getUsernummer() == aktuellerUser.getUsernummer()) {
                return warenkorb;
            }
        }
        System.out.println("No cart found for the current user.");
        return null;
    }

    public void addWarenkorb(Warenkorb warenkorb) {
        warenkorbList.add(warenkorb);
    }

    public List<Warenkorb> getWarenkorbList() {
        return warenkorbList;
    }

    public void fuegeArtikelHinzu(Artikel artikel, int anzahl) {
        Warenkorb kundenWarenkorb = getWarenkorb();
        if (kundenWarenkorb == null) {
            kundenWarenkorb = new Warenkorb((Kunde) aktuellerUser);
            addWarenkorb(kundenWarenkorb);
            System.out.println("Neuer Warenkorb wurde erstellt für den Benutzer.");
        }
        WarenkorbArtikel neuerArtikel = new WarenkorbArtikel(artikel, anzahl);
        kundenWarenkorb.getWarenKorbArtikel().add(neuerArtikel);
    }

}


