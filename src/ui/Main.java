package ui;

import domain.PersonenVerwaltung;
import Entities.User;
import java.util.ArrayList;
import java.util.List;

public class  Main {
    public static void main(String[] args) {
        PersonenVerwaltung user = new PersonenVerwaltung();
        user.registriereMitarbeiter( "admin", "admin", "admin");
        user.registriereKunde( "user", "user", "user", "address");

        Cui cui = new Cui(user);

    }
}
