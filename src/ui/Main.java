package ui;

import domain.PersonenVerwaltung;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        PersonenVerwaltung user = new PersonenVerwaltung();
        user.registriereMitarbeiter(1, "admin", "admin", "admin");
        Cui cui = new Cui();

    }
}
