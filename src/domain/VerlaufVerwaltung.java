package domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Entities.Artikel;
import Entities.Event;
import Entities.User;

public class VerlaufVerwaltung
{

    private List<Event> eventList = new ArrayList<>();

    public VerlaufVerwaltung() {

    }

    public List<Event> getEventList() {


        return eventList;
    }


    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, User user) {
        LocalDate date = LocalDate.now();

        Event event = new Event(operation,date, artikel, quantity,user);

        eventList.add(event);
    }

}

