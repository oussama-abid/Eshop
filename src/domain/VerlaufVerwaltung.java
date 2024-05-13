package domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Entities.*;

public class VerlaufVerwaltung
{

    private List<Event> eventList = new ArrayList<>();

    public VerlaufVerwaltung() {

    }

    public List<Event> getEventList() {


        return eventList;
    }


    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, Nutzer nutzer) {
        LocalDate date = LocalDate.now();
        System.out.println("Test");

        Event event = new Event(operation,date, artikel, quantity, nutzer);
        System.out.println("Test");

        eventList.add(event);
        System.out.println("Test");

    }

    public void kundeEreignisfesthalten(String operation, Nutzer authuser) {
        LocalDate date = LocalDate.now();
        System.out.println("Test");
        Kunde kunde = (Kunde) authuser;
        System.out.println("Test");
        Warenkorb warenkorb = kunde.getWarenkorb();
        System.out.println("Test");
        List<WarenkorbArtikel> WarenkorbListe = warenkorb.getWarenkorbListe();
        System.out.println("Test");


            for (WarenkorbArtikel item : WarenkorbListe) {
                Event event = new Event(operation,date, item.getArtikel(), item.getAnzahl(),authuser);
                eventList.add(event);
            }
    }
}

