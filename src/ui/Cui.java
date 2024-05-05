package ui;

import Entities.Artikel;
import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.User;
import domain.PersonenVerwaltung;
import domain.ArtikelVerwaltung;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cui {
    private Scanner scanner;
    private PersonenVerwaltung userManagement;   // TODO: löschen (-> ui.EShop)
    private EShop shop;
    private ArtikelVerwaltung Produkte;
    private boolean isFirstTime = true;

    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterlist = new ArrayList<>();


    private User authuser ;

    public Cui(PersonenVerwaltung userManagement) {
        scanner = new Scanner(System.in);

        this.userManagement = userManagement;
        shop = new EShop();
        Produkte = new ArtikelVerwaltung();
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

        // es muss noch eine UserExistiertBereitsException gemacht werden
        System.out.println("Geben sie ihre Informationen ein");


        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Adresse: ");
        String Adresse = scanner.nextLine();
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        userManagement.registriereKunde(name, benutzerkennung, passwort,Adresse);
        System.out.println("Sie wurden erfolgreich registriert. Sie können sich nun einloggen. ");

//        actions();
    }

    private void loginUser() {
        System.out.println("Geben sie ihre Login Informationen ein.");
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        User user = userManagement.login(benutzerkennung, passwort);

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
        List<Artikel> artikelListe = Produkte.getArtikelListe();
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


    //--------------------- Kunde functions --------------------------------------------------











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
        Produkte.ArtikelHinzufuegen(art);
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
                                //ShopVerlaufAnzeigen();
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

        userManagement.registriereMitarbeiter(name, benutzerkennung, passwort);
        System.out.println("Mitarbeiter  wird erfolgreich registriert");
        Mitarbeitermenu();

    }

    private void ZeigeMitarbeiterListe() {
        mitarbeiterlist = userManagement.getMitarbeiterlist();
        for (Mitarbeiter mitarbeiter : mitarbeiterlist) {
            System.out.println(mitarbeiter);
        }
        Mitarbeitermenu ();
    }

    private void ZeigeKundenListe() {
        kundenList = userManagement.getKundenList();
        for (Kunde kunde : kundenList) {
            System.out.println(kunde);
        }
        Mitarbeitermenu ();
    }

    private void HinzufuegenZumWarenkorb(){




    }

    private void WarenkorbAnsehen(){


    }

    private void aendereBestand() {
        System.out.print("Geben Sie die Artikelnummer ein: ");
        int artikelnummer = Integer.parseInt(scanner.nextLine());
        for (Artikel artikel : Produkte.getArtikelListe()) {
            if (artikel.getArtikelnummer() == artikelnummer) {
                    System.out.print("Geben Sie den neue bestand ein: ");
                    String newBestandInput = scanner.nextLine();
                    int newBestand = Integer.parseInt(newBestandInput);
                    Produkte.BestandAendern(artikelnummer, newBestand);
                    System.out.println("Lagerbestand für Artikel " + artikelnummer + " auf " + newBestand + " aktualisiert.");
                    Mitarbeitermenu ();
                    return;

            }
        }
        System.out.println("Artikel mit Artikelnummer  nicht gefunden.");
        Mitarbeitermenu ();
    }


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
}




