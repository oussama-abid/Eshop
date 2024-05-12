package domain;

import java.util.ArrayList;
import java.util.List;


import Entities.Adresse;
import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.Nutzer;

public class PersonenVerwaltung {

    private List<Nutzer> nutzers = new ArrayList<>();
    private  List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
    private int letzteBenutzernummer = 5000;

    public PersonenVerwaltung() {
    }

    public void registriereMitarbeiter( String name, String benutzerkennung, String passwort) {
        int nummer = EindeutigeBenutzernummer();
        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzerkennung, passwort);
        nutzers.add(mitarbeiter);
        System.out.println(" Mitarbeiter registriert: "+" Benutzernummer: " + nummer + " Name: " + name);

    }

    public void registriereKunde(String name, String benutzerkennung, String passwort, String straße, String stadt, String bundesland, int postleitzahl, String land) {
        int nummer = EindeutigeBenutzernummer();
        Adresse adresse = new Adresse(straße, stadt, bundesland, postleitzahl, land);
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, passwort, adresse);
        nutzers.add(kunde);
    }
    private int EindeutigeBenutzernummer() {
        return ++letzteBenutzernummer;
    }

    public Nutzer login(String benutzerkennung, String passwort) {
        for (Nutzer nutzer : nutzers) {
            if (benutzerkennung.equals(nutzer.getBenutzerkennung()) && passwort.equals(nutzer.getPasswort())) {
                return nutzer;
            }
        }
        return null;
    }
    public List<Kunde> getKundenList() {

        for (Nutzer nutzer : nutzers) {
            if (nutzer instanceof Kunde) {
                kundenList.add((Kunde) nutzer);
            }
        }
        return kundenList;
    }

    public List<Mitarbeiter> getMitarbeiterlist() {

        for (Nutzer nutzer : nutzers) {
            if (nutzer instanceof Mitarbeiter) {
                mitarbeiterList.add((Mitarbeiter) nutzer);
            }
        }
        return mitarbeiterList;
    }
}
