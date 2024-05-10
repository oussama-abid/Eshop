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

    private User authuser ;

    public Cui(EShop shop) {
        scanner = new Scanner(System.in);
        this.shop = new EShop();
        actions();
    }


    //-----------------------------------------------for both ---------------------------------------------------


    private void actions() {
        boolean shouldExit = false;
        boolean isValid = false;
        while (!shouldExit) {
            System.out.print("Wollen sie sich registrieren (R) oder einloggen (L)? : ");
            String input1 = scanner.nextLine();
            switch (input1) {
                case "L":
                case "l":
                    loginUser();
                    isValid = true;
                    break;
                case "R"  :
                case "r"  :
                    registerUser();
                    isValid = true;
                    break;
                case "exit":
                    shouldExit = true; // Allow exit to break the loop
                    break;
                default:
                    System.out.println("----------------------");
            }
        }
        scanner.close();
    }



    private void registerUser() {
        // Es muss noch eine UserExistiertBereitsException gemacht werden
        System.out.println("Geben Sie Ihre Informationen ein");

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();

        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();


        System.out.println("Geben Sie Ihre Adresse:");
        System.out.print("Straße: ");
        String straße = scanner.nextLine();
        System.out.print("Stadt: ");
        String stadt = scanner.nextLine();
        System.out.print("Bundesland: ");
        String bundesland = scanner.nextLine();

        System.out.print("PLZ: ");
        int postleitzahl = Integer.parseInt(scanner.nextLine());
        System.out.print("Land: ");
        String land = scanner.nextLine();





        shop.registriereKunde(name, benutzerkennung, passwort, straße, stadt, bundesland, postleitzahl, land);


        // actions();
    }


    private void loginUser() {
        System.out.println("Geben sie ihre Login Informationen ein.");
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        User user = shop.login(benutzerkennung, passwort);

        if (user != null) {
            authuser = user;
            if(user instanceof Kunde) {

                System.out.println("Herzlich Willkommen bei unserem E-shop, Viel Spaß!");
                showeshop();
            }
            else {
                Mitarbeitermenu();

            }

        } else {
            System.out.println("Falscher Benutzername oder Passwort");
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

        int Bestand = 0;
        boolean validBestand = false;
        while (!validBestand) {
            System.out.print("Bestand: ");
            if (scanner.hasNextInt()) {
                Bestand = scanner.nextInt();
                validBestand = true;
            } else {
                System.out.println("Fehler: Bitte geben Sie eine ganze Zahl ein.");
                scanner.next(); // Verbrauche ungültige Eingabe
            }
        }
        scanner.nextLine();
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
        scanner.nextLine();

        Artikel art = new Artikel(Bezeichnung, Bestand, Preis);
        shop.ArtikelHinzufuegen(art,authuser);
        shop.Ereignisfesthalten("neuer Artikel",art,art.getBestand(), authuser);
        System.out.println("Artikel wurde hinzugefügt");
        Mitarbeitermenu();
        actions();
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

        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
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
        ShopVerlauf =shop.ShopVerlaufAnzeigen();
        for (Event event : ShopVerlauf) {
            System.out.println(event);
        }
        Mitarbeitermenu ();
    }


    private void aendereBestand() {
        System.out.print("Geben Sie die Artikelnummer ein: ");
        int artikelnummer = Integer.parseInt(scanner.nextLine());
        for (Artikel artikel : shop.getArtikelListe()) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                    System.out.print("wie viele Artikel möchten Sie hinzufügen ? : ");
                    String newBestandInput = scanner.nextLine();
                    int newBestand = Integer.parseInt(newBestandInput);
                    shop.BestandAendern(artikelnummer, newBestand);
                    System.out.println("Lagerbestand für Artikel " + artikelnummer + " auf " + artikel.getBestand() + " aktualisiert.");
                    shop.Ereignisfesthalten("Einlagerung",artikel,newBestand, authuser);
                    Mitarbeitermenu ();
                    return;

            }
        }
        System.out.println("Artikel nicht gefunden.");
        Mitarbeitermenu ();
    }







//--------------------- Kunde functions --------------------------------------------------


    private void KundenMenu () {
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
                //ArtikelSuchen
                isValid = true;
                break;
            case "3":
                HinzufuegenZumWarenkorb();
                isValid = true;
                break;
            case "4":
                WarenkorbAnsehen();
                isValid = true;
                break;
            case "5":
                isValid = true; // Allow exit to break the loop
                break;
            default:
                System.out.println("Was möchten sie machen?");
        }
    }


}


    private void HinzufuegenZumWarenkorb(){




    }

    private void WarenkorbAnsehen(){


    }



























}

