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

            WarenkorbArtikel warenkorbArtikel = new WarenkorbArtikel(artikel, anzahl);

            warenkorb.addItem(warenkorbArtikel);


    }

}



