package domain;

import java.util.ArrayList;
import java.util.List;


import Entities.Adresse;
import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.User;

public class PersonenVerwaltung {

    private List<User> users = new ArrayList<>();
    private  List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
    private int letzteBenutzernummer = 5000;

    public PersonenVerwaltung() {
    }

    public void registriereMitarbeiter( String name, String benutzerkennung, String passwort) {
        int nummer = EindeutigeBenutzernummer();
        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzerkennung, passwort);
        users.add(mitarbeiter);
        System.out.println(" Mitarbeiter registriert: "+" Benutzernummer: " + nummer + " Name: " + name);

    }

    public void registriereKunde(String name, String benutzerkennung, String passwort, String straße, String stadt, String bundesland, int postleitzahl, String land) {
        int nummer = EindeutigeBenutzernummer();
        Adresse adresse = new Adresse(straße, stadt, bundesland, postleitzahl, land);
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, passwort, adresse);
        users.add(kunde);
    }
    private int EindeutigeBenutzernummer() {
        return ++letzteBenutzernummer;
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
