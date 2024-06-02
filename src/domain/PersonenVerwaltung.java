package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import Entities.Adresse;
import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.Nutzer;
import Exceptions.NutzernameExistiertBereits;
import Exceptions.Plzexception;
import Persistence.FilePersistenceManager;

public class PersonenVerwaltung {

    private List<Nutzer> nutzers = new ArrayList<>();
    private  List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterList = new ArrayList<>();

    private FilePersistenceManager fileManager= new FilePersistenceManager();



    private int letzteBenutzernummer = 5000;

    public PersonenVerwaltung() {

    }

    public void loadUsers(String filePath) {
        try {
            fileManager.openForReading(filePath);
            Nutzer nutzer;
            while ((nutzer = fileManager.ladeNutzer()) != null) {
                nutzers.add(nutzer);
            }
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void registriereMitarbeiter(String name, String benutzerkennung, String passwort) {
        int nummer = EindeutigeBenutzernummer();
        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzerkennung, passwort);
        nutzers.add(mitarbeiter);
       speichernutzer(mitarbeiter);
        System.out.println("Mitarbeiter registriert: Benutzernummer: " + nummer + " Name: " + name);
    }

    private void speichernutzer(Nutzer nutzer) {
        try {
            fileManager.openForWriting("benutzers.txt");
            fileManager.speichereNutzer(nutzer);
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registriereKunde(String name, String benutzerkennung, String passwort, String straße, String stadt, String bundesland, int postleitzahl, String land) {
        int nummer = EindeutigeBenutzernummer();
        Adresse adresse = new Adresse(straße, stadt, bundesland, postleitzahl, land);
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, passwort, adresse);
        nutzers.add(kunde);
        speichernutzer(kunde);
        System.out.println("Kunde registriert: Benutzernummer: " + nummer + " Name: " + name);
    }

    private int EindeutigeBenutzernummer() {
        int lastNummer = letzteBenutzernummer;

        for (Nutzer nutzer : nutzers) {
            int nummer = nutzer.getNutzerNummer();
            if (nummer > lastNummer) {
                lastNummer = nummer;
            }
        }

        return lastNummer + 1;
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

    public boolean checkUniqueUsername(String benutzerkennung) throws NutzernameExistiertBereits {
        for (Nutzer nutzer : nutzers) {
            if (nutzer.getBenutzerkennung().equals(benutzerkennung)) {
                throw new NutzernameExistiertBereits();
            }
        }
        return true;
    }


    public boolean validatePlz(int postleitzahl)  throws Plzexception {
        if (postleitzahl <= 0 || !String.valueOf(postleitzahl).matches("\\d+")) {
            throw new Plzexception();
        }
        return true;
    }
}
