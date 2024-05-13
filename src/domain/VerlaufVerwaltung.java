package domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Entities.*;

public class VerlaufVerwaltung {


    private List<Event> eventList = new ArrayList<>();

    public VerlaufVerwaltung() {

    }

    public List<Event> getEventList() {


        return eventList;
    }


    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, Nutzer nutzer) {
        LocalDate date = LocalDate.now();
        Event event = new Event(operation,date, artikel, quantity, nutzer);
        eventList.add(event);
    }

    public void kundeEreignisfesthalten(String operation, Nutzer authuser) {
        LocalDate date = LocalDate.now();
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> WarenkorbListe = warenkorb.getWarenkorbListe();

            for (WarenkorbArtikel item : WarenkorbListe) {
                Event event = new Event(operation,date, item.getArtikel(), item.getAnzahl(),authuser);
                eventList.add(event);
            }
    }
}

