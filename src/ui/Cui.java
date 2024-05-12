package ui;

import Entities.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cui {
    private Scanner scanner;
    private EShop shop;


    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterlist = new ArrayList<>();
    private List<Event> ShopVerlauf = new ArrayList<>();
    private List<Artikel> artikelListe = new ArrayList<>();

    private Nutzer authuser ;

    public Cui(EShop shop) {
        scanner = new Scanner(System.in);
        this.shop = shop; // Use the passed EShop object
        this.authuser = null;
        loginMenue();
    }


    //-----------------------------------------------for both ---------------------------------------------------


    private void loginMenue() {
        boolean menueVerlassen = false;
        while (!menueVerlassen) {
            // Überprüfen, ob der Benutzer bereits angemeldet ist
            if (authuser == null) {
                System.out.print("Wollen Sie sich registrieren (R) oder einloggen (L)? : ");
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
                    case "exit":
                        menueVerlassen = true; // Allow exit to break the loop
                        break;
                    default:
                        System.out.println("----------------------");
                }
            } else {
                // Der Benutzer ist bereits angemeldet, daher zeigen wir das Hauptmenü an
                showMainMenu();
                menueVerlassen = true; // Beenden Sie die Schleife nach dem Anzeigen des Hauptmenüs
            }
        }
        scanner.close();
    }



    private void registriereNutzer() {
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
                if (shop.checkUniqueUsername(benutzerkennung)) {
                    uniquebenutzerkennung = true;
                } else {
                    System.out.println("Benutzerkennung ist bereits vergeben.");
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

            int postleitzahl = 0;      // Wie Eyup das mit anderen Cui-Sachen auch gemacht hat
            boolean validPostleitzahl = false;
            while (!validPostleitzahl) {
                System.out.print("PLZ: ");
                if (scanner.hasNextInt()) {
                    postleitzahl = scanner.nextInt();
                    scanner.nextLine(); // Verbrauche das Zeilenendezeichen
                    validPostleitzahl = true;
                } else {
                    System.out.println("Fehler: Bitte geben Sie eine ganze Zahl für die PLZ ein.");
                    scanner.next(); // Verbrauche ungültige Eingabe
                }
            }

            System.out.print("Land: ");
            String land = scanner.nextLine();

            shop.registriereKunde(name, benutzerkennung, passwort, straße, stadt, bundesland, postleitzahl, land);
            System.out.println("Registrierung erfolgreich, Sie können sich nun einloggen!");
            istValideEingabe = true; // Falls alle Eingabe OK, Schleife beenden
        }
    }


    private void loginNutzer() {
        System.out.println("Geben Sie Ihre Login-Informationen ein:");
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        Nutzer nutzer = shop.login(benutzerkennung, passwort);

        if (nutzer != null) {
            authuser = nutzer; // Update authuser after successful login
            System.out.println("Login erfolgreich.");
            // Proceed to the main menu
            showMainMenu();
        } else {
            System.out.println("Falscher Benutzername oder Passwort.");
            loginMenue(); // Prompt user to log in again
        }
    }

    private void showMainMenu() {
        // Display main menu options based on user's role
        if (authuser instanceof Kunde) {
            KundenMenu();
        } else if (authuser instanceof Mitarbeiter) {
            Mitarbeitermenu();
        }
    }
    private void showeshop() {
        artikelListe = shop.getArtikelListe();
        System.out.println("-------- Artikel: ----------");

        for (Artikel artikel : artikelListe) {
            System.out.println("Artikel Nummer: " + artikel.getArtikelnummer());
            System.out.println("Bezeichnung: " + artikel.getBezeichnung());
            System.out.println("Bestand: " + artikel.getBestand());
            System.out.println("Preis: " + artikel.getPreis());
            System.out.println("----------------------");
        }
        if (authuser instanceof Kunde) {
            KundenMenu();
        } else {
            Mitarbeitermenu();
        }
    }


    //--------------------- Mitarbeiter functions --------------------------------------------------


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
                if (bestand > 0) {
                    validBestand = true;
                } else {
                    System.out.println("Fehler: Bitte geben Sie eine >0  Zahl ein.");
                }
            } else {
                System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                scanner.next();
            }
        }

        float Preis = 0;
        boolean validPreis = false;
        while (!validPreis) {
            System.out.print("Preis: ");
            if (scanner.hasNextFloat()) {
                Preis = scanner.nextFloat();
                validPreis = true;
            } else {
                System.out.println("Fehler: Bitte geben Sie einen gültigen Preis ein.");
                scanner.next();
            }
        }


        Artikel art = new Artikel(Bezeichnung, bestand, Preis);
        shop.ArtikelHinzufuegen(art);
        shop.Ereignisfesthalten("neuer Artikel",art,art.getBestand(), authuser);
        System.out.println("Artikel wurde hinzugefügt");


    }

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
            System.out.println(" 8. Logout");

            String input1 = scanner.nextLine();
            switch (input1) {
                case "1":
                    showeshop();
                    isValid = true;
                    break;
                case "2":
                    FuegeArtikelHinzu();
                    isValid = true;
                    break;
                case "3":
                    aendereBestand();
                    isValid = true;
                    break;
                case "4":
                    ZeigeKundenListe();
                isValid = true;
                    break;
                case "5":
                    ZeigeMitarbeiterListe();
                    isValid = true;
                    break;
                case "6":
                    registriereMitarbeiter();
                    isValid = true;
                    break;
                case "7":
                    ShopVerlaufAnzeigen();
                    isValid = true;
                    break;
                case "8":
                    isValid = true; // Allow exit to break the loop
                    authuser = null;
                    break;
                default:
                    System.out.println("Was möchten sie machen?");

            }
        }


    }

    private void registriereMitarbeiter() {
        System.out.println("Geben sie die Informationen ein");


        System.out.print("Name: ");
        String name = scanner.nextLine();
        boolean uniquebenutzerkennung = false;
        String benutzerkennung = "";
        while (!uniquebenutzerkennung) {
            System.out.print("Benutzerkennung: ");
            benutzerkennung = scanner.nextLine();
            if (shop.checkUniqueUsername(benutzerkennung)) {
                uniquebenutzerkennung = true;
            } else {
                System.out.println("Benutzerkennung ist bereits vergeben.");
            }
        }
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        shop.registriereMitarbeiter(name, benutzerkennung, passwort);
        System.out.println("Mitarbeiter  wird erfolgreich registriert");
        Mitarbeitermenu();

    }

    private void ZeigeMitarbeiterListe() {
        mitarbeiterlist = shop.getMitarbeiterlist();
        for (Mitarbeiter mitarbeiter : mitarbeiterlist) {
            System.out.println(mitarbeiter);
        }
        Mitarbeitermenu ();
    }

    private void ZeigeKundenListe() {
        kundenList = shop.getKundenList();
        for (Kunde kunde : kundenList) {
            System.out.println(kunde);
        }
        Mitarbeitermenu ();
    }

    private void ShopVerlaufAnzeigen() {
        ShopVerlauf = shop.ShopVerlaufAnzeigen();
        if (ShopVerlauf.size() == 0) {
            System.out.println("Keine Ereignisse bisher aufgezeichnet.");
        } else {
            for (Event event : ShopVerlauf) {
                System.out.println(event);
            }
        }
        Mitarbeitermenu();
    }


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

        Artikel artikel = shop.findeArtikelDurchID(artikelnummer);
        if (artikel == null) {
            System.out.println("Artikel nicht gefunden.");
            Mitarbeitermenu();
        } else {
            System.out.print("Wie viele Artikel möchten Sie hinzufügen? : ");
            int newBestand = 0;
            boolean validBestand = false;
            while (!validBestand) {
                System.out.print("Bestand: ");
                if (scanner.hasNextInt()) {
                    newBestand = scanner.nextInt();
                    if (newBestand > 0) {
                        validBestand = true;
                    } else {
                        System.out.println("Fehler: Bitte geben Sie eine Zahl größer als 0 ein.");
                    }
                } else {
                    System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                    scanner.next();
                }
            }
            shop.BestandAendern(artikelnummer, newBestand);
            System.out.println("Lagerbestand für Artikel " + artikelnummer + " auf " + artikel.getBestand() + " aktualisiert.");
            shop.Ereignisfesthalten("Einlagerung", artikel, newBestand, authuser);
            Mitarbeitermenu();

        }


    }







//--------------------- Kunde functions --------------------------------------------------


    private void KundenMenu() {
        boolean isValid = false;
        while (!isValid) {
            System.out.println(" 1. Artikelliste ausgeben");
            System.out.println(" 2. Artikel suchen");
            System.out.println(" 3. Artikel zum Warenkorb hinzufuegen");
            System.out.println(" 4. Warenkorb ansehen");
            System.out.println(" 5. Logout");

            String input1 = scanner.nextLine();
            switch (input1) {
                case "1":
                    showeshop();
                    isValid = true;
                    break;
                case "2":
                    SucheArtikelMitName();
                    isValid = true;
                    break;
                case "3":
                    HinzufuegenZumWarenkorb();
                    isValid = true;
                    break;
                case "4":
                    if (authuser != null) {
                        WarenkorbAnsehen();
                    }
                    isValid = true;
                    break;
                case "5":
                        //WarenkorBearbeiten();
                    isValid = true;
                    break;
                case "6":
                    isValid = true; // Allow exit to break the loop
                    authuser = null;
                    break;
                default:
                    System.out.println("Was möchten sie machen?");
            }
        }
    }



    private void HinzufuegenZumWarenkorb() {
        System.out.print("Geben Sie die Artikelnummer des zu hinzufügenden Artikels ein: ");
        int artikelnummer = 0;
        try {
            artikelnummer = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Bitte geben Sie eine gültige Artikelnummer ein.");
            return;
        }
        Artikel artikel = shop.findeArtikelDurchID(artikelnummer);
        if (artikel != null) {
            System.out.print("Geben Sie die Anzahl ein: ");

            int anzahl = 0;
            boolean validBestand = false;
            while (!validBestand) {
                System.out.print("Anzahl: ");
                if (scanner.hasNextInt()) {
                    anzahl = scanner.nextInt();
                    if (anzahl > 0) {
                        validBestand = true;
                    } else {
                        System.out.println("Fehler: Bitte geben Sie eine >0  Zahl ein.");
                    }
                } else {
                    System.out.println("Bitte geben Sie eine gültige Anzahl ein.");
                    scanner.next();
                }
            }


            try {
                shop.inWarenKorbLegen(artikel, anzahl, authuser);

            } catch (Exception e) {
                System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            }
            KundenMenu();

        }
        else {
            System.out.println("Artikel nicht gefunden.");
            KundenMenu();
        }
    }


    public void WarenkorbAnsehen() {

        Warenkorb warenkorb = shop.getWarenkorb(authuser);

        if (warenkorb.getWarenkorbListe().size() == 0) {
            System.out.println("Der Warenkorb ist leer");
            KundenMenu();
        } else {
            System.out.println(warenkorb);
            warenkorbmenu();
        }


    }
public void warenkorbmenu(){
    System.out.println("--------------------------------------");
    System.out.println("Was möchten sie machen?");
    System.out.println(" 1. Artikel Menge ändern");
    System.out.println(" 2. Warenkorb leeren");
    System.out.println(" 3. kaufen");
    System.out.println(" 4. Zur HomePage zurück");

    boolean isValid = false;
    String input1 = scanner.nextLine();
    switch (input1) {
        case "1":
            //ArtikelMengeändern();
            isValid = true;
            break;
        case "2":
            Warenkorbleeren();
            isValid = true;
            break;
        case "3":
            kaufen();
            isValid = true;
            break;
        case "4":
            KundenMenu();
            isValid = true;
            break;
        default:
            System.out.println("Was möchten sie machen?");
    }
}
    private void Warenkorbleeren() {
        shop.Warenkorbleeren(authuser);
    }

    private void kaufen() {
       shop.articlebestandanderen(authuser);
       shop.kundeEreignisfesthalten("Auslagerung", authuser);
       shop.kaufen(authuser);
       KundenMenu();

    }



    public void  SucheArtikelMitName() {
        System.out.print("Geben Sie den Suchbegriff ein: ");
        String suchbegriff = scanner.nextLine().toLowerCase();

       Artikel artikel =  shop.sucheartiklemitname(suchbegriff);

        if (artikel != null) {
            System.out.println("Gefundene Artikel:");
            System.out.println(artikel);
            KundenMenu();
        } else {
            System.out.println("Keine Artikel gefunden, die \"" + suchbegriff + "\" enthalten.");
            KundenMenu();
        }

    }


    /*
    private void ArtikelMengeändern() {
        System.out.print("Geben Sie die Artikelnummer des zu ändernden Artikels ein: ");
        int artikelnummer = Integer.parseInt(scanner.nextLine());

        System.out.print("Geben Sie die neue Anzahl ein: ");
        int neueAnzahl = Integer.parseInt(scanner.nextLine());

        try {
            shop.artikelMengeaendern(artikelnummer, neueAnzahl, authuser);
            System.out.println("Artikelmenge erfolgreich geändert.");
        } catch (Exception e) {
            System.out.println("Fehler beim Ändern der Artikelmenge: " + e.getMessage());
        }

        WarenkorbAnsehen(); // Anzeigen des aktualisierten Warenkorbs
    }
*/

}


