package domain;

import Entities.*;
import Exceptions.*;

import java.util.List;

/**
 * this class is used to  Manage user interface interactions with various management classes of the EShop.
 * Handles functionalities such as user management, article management,
 * event management, shopping cart operations...
 */
public class EShop {

    private PersonenVerwaltung PersonenVerwaltung;
    private ArtikelVerwaltung Produkte;
    private VerlaufVerwaltung VerlaufVerwaltung;
    private WarenkorbVerwaltung warenkorbVerwaltung;

    /**
     * Initializes the EShop with different management classes and loads EShop data from files.
     */
    public EShop() {
        this.PersonenVerwaltung = new PersonenVerwaltung();
        this.Produkte = new ArtikelVerwaltung();
        this.VerlaufVerwaltung = new VerlaufVerwaltung();
        this.warenkorbVerwaltung = new WarenkorbVerwaltung();

        // Load initial data from files
        PersonenVerwaltung.loadUsers("benutzers.txt");
        Produkte.loadartikel("artikel.txt");
        VerlaufVerwaltung.loadevents("events.txt");
    }

    /**
     * Logs in a user with the specified credentials.
     *
     * @param benutzerkennung the username
     * @param passwort        the password
     * @return the logged-in user
     * @throws FalscheLoginDaten if login credentials are incorrect
     */
    public Nutzer login(String benutzerkennung, String passwort) throws FalscheLoginDaten {
        return PersonenVerwaltung.login(benutzerkennung, passwort);
    }

    /**
     * Registers a new employee with the specified credentials.
     *
     * @param name            the employee's name
     * @param benutzerkennung the employee's username
     * @param passwort        the employee's password
     */
    public Mitarbeiter registriereMitarbeiter(String name, String benutzerkennung, String passwort) {
       return PersonenVerwaltung.registriereMitarbeiter(name, benutzerkennung, passwort);
    }

    /**
     * Registers a new customer with the specified credentials.
     *
     * @param name            the customer's name
     * @param benutzerkennung the customer's username
     * @param passwort        the customer's password
     * @param straße          the customer's street
     * @param stadt           the customer's city
     * @param bundesland      the customer's state
     * @param postleitzahl    the customer's postal code
     * @param land            the customer's country
     */
    public Kunde registriereKunde(String name, String benutzerkennung, String passwort, String straße, String stadt, String bundesland, int postleitzahl, String land) throws Plzexception {
        return PersonenVerwaltung.registriereKunde(name, benutzerkennung, passwort, straße, stadt, bundesland, postleitzahl, land);
    }

    /**
     * Retrieves a list of customers.
     *
     * @return a list of customers
     */
    public List<Kunde> getKundenList() {
        return PersonenVerwaltung.getKundenList();
    }

    /**
     * Retrieves a list of employees.
     *
     * @return a list of employees
     */
    public List<Mitarbeiter> getMitarbeiterlist() {
        return PersonenVerwaltung.getMitarbeiterlist();
    }


    /**
     * Changes the stock or packaging size of an article.
     *
     * @param artikelnummer     the article number
     * @param newBestand        the new stock quantity
     * @param neuePackungsGrosse the new packaging size
     * @throws Exception if an error occurs while updating the article stock
     */
    public void BestandAendern(int artikelnummer, int newBestand, int neuePackungsGrosse) throws Exception {
        Produkte.BestandAendern(artikelnummer, newBestand, neuePackungsGrosse);
    }

    /**
     * Adds a new article to the EShop inventory.
     *
     * @param Bezeichnung      the article name
     * @param bestand          the stock quantity
     * @param Preis            the article price per piece
     * @param istMassenartikel true if it's a mass article, false otherwise
     * @param packungsGrosse   the packaging size
     * @return the added article
     * @throws MassengutException if the article quantity and the packaging size are not compatible
     */
    public Artikel ArtikelHinzufuegen(String Bezeichnung, int bestand, float Preis, boolean istMassenartikel, int packungsGrosse) throws MassengutException {

        return Produkte.ArtikelHinzufuegen(Bezeichnung, bestand, Preis, istMassenartikel, packungsGrosse);
    }

    /**
     * Retrieves a list of all articles in the EShop.
     *
     * @return a list of articles
     */
    public List<Artikel> getArtikelListe() {
        return Produkte.getArtikelListe();
    }

    /**
     * Retrieves the list of events (operations) made on articles in the EShop.
     *
     * @return the list of events
     */
    public List<Event> ShopVerlaufAnzeigen() {
        return VerlaufVerwaltung.getEventList();
    }

    /**
     * Records an operation made by an employee on an article (e.g., einlagerung, auslagerung ).
     *
     * @param operation the operation type
     * @param artikel   the article
     * @param quantity  the quantity
     * @param nutzer    the user performing the operation
     */
    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, Nutzer nutzer) {
        VerlaufVerwaltung.Ereignisfesthalten(operation, artikel, quantity, nutzer);
    }

    /**
     * Records a purchase operation (auslagerung)made by a customer.
     *
     * @param operation the operation type ("auslagerung")
     * @param authuser  the authenticated user
     */
    public void kundeEreignisfesthalten(String operation, Nutzer authuser) {
        VerlaufVerwaltung.kundeEreignisfesthalten(operation, authuser);
    }

    /**
     * Finds an article by its ID.
     *
     * @param artikelnummer the article number
     * @return the found article
     * @throws Artikelnichtgefunden if the article is not found
     */
    public Artikel findeArtikelDurchID(int artikelnummer) throws Artikelnichtgefunden {
        return Produkte.SucheArtikelPerID(artikelnummer);
    }

    /**
     * Adds an article to the shopping cart.
     *
     * @param artikel   the article to add
     * @param anzahl    the quantity of the article
     * @param authuser  the authenticated user
     * @throws AnzahlException        if an invalid quantity is specified
     * @throws PackungsGrosseException if the packaging size is exceeded
     */
    public void inWarenKorbLegen(Artikel artikel, int anzahl, Nutzer authuser) throws AnzahlException, PackungsGrosseException {
        warenkorbVerwaltung.inWarenKorbLegen(artikel, anzahl, authuser);
    }

    /**
     * Retrieves the shopping cart of a customer.
     *
     * @param authuser the authenticated user
     * @return the shopping cart
     */
    public Warenkorb getWarenkorb(Nutzer authuser) {
        return warenkorbVerwaltung.getWarenkorb(authuser);
    }

    /**
     * Completes the purchase , changing article quantity after purchase , and saving the Auslagerung event
     *
     * @param authuser the authenticated user
     */
    public void kaufen(Nutzer authuser) {
        articlebestandanderen(authuser);
        kundeEreignisfesthalten("Auslagerung", authuser);
        warenkorbVerwaltung.kaufen(authuser);
    }

    /**
     * Empties the shopping cart of a customer.
     *
     * @param authuser the authenticated user
     */
    public void Warenkorbleeren(Nutzer authuser) {
        warenkorbVerwaltung.Warenkorbleeren(authuser);
    }

    /**
     * Updates the article stock after a purchase.
     *
     * @param authuser the authenticated user
     */
    public void articlebestandanderen(Nutzer authuser) {
        Produkte.articlebestandanderen(authuser);
    }

    /**
     * Checks if a username is unique.
     *
     * @param benutzerkennung the username to check
     * @return true if the username is unique, false otherwise
     * @throws NutzernameExistiertBereits if the username already exists
     */
    public boolean checkUniqueUsername(String benutzerkennung) throws NutzernameExistiertBereits {
        return PersonenVerwaltung.checkUniqueUsername(benutzerkennung);
    }

    /**
     * Changes the quantity of an article in the shopping cart.
     *
     * @param ArtikelNummer the article number
     * @param neueAnzahl    the new quantity
     * @param authuser      the authenticated user
     * @throws AnzahlException          if an invalid quantity is specified
     * @throws Artikelnamenichtgefunden if the article name is not found
     */
    public void artikelMengeaendern(int ArtikelNummer, int neueAnzahl, Nutzer authuser) throws AnzahlException, Artikelnamenichtgefunden {
        warenkorbVerwaltung.artikelMengeaendern(ArtikelNummer, neueAnzahl, authuser);
    }

    /**
     * Searches for articles by name.
     *
     * @param suchbegriff the search term
     * @return a list of articles matching the search term
     */
    public List<Artikel> suchemitname(String suchbegriff) {
        return Produkte.suchemitname(suchbegriff);
    }

    /**
     * Retrieves the history of articles.
     *
     * @return a list of Artikelhistory objects
     */
    public List<Artikelhistory> ShophistoryAnzeigen() {
        return VerlaufVerwaltung.getArticleQuantitiesPerDay();
    }

    /**
     * Checks if the packaging size of an article is valid.
     *
     * @param anzahl  the quantity
     * @param artikel the article
     * @throws PackungsGrosseException if the packaging size is invalid
     */
    public void checkpackunggrosse(int anzahl, Artikel artikel) throws PackungsGrosseException {
        Produkte.checkpackunggrosse(anzahl, artikel);
    }

    /**
     * Filters events based on a filter text.
     *
     * @param filterText the filter text
     * @return a list of filtered events
     */
    public List<Event> filterevents(String filterText) {
        return VerlaufVerwaltung.filterEvents(filterText);
    }
}
