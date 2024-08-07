package ui;

import Entities.*;
import Exceptions.*;
import domain.EShop;

import java.time.LocalDate;
import java.util.*;

/**
 * The Cui class manages the Command-line User Interface (CUI) for an EShop application.
 * It handles user interaction, including registration, login, and menu navigation for customers and employees.
 */

public class Cui {
    private Scanner scanner;
    private EShop shop;


    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterlist = new ArrayList<>();
    private List<Event> ShopVerlauf = new ArrayList<>();
    private Map<LocalDate, Map<Artikel, Integer>> history = new HashMap<>();
    private List<Artikel> artikelListe = new ArrayList<>();

    private Nutzer authuser ;

    /**
     * Constructor for Cui class that initializes  an EShop instance and starts the login menu.
     *
     * @param shop The EShop instance to associate with the Cui interface.
     */

    public Cui(EShop shop) {
        scanner = new Scanner(System.in);
        this.shop = shop;
        this.authuser = null;
        loginMenue();
    }


    //-----------------------------------------------for both ---------------------------------------------------

    /**
     * Displays the login menu , Allows registration, login, or exiting the shop.
     */
    private void loginMenue() {
        boolean menueVerlassen = false;
        while (!menueVerlassen) {
            // Überprüfen, ob der Benutzer bereits angemeldet ist
            if (authuser == null) {
                System.out.print("Wollen Sie sich registrieren (R), einloggen (L) oder den Shop verlassen (Q)? : ");
                String input1 = scanner.nextLine();
                switch (input1) {
                    case "L":
                    case "l":
                        loginNutzer();
                        break;
                    case "R":
                    case "r":
                        registriereNutzer();
                        break;
                    case "Q":
                    case "q":
                        menueVerlassen = true; // Allow exit to break the loop
                        break;
                    default:
                        System.out.println("----------------------");
                }
            } else {
                // Der Benutzer ist bereits angemeldet, daher zeigen wir das Hauptmenü an
                menueVerlassen = true; // Beenden Sie die Schleife nach dem Anzeigen des Hauptmenüs
            }
        }
        scanner.close();
    }

    /**
     * Handles the user registration process, collecting user information and registering them in the EShop.
     */

    private void registriereNutzer()  {
        boolean istValideEingabe = false;
        Scanner scanner = new Scanner(System.in);

        while (!istValideEingabe) {
            System.out.println("Geben Sie Ihre Informationen ein");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            boolean uniquebenutzerkennung = false;
            String benutzerkennung = "";
            while (!uniquebenutzerkennung) {
                System.out.print("Benutzerkennung: ");
                benutzerkennung = scanner.nextLine();
                try {
                    shop.checkUniqueUsername(benutzerkennung);
                    uniquebenutzerkennung = true; // Username is unique
                } catch (NutzernameExistiertBereits e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.print("Passwort: ");
            String passwort = scanner.nextLine();

            System.out.println("Geben Sie Ihre Adresse:");
            System.out.print("Straße: ");
            String straße = scanner.nextLine();
            System.out.print("Stadt: ");
            String stadt = scanner.nextLine();
            System.out.print("Bundesland: ");
            String bundesland = scanner.nextLine();
            int postleitzahl = 0;
            boolean validPostleitzahl = false;
            while (!validPostleitzahl) {
                System.out.print("PLZ: ");

                if (scanner.hasNextInt()) {
                    postleitzahl = scanner.nextInt();
                    scanner.nextLine();
                    if (postleitzahl > 0) {
                        validPostleitzahl = true;
                    } else {
                        System.out.println("Fehler: Bitte geben Sie eine positive Zahl ein.");
                    }
                } else {
                    System.out.println("Bitte geben Sie eine gültige plz ein.");
                    scanner.nextLine();
                }
            }


            System.out.print("Land: ");
            String land = scanner.nextLine();

            try {
             Kunde kunde = shop.registriereKunde(name, benutzerkennung, passwort, straße, stadt, bundesland, postleitzahl, land);
             System.out.println("Kunde registriert: Benutzernummer: " + kunde.getNutzerNummer() + " Name: " + kunde.getName());
                System.out.println("Registrierung erfolgreich, Sie können sich nun einloggen!");
                istValideEingabe = true;
            } catch (Plzexception e) {

                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles the user login process.
     * save the authenticated user after successful login.
     */

    private void loginNutzer() {
        System.out.println("Geben Sie Ihre Login-Informationen ein:");
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();


        try {
            Nutzer nutzer = shop.login(benutzerkennung, passwort);
            authuser = nutzer; // Update authuser after successful login
            System.out.println("Login erfolgreich.");
            // Proceed to the main menu
            showMainMenu();
        } catch (FalscheLoginDaten e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Displays the main menu based on the authenticated user's type (Kunde or Mitarbeiter).
     * Redirects to specific menus for customers or employees.
     */

    private void showMainMenu() {
        // Display main menu options based on user's role
        if (authuser instanceof Kunde) {
            KundenMenu();
        } else if (authuser instanceof Mitarbeiter) {
            Mitarbeitermenu();
        }
    }

    /**
     * Displays the list of articles available in the shop.
     * For each article, display its details including article number, description, stock, and price.
     */

    private void zeigeArtikelliste() {
        artikelListe = shop.getArtikelListe();
        System.out.println("-------- Artikel: ----------");

        for (Artikel artikel : artikelListe) {
            System.out.println("Artikel Nummer: " + artikel.getArtikelnummer());
            System.out.println("Bezeichnung: " + artikel.getBezeichnung());
            System.out.println("Bestand: " + artikel.getBestand());
            System.out.println("Preis: " + artikel.getPreis());


            if (artikel instanceof Massenartikel) {
                Massenartikel massenArtikel = (Massenartikel) artikel;
                System.out.println("Packungsgröße: " + massenArtikel.getPackungsGrosse());
            }

            System.out.println("----------------------");
        }
    }



    //--------------------- Mitarbeiter functions --------------------------------------------------

    /**
     * Allows an employee to add a new article to the shop's inventory.
     */

    private void FuegeArtikelHinzu() {
        System.out.println("Artikelbeschreibung: ");
        System.out.print("Bezeichnung: ");
        String Bezeichnung = scanner.nextLine();

        int bestand = 0;
        boolean validBestand = false;
        while (!validBestand) {
            System.out.print("Bestand: ");
            if (scanner.hasNextInt()) {
                bestand = scanner.nextInt();
                scanner.nextLine();
                if (bestand > 0) {
                    validBestand = true;
                } else {
                    System.out.println("Fehler: Bitte geben Sie eine >0  Zahl ein.");
                }
            } else {
                System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                scanner.nextLine();
            }
        }

        float Preis = 0;
        boolean validPreis = false;
        while (!validPreis) {
            System.out.print("Preis: ");
            if (scanner.hasNextFloat()) {
                Preis = scanner.nextFloat();
                scanner.nextLine(); // newline character konsumieren
                validPreis = true;
            } else {
                System.out.println("Fehler: Bitte geben Sie einen gültigen Preis ein.");
                scanner.nextLine(); // falls keine gültige Float-Zahl eingegeben wurde, den Input konsumieren
            }
        }
        boolean istMassenartikel = false;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Ist der artikel ein Massenartikel? (ja/nein): ");
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "ja":
                    istMassenartikel = true;
                    validInput = true;
                    break;
                case "nein":
                    istMassenartikel = false;
                    validInput = true;
                    break;
                default:
                    System.out.println("Fehler: Bitte geben Sie 'ja' oder 'nein'.");
            }
        }

        int packungsGrosse = 0;
        if (istMassenartikel) {
            boolean validPackungsGrosse = false;
            while (!validPackungsGrosse) {
                System.out.print("Packungsgröße: ");
                if (scanner.hasNextInt()) {
                    packungsGrosse = scanner.nextInt();
                    if (packungsGrosse > 0) {
                        validPackungsGrosse = true;
                    } else if (packungsGrosse <= 0) {
                        System.out.println("Fehler: Bitte geben Sie eine Zahl größer als 0 ein.");
                    }
                } else {
                    System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                    scanner.nextLine(); // consume input if not an integer
                }
            }
        }
        try {
            Artikel art = shop.ArtikelHinzufuegen(Bezeichnung, bestand, Preis,istMassenartikel,packungsGrosse);// Username is unique
            System.out.println("Neuer Artikel hinzugefügt: " + art);
            shop.Ereignisfesthalten("neuer Artikel", art, art.getBestand(), authuser);
        } catch (MassengutException e) {
            System.out.println(e.getMessage());
        }




    }

    /**
     * Mitarbeiter menu to access different eshop parts.
     */

    private void Mitarbeitermenu (){
        boolean isValid = false;
        while (!isValid) {
            System.out.println(" 1. Artikelliste ausgeben");
            System.out.println(" 2. Artikel hinzufuegen");
            System.out.println(" 3. Artikel Bestand aendern");
            System.out.println(" 4. Kundenliste ausgeben");
            System.out.println(" 5. Mitarbeiterliste ausgeben");
            System.out.println(" 6. Mitarbeiter hinzufuegen");
            System.out.println(" 7. Shop Verlauf Ansehen");
            System.out.println(" 8. Shop history Ansehen");
            System.out.println(" 9. Logout");

            String input1 = scanner.nextLine();
            switch (input1) {
                case "1":
                    zeigeArtikelliste();
                    break;
                case "2":
                    FuegeArtikelHinzu();
                    break;
                case "3":
                    aendereBestand();
                    break;
                case "4":
                    ZeigeKundenListe();
                    break;
                case "5":
                    ZeigeMitarbeiterListe();
                    break;
                case "6":
                    registriereMitarbeiter();
                    break;
                case "7":
                    ShopVerlaufAnzeigen();
                    break;
                case "8":
                    ShophistoryAnzeigen();
                    break;
                case "9":
                    isValid = true; // Allow exit to break the loop
                    authuser = null;
                    break;
            }
        }
    }
    /**
     * Displays the shop history for every article
     */
    private void ShophistoryAnzeigen() {
        List<Artikelhistory> quantities = shop.ShophistoryAnzeigen();
        for (Artikelhistory dailyQuantity : quantities) {
            System.out.println(dailyQuantity);
        }
    }
    /**
     * Allow an employee to register a new employee
     */
    private void registriereMitarbeiter() {
        System.out.println("Geben sie die Informationen ein");
        System.out.print("Name: ");

        String name = scanner.nextLine();
        boolean uniquebenutzerkennung = false;
        String benutzerkennung = "";
        while (!uniquebenutzerkennung) {
            System.out.print("Benutzerkennung: ");
            benutzerkennung = scanner.nextLine();
            try {
                shop.checkUniqueUsername(benutzerkennung);
                uniquebenutzerkennung = true; // Username is unique
            } catch (NutzernameExistiertBereits e) {
                System.out.println(e);
            }
        }
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        Mitarbeiter mitarbeiter = shop.registriereMitarbeiter(name, benutzerkennung, passwort);
        System.out.println("Mitarbeiter registriert: Benutzernummer: " + mitarbeiter.getNutzerNummer() + " Name: " + mitarbeiter.getName());


    }
    /**
     * Displays the employess list
     */
    private void ZeigeMitarbeiterListe() {
        mitarbeiterlist = shop.getMitarbeiterlist();
        for (Mitarbeiter mitarbeiter : mitarbeiterlist) {
            System.out.println(mitarbeiter);
        }
    }
    /**
     * Displays the customers list
     */
    private void ZeigeKundenListe() {
        kundenList = shop.getKundenList();
        for (Kunde kunde : kundenList) {
            System.out.println(kunde);
        }
    }
    /**
     * Displays the operations list for every article
     */
    private void ShopVerlaufAnzeigen() {
        ShopVerlauf = shop.ShopVerlaufAnzeigen();
        if (ShopVerlauf.size() == 0) {
            System.out.println("Keine Ereignisse bisher aufgezeichnet.");
        } else {
            for (Event event : ShopVerlauf) {
                System.out.println(event);
            }
        }
    }
    /**
     * allow mitarbeiter to edit stock of an article
     */
    private void aendereBestand() {
        int artikelnummer = 0;

        boolean validInput = false;
        while (!validInput) {
            System.out.print("Geben Sie die Artikelnummer ein: ");
            try {
                artikelnummer = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
            }
        }

        try {
            Artikel artikel = shop.findeArtikelDurchID(artikelnummer);
                System.out.print("Geben Sie die Anzahl der hinzuzufügenden/abzuziehenden Artikel ein: ");
                int newBestand = 0;
                boolean validBestand = false;
                while (!validBestand) {
                    System.out.print("Bestand: ");
                    if (scanner.hasNextInt()) {
                        newBestand = scanner.nextInt();
                        scanner.nextLine();
                        validBestand = true;
                    } else {
                        System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                        scanner.next();
                    }
                }
            int neuePackungsGrosse = 0;

            if (artikel instanceof Massenartikel) {
                neuePackungsGrosse = ((Massenartikel) artikel).getPackungsGrosse() ;
                System.out.print("Möchten Sie die Packungsgröße ändern? (ja/nein): ");
                String packungsGrosseAntwort = scanner.nextLine().trim().toLowerCase();
                if (packungsGrosseAntwort.equals("ja")) {
                    System.out.print("Geben Sie die neue Packungsgröße ein: ");
                    boolean validPackungsGrosse = false;
                    while (!validPackungsGrosse) {
                        if (scanner.hasNextInt()) {
                            neuePackungsGrosse = scanner.nextInt();
                            scanner.nextLine();  // consume the newline
                            if (neuePackungsGrosse > 0) {
                                validPackungsGrosse = true;
                            } else {
                                System.out.println("Fehler: Bitte geben Sie eine Zahl größer als 0 ein.");
                            }
                        } else {
                            System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                            scanner.next();  // consume invalid input
                        }
                    }

                }
            }

                try {

                    shop.BestandAendern(artikelnummer, newBestand,neuePackungsGrosse);
                    System.out.println("Lagerbestand für Artikel " + artikelnummer + " aktualisiert. Neuer Bestand: " + artikel.getBestand());
                    String ereignisTyp = newBestand >= 0 ? "Einlagerung" : "Auslagerung";
                    shop.Ereignisfesthalten(ereignisTyp, artikel, Math.abs(newBestand), authuser);

                } catch (Exception e) {
                    System.out.println("Fehler: " + e.getMessage());
                }

        } catch (Artikelnichtgefunden e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }




//--------------------- Kunde functions --------------------------------------------------

    /**
     * Displays the customers menu
     */

    private void KundenMenu() {
        boolean isValid = false;
        while (!isValid) {

            System.out.println("--------------E-SHOP AES-------------------");
            System.out.println(" 1. Artikelliste ausgeben");
            System.out.println(" 2. Artikel suchen");
            System.out.println(" 3. Artikel zum Warenkorb hinzufuegen");
            System.out.println(" 4. Warenkorb ansehen");
            System.out.println(" 5. Logout");

            String input1 = scanner.nextLine();
            switch (input1) {
                case "1":
                    zeigeArtikelliste();
                    break;
                case "2":
                    SucheArtikelMitName();
                    break;
                case "3":
                    HinzufuegenZumWarenkorb();
                    break;
                case "4":

                        WarenkorbAnsehen();

                    break;
                case "5":
                    isValid = true;
                    authuser = null;
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Allow customer to add article to his cart
     */
    private void HinzufuegenZumWarenkorb() {
        System.out.print("Geben Sie die Artikelnummer des zu hinzufügenden Artikels ein: ");
        int artikelnummer;
        try {
            artikelnummer = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Bitte geben Sie eine gültige Artikelnummer ein.");
            return;
        }
        try {
            Artikel artikel = shop.findeArtikelDurchID(artikelnummer);
            System.out.print("Geben Sie die Anzahl ein: ");
            int anzahl = 0;
            boolean validBestand = false;
            while (!validBestand) {
                System.out.print("Anzahl: ");
                if (scanner.hasNextInt()) {
                    anzahl = scanner.nextInt();
                    scanner.nextLine();
                    if (anzahl > 0) {
                        if (artikel instanceof Massenartikel) {

                            try {
                                shop.checkpackunggrosse(anzahl,artikel);
                                validBestand = true;
                            }
                            catch (PackungsGrosseException e){
                                System.out.println("Fehler: Bitte geben Sie eine >0 Zahl ein.");
                            }
                        } else {
                            validBestand = true;
                        }
                    } else {
                        System.out.println("Fehler: Bitte geben Sie eine >0 Zahl ein.");
                    }
                } else {
                    System.out.println("Bitte geben Sie eine gültige Anzahl ein.");
                    scanner.nextLine();
                }
            }

            try {
                shop.inWarenKorbLegen(artikel, anzahl, authuser);
            } catch (AnzahlException e) {
                System.out.println(e.getMessage());
            }
            catch (PackungsGrosseException e) {
                System.out.println(e.getMessage());
            }

        } catch (Artikelnichtgefunden e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Artikel erfolgreich hinzugefügt");
    }


    /**
     * Displays a menu to manage the cart
     */

    private void WarenkorbAnsehen() {
        Warenkorb warenkorb = shop.getWarenkorb(authuser);
        System.out.println(warenkorb);
        System.out.println("-----------------------------------------------------");

        if(!warenkorb.getWarenkorbListe().isEmpty()){
            boolean isValidInput = false;
            while (!isValidInput) {
                System.out.println("Was möchten Sie machen?");
                System.out.println(" 1. Artikel Menge ändern");
                System.out.println(" 2. Warenkorb leeren");
                System.out.println(" 3. kaufen");
                System.out.println(" 4. Zur HomePage zurück");

                String input1 = scanner.nextLine();
                switch (input1) {
                    case "1":
                        ArtikelMengeändern();
                        isValidInput = true;
                        break;
                    case "2":
                        Warenkorbleeren();
                        isValidInput = true;
                        break;
                    case "3":
                        kaufen();
                        isValidInput = true;
                        break;
                    case "4":
                        isValidInput = true;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe. Bitte geben Sie eine Nummer zwischen 1 und 4 ein.");
                        break;
                }
            }

        }

    }

    /**
     * make the cart empty
     */

    private void Warenkorbleeren() {
        shop.Warenkorbleeren(authuser);
    }

    /**
     * allow customer to continue the purchase process
     */

    private void kaufen() {
        Warenkorb warenkorb = shop.getWarenkorb(authuser);

        if (warenkorb.getWarenkorbListe().isEmpty()) {
            System.out.println("Ihr Warenkorb ist leer. Bitte fügen Sie Artikel hinzu, bevor Sie fortfahren.");
            return;
        }

        shop.kaufen(authuser);

    }

    /**
     * allow customer to search artikel with name
     */

    public void SucheArtikelMitName() {
        //eine extra Funktion um eine Suchmaschiene zu simmulieren die auch ungenaue suche erlaubt
        System.out.print("Geben Sie den Suchbegriff oder Artikelnummer ein: ");
        String suchbegriff = scanner.nextLine().toLowerCase();

        List<Artikel> gefundeneArtikel = new ArrayList<>();


        gefundeneArtikel=shop.suchemitname(suchbegriff);

        if (!gefundeneArtikel.isEmpty()) {
            System.out.println("Gefundene Artikel:");
            for (Artikel artikel : gefundeneArtikel) {
                System.out.println("Artikelnummer: " + artikel.getArtikelnummer());
                System.out.println("Bezeichnung: " + artikel.getBezeichnung());
                System.out.println("Bestand: " + artikel.getBestand());
                System.out.println("Preis: " + artikel.getPreis());
                System.out.println("-------------------");
            }
        }else {
            System.out.println("Keine Artikel gefunden, die mit \"" + suchbegriff + "\" beginnen.");
        }



    }
    /**
     * allow customer to change quantity of added article in cart
     */

    private void ArtikelMengeändern() {
        System.out.print("Geben Sie die Artikelnummer des zu ändernden Artikels ein: ");
        int artikelNummer = 0;
        boolean validInput = false;
        while (!validInput) {
            if (scanner.hasNextInt()) {
                artikelNummer = scanner.nextInt();
                validInput = true;
            } else {
                System.out.println("Fehler: Bitte geben Sie eine gültige Artikelnummer ein.");
                scanner.next();
            }
        }
        scanner.nextLine(); // Consume the newline character

        System.out.print("Geben Sie die neue Anzahl ein: ");
        int neueAnzahl = 0;
        validInput = false;
        while (!validInput) {
            if (scanner.hasNextInt()) {
                neueAnzahl = scanner.nextInt();
                validInput = true;
            } else {
                System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                scanner.next();
            }
        }
        scanner.nextLine(); // Consume the newline character

        try {
            if (neueAnzahl >= 0) {
                shop.artikelMengeaendern(artikelNummer, neueAnzahl, authuser);
                System.out.println("Anzahl erfolgreich geändert.");
            } else {
                System.out.println("Die Anzahl muss eine nicht-negative Zahl sein.");
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Ändern der Anzahl: " + e.getMessage());
        }
    }


}





