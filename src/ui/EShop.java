package ui;

import Entities.User;
import domain.PersonenVerwaltung;

public class EShop {

    private PersonenVerwaltung PersonenVerwaltung;


    public EShop() {
        this.PersonenVerwaltung = new PersonenVerwaltung();
    }

    public User login(String benutzerkennung, String passwort) {
        return PersonenVerwaltung.login(benutzerkennung, passwort);
    }




}