package domain;

import Entities.*;
import Persistence.FilePersistenceManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages operations and history related to articles , including loading, sorting, saving events,
 * and filtering operations based on specific criteria.
 */
public class VerlaufVerwaltung {

    private List<Event> eventList = new ArrayList<>();
    private FilePersistenceManager fileManager = new FilePersistenceManager();

    /**
     * Loads events from a txt file.
     *
     * @param file the file path to load events from
     */
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

    /**
     * Retrieves the list of events.
     *
     * @return the list of events
     */
    public List<Event> getEventList() {
        return eventList;
    }

    /**
     * Sorts a list of Artikelhistory objects by date in descending order.
     *
     * @param eventList the list of Artikelhistory objects to sort
     * @return the sorted list of Artikelhistory objects
     */
    public List<Artikelhistory> sortEventsByDate(List<Artikelhistory> eventList) {
        Collections.sort(eventList, (event1, event2) -> event2.getDate().compareTo(event1.getDate()));
        return eventList;
    }

    /**
     * Retrieves the daily quantities of articles .
     *
     * @return the list of Artikelhistory objects representing daily quantities
     */
    public List<Artikelhistory> getArticleQuantitiesPerDay() {
        List<Artikelhistory> dailyQuantities = new ArrayList<>();

        for (Event event : eventList) {
            LocalDate date = event.getDate();
            Artikel artikel = event.getArticle();
            int quantityChange;

            if ("Auslagerung".equals(event.getOperation())) {
                quantityChange = -event.getQuantity();
            } else if ("Einlagerung".equals(event.getOperation())) {
                quantityChange = event.getQuantity();
            } else {
                quantityChange = 0;
            }

            Artikelhistory existingEntry = null;

            for (Artikelhistory dailyQuantity : dailyQuantities) {
                if (dailyQuantity.getDate().equals(date) && dailyQuantity.getArticle().getArtikelnummer() == artikel.getArtikelnummer()) {
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

    /**
     * Records an event related to an operation on an article by a user.
     *
     * @param operation the operation performed
     * @param artikel   the article involved in the operation
     * @param quantity  the quantity of the article involved
     * @param nutzer    the user performing the operation
     */
    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, Nutzer nutzer) {
        LocalDate date = LocalDate.now();
        Event event = new Event(operation, date, artikel, quantity, nutzer);
        eventList.add(event);
        speicherevent(event);
    }

    /**
     * Records events related to operations of "auslagerung" on  every article after buying process .
     *
     * @param operation the operation performed
     * @param authuser  the authenticated user (customer)
     */
    public void kundeEreignisfesthalten(String operation, Nutzer authuser) {
        LocalDate date = LocalDate.now();
        Kunde kunde = (Kunde) authuser;
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> warenkorbListe = warenkorb.getWarenkorbListe();

        for (WarenkorbArtikel item : warenkorbListe) {
            Event event = new Event(operation, date, item.getArtikel(), item.getAnzahl(), authuser);
            eventList.add(event);
            speicherevent(event);
        }
    }

    /**
     * Saves an event to a file.
     *
     * @param event the event to save
     */
    private void speicherevent(Event event) {
        try {
            fileManager.openForWriting("events.txt");
            fileManager.speichereevent(event);
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters events based on article name or user.
     *
     * @param filterText the text to filter events by
     * @return the filtered list of events
     */
    public List<Event> filterEvents(String filterText) {
        List<Event> filteredList = new ArrayList<>();

        for (Event event : eventList) {
            if (event.getArticle().getBezeichnung().toLowerCase().contains(filterText.toLowerCase())
                    || event.getUser().getName().toLowerCase().contains(filterText.toLowerCase())) {
                filteredList.add(event);
            }
        }

        return filteredList;
    }
}
