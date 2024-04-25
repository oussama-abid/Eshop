package ui;

import Entities.User;
import domain.PersonenVerwaltung;

public class EShop {

    private PersonenVerwaltung BenutzerManagement;


    public EShop() {
        this.BenutzerManagement = new PersonenVerwaltung();
    }

    public User login(String benutzerkennung, String passwort) {
        return BenutzerManagement.login(benutzerkennung, passwort);
    }

    // public Article addArticle() {
    //     articleManagement.addArticle();
    // }
}