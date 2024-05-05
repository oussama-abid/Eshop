package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.User;

public class PersonenVerwaltung {

    private List<User> users = new ArrayList<>();
    List<Kunde> kundenList = new ArrayList<>();
    List<Mitarbeiter> mitarbeiterList = new ArrayList<>();

    public PersonenVerwaltung() {
    }

    public void registriereMitarbeiter( String name, String benutzerkennung, String passwort) {
        int nummer = EindeutigeBenutzernummer();
        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzerkennung, passwort);
        users.add(mitarbeiter);
    }

    public void registriereKunde(String name, String benutzerkennung, String passwort, String adresse) {
        int nummer = EindeutigeBenutzernummer();
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, passwort, adresse);
        users.add(kunde);
    }
    private int EindeutigeBenutzernummer() {
        Random random = new Random();
        int newNummer;
        boolean unique = false;
        do {
            newNummer = random.nextInt();
            newNummer = Math.abs(newNummer);
            unique = true;
            for (User user : users) {
                if (user.getUsernummer() == newNummer) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);
        return newNummer;
    }

    public User login(String benutzerkennung, String passwort) {
        for (User user : users) {
            if (benutzerkennung.equals(user.getBenutzerkennung()) && passwort.equals(user.getPasswort())) {
                return user;
            }
        }
        return null;
    }
    public List<Kunde> getKundenList() {

        for (User user : users) {
            if (user instanceof Kunde) {
                kundenList.add((Kunde) user);
            }
        }
        return kundenList;
    }

    public List<Mitarbeiter> getMitarbeiterlist() {

        for (User user : users) {
            if (user instanceof Mitarbeiter) {
                mitarbeiterList.add((Mitarbeiter) user);
            }
        }
        return mitarbeiterList;
    }
}
