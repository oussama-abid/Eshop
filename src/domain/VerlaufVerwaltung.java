package domain;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import Entities.*;
import Persistence.FilePersistenceManager;

public class VerlaufVerwaltung {


    private List<Event> eventList = new ArrayList<>();
    private FilePersistenceManager fileManager= new FilePersistenceManager();


    public VerlaufVerwaltung() {

    }

    public void loadevents(String file) {
        try {
            fileManager.openForReading(file);
            Event event;
            while ((event = fileManager.ladeEvent()) != null) {
                eventList.add(event);
            }
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Event> getEventList() {
        return sortEventsByDate(eventList);
    }

    public List<Event> sortEventsByDate(List<Event> eventList) {
        Collections.sort(eventList, (event1, event2) -> event2.getDate().compareTo(event1.getDate()));
        return eventList;
    }



    public List<Artikelhistory> getArticleQuantitiesPerDay() {
        List<Artikelhistory> dailyQuantities = new ArrayList<>();

        for (Event event : eventList) {
            LocalDate date = event.getDate();
            Artikel artikel = event.getArticle();
            int quantityChange = event.getOperation().equals("Auslagerung") ? -event.getQuantity() : event.getQuantity();

            // Find existing DailyArticleQuantity for the same date and artikel
            Artikelhistory existingEntry = null;
            for (Artikelhistory dailyQuantity : dailyQuantities) {
                if (dailyQuantity.getDate().equals(date) && dailyQuantity.getArticle().equals(artikel)) {
                    existingEntry = dailyQuantity;
                    break;
                }
            }

            // If found, update the total quantity; if not, create a new entry
            if (existingEntry != null) {
                existingEntry.addQuantity(quantityChange);
            } else {
                dailyQuantities.add(new Artikelhistory(date, artikel, quantityChange));
            }
        }

        return dailyQuantities;
    }


    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, Nutzer nutzer) {
        LocalDate date = LocalDate.now();
        Event event = new Event(operation,date, artikel, quantity, nutzer);
        eventList.add(event);
        speicherevent(event);
    }

    public void kundeEreignisfesthalten(String operation, Nutzer authuser) {
        LocalDate date = LocalDate.now();
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> WarenkorbListe = warenkorb.getWarenkorbListe();

            for (WarenkorbArtikel item : WarenkorbListe) {
                Event event = new Event(operation,date, item.getArtikel(), item.getAnzahl(),authuser);
                eventList.add(event);
                speicherevent(event);
            }
    }
    private void speicherevent(Event event) {
        try {
            fileManager.openForWriting("events.txt");
            fileManager.speichereevent(event);
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

