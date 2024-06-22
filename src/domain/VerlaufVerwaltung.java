package domain;

import Entities.*;
import Persistence.FilePersistenceManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        return eventList;
    }

    public List<Artikelhistory> sortEventsByDate(List<Artikelhistory> eventList) {
        Collections.sort(eventList, (event1, event2) -> event2.getDate().compareTo(event1.getDate()));
        return eventList;
    }



    public List<Artikelhistory> getArticleQuantitiesPerDay() {
        List<Artikelhistory> dailyQuantities = new ArrayList<>();

        for (Event event : eventList) {
            LocalDate date = event.getDate();
            Artikel artikel = event.getArticle();
            int quantityChange ;
            if (event.getOperation().equals("Auslagerung")){
                quantityChange = -event.getQuantity();
            }
           else  if (event.getOperation().equals("Einlagerung")){
                 quantityChange = event.getQuantity();
            }
            else {
                 quantityChange = 0;
            }

            Artikelhistory existingEntry = null;


            for (Artikelhistory dailyQuantity : dailyQuantities) {
                if (dailyQuantity.getDate().equals(date) && dailyQuantity.getArticle().getArtikelnummer()==artikel.getArtikelnummer()) {
                    existingEntry = dailyQuantity;
                    break;
                }
            }


            if (existingEntry != null) {
                existingEntry.addQuantity(quantityChange);
            } else {
                dailyQuantities.add(new Artikelhistory(date, artikel, quantityChange));
            }
        }


        return sortEventsByDate(dailyQuantities);
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

