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

    public void ersteNutzer(){
        Mitarbeiter ersterMitarbeiter = new Mitarbeiter( letzteBenutzernummer,"admin", "admin", "admin");
        nutzers.add(ersterMitarbeiter);
        letzteBenutzernummer ++;
        Kunde ersterKunde = new Kunde(letzteBenutzernummer, "user", "user", "user", new Adresse("str","Bremen", "Bremen", 28217, "DE"));
        nutzers.add(ersterKunde);
        letzteBenutzernummer ++;

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
        System.out.println(" Kunde registriert: "+" Benutzernummer: " + nummer + " Name: " + name);
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
        // // macht eine neue temporäre liste um Kunden anzuzeigen
        List<Kunde> tempKundenListe = new ArrayList<>();
        for (Nutzer nutzer : nutzers) {
            if (nutzer instanceof Kunde) {
                tempKundenListe.add((Kunde) nutzer);
            }
        }
        return tempKundenListe;
    }


    public List<Mitarbeiter> getMitarbeiterlist() {
        // macht eine neue temporäre liste um mitarbeiter anzuzeigen
        List<Mitarbeiter> tempMitarbeiterListe = new ArrayList<>();
        for (Nutzer nutzer : nutzers) {
            if (nutzer instanceof Mitarbeiter) {
                tempMitarbeiterListe.add((Mitarbeiter) nutzer);
            }
        }
        return tempMitarbeiterListe;
    }

    public boolean checkUniqueUsername(String benutzerkennung) {
        for (Nutzer nutzer : nutzers) {
            if (nutzer.getBenutzerkennung().equals(benutzerkennung)) {
                return false;
            }
        }
        return true;
    }
}
