package domain;

import Entities.Adresse;
import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.Nutzer;
import Exceptions.FalscheLoginDaten;
import Exceptions.NutzernameExistiertBereits;
import Exceptions.Plzexception;
import Persistence.FilePersistenceManager;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages users (customers and employees) in the Eshop system.
 * Provides methods for registration, login, data loading.
 * and throwing specific exceptions related to users management.
 */
public class PersonenVerwaltung {

    private List<Nutzer> nutzers = new ArrayList<>();

    private FilePersistenceManager fileManager = new FilePersistenceManager();
    private int letzteBenutzernummer = 3000;

    /**
     * Loads users from txt file into the app.
     *
     * @param filePath the file path containing users
     */
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

    /**
     * Registers a new employee.
     *
     * @param name            the employee's name
     * @param benutzerkennung the username
     * @param passwort        the password
     */
    public Mitarbeiter registriereMitarbeiter(String name, String benutzerkennung, String passwort) {
        int nummer = EindeutigeBenutzernummer();
        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzerkennung, hashPassword(passwort));
        nutzers.add(mitarbeiter);
        speichernutzer(mitarbeiter);
        return mitarbeiter;

    }

    /**
     * Registers a new customer.
     *
     * @param name            the customer's name
     * @param benutzerkennung the username
     * @param passwort        the password
     * @param straße          the street
     * @param stadt           the city
     * @param bundesland      the state
     * @param postleitzahl    the postal code
     * @param land            the country
     * @throws Plzexception if the postal code is invalid
     */
    public Kunde registriereKunde(String name, String benutzerkennung, String passwort, String straße, String stadt, String bundesland, int postleitzahl, String land) throws Plzexception {
        int nummer = EindeutigeBenutzernummer();
        validatePlz(postleitzahl);
        Adresse adresse = new Adresse(straße, stadt, bundesland, postleitzahl, land);
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, hashPassword(passwort), adresse);
        nutzers.add(kunde);
        speichernutzer(kunde);
        return kunde;

    }

    /**
     * Generates a unique user number for a new user.
     *
     * @return a unique user number
     */
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

    /**
     * Authenticates a user during login.
     *
     * @param benutzerkennung the username
     * @param passwort        the password
     * @return the authenticated user
     * @throws FalscheLoginDaten if the login credentials are incorrect
     */
    public Nutzer login(String benutzerkennung, String passwort) throws FalscheLoginDaten {
        for (Nutzer nutzer : nutzers) {
            if (benutzerkennung.equals(nutzer.getBenutzerkennung()) && verifyPassword(passwort, nutzer.getPasswort())) {
                return nutzer;
            }
        }
        throw new FalscheLoginDaten();
    }

    /**
     * Retrieves a list of customers.
     *
     * @return a list of customers
     */
    public List<Kunde> getKundenList() {
        List<Kunde> tempKundenListe = new ArrayList<>();
        for (Nutzer nutzer : nutzers) {
            if (nutzer instanceof Kunde) {
                tempKundenListe.add((Kunde) nutzer);
            }
        }
        return tempKundenListe;
    }

    /**
     * Retrieves a list of employees.
     *
     * @return a list of employees
     */
    public List<Mitarbeiter> getMitarbeiterlist() {
        List<Mitarbeiter> tempMitarbeiterListe = new ArrayList<>();
        for (Nutzer nutzer : nutzers) {
            if (nutzer instanceof Mitarbeiter) {
                tempMitarbeiterListe.add((Mitarbeiter) nutzer);
            }
        }
        return tempMitarbeiterListe;
    }

    /**
     * Checks if a username is unique.
     *
     * @param benutzerkennung the username to check
     * @return true if the username is unique, otherwise throws NutzernameExistiertBereits
     * @throws NutzernameExistiertBereits if the username already exists
     */
    public boolean checkUniqueUsername(String benutzerkennung) throws NutzernameExistiertBereits {
        for (Nutzer nutzer : nutzers) {
            if (nutzer.getBenutzerkennung().equals(benutzerkennung)) {
                throw new NutzernameExistiertBereits(benutzerkennung);
            }
        }
        return true;
    }

    /**
     * Validates a postal code.
     *
     * @param postleitzahl the postal code to validate
     * @return true if the postal code is valid, otherwise throws Plzexception
     * @throws Plzexception if the postal code is invalid
     */
    public boolean validatePlz(int postleitzahl) throws Plzexception {
        if (postleitzahl <= 0 || !String.valueOf(postleitzahl).matches("\\d+")) {
            throw new Plzexception();
        }
        return true;
    }

    /**
     * Hashes a password .
     *
     * @param password the password to hash
     * @return the hashed password as a hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies if the input password matches the stored password.
     *
     * @param inputPassword the input password to verify
     * @param storedHash    the stored hashed password
     * @return true if the input password matches the stored hash, false otherwise
     */
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(storedHash);
    }

    /**
     * Stores user data in the file .
     *
     * @param nutzer the user to store
     */
    private void speichernutzer(Nutzer nutzer) {
        try {
            fileManager.openForWriting("benutzers.txt");
            fileManager.speichereNutzer(nutzer);
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
