package ui;

import Entities.Artikel;
import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.User;
import domain.PersonenVerwaltung;
import domain.Eshopproducts;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import Entities.User;

public class Cui {
    private Scanner scanner;
    private PersonenVerwaltung userManagement;   // TODO: löschen (-> ui.EShop)
    private EShop shop;
    private Eshopproducts Produkte;
    private boolean isFirstTime = true;

    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterlist = new ArrayList<>();


    private User authuser ;

    public Cui(PersonenVerwaltung userManagement) {
        scanner = new Scanner(System.in);

        this.userManagement = userManagement;
        shop = new EShop();
        Produkte = new Eshopproducts();
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

        List<Artikel> artikels = Produkte.getArticles();
        System.out.println("-------- Artikel: ----------");
        for (Artikel artikel : artikels) {
            System.out.println("Artikel Nummer: " + artikel.getArtikelnummer());
            System.out.println("Bezeichnung: " + artikel.getBezeichnung());
            System.out.println("Bestand: " + artikel.getBestand());
            System.out.println("Preis: " + artikel.getPreis());
            System.out.println("----------------------");
        }
        if(authuser instanceof Kunde) {

        }
        else {
            Mitarbeitermenu();

        }

    }
    //--------------------- Kunde functions --------------------------------------------------











    //--------------------- Mitarbeiter functions --------------------------------------------------


    private void FuegeArtikelHinzu() {
        System.out.println("Artikelbeschreibung: ");
        int nummer = EindeutigeArtikelnummer();

        System.out.print("Bezeichnung: ");
        String Bezeichnung = scanner.nextLine();
        System.out.print("Bestand: ");
        int Bestand = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Preis: ");
        float Preis = Float.parseFloat(scanner.nextLine());



        Artikel art = new Artikel(nummer, Bezeichnung, Bestand, Preis);
        Produkte.addArticle(art);
        System.out.println("Artikel wurde Hinzugefuegt");
        Mitarbeitermenu();
//        actions();

    }
    private int EindeutigeArtikelnummer() {
        Random random = new Random();
        int newNummer;
        boolean unique = false;
        do {
            newNummer = random.nextInt();
            newNummer = Math.abs(newNummer);
            unique = true;
            for (Artikel artikel : Produkte.getArticles()) {
                if (artikel.getArtikelnummer() == newNummer) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);
        return newNummer;
    }


    private void Mitarbeitermenu (){
        boolean isValid = false;
        while (!isValid) {
            System.out.print(" 1. Artikelliste ausgeben");
            System.out.print(" 2. Artikel hinzufuegen");
            System.out.print(" 3. Artikel Bestand aenern");
            System.out.print(" 4. Kundenliste ausgeben");
            System.out.print(" 5. Mitarbeiterliste ausgeben");
            System.out.print(" 6. Mitarbeiter hinzufuegen");
            System.out.print(" 7. Logout");

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

    private void aendereBestand() {
        System.out.print("Geben Sie die Artikelnummer ein: ");
        int artikelnummer = Integer.parseInt(scanner.nextLine());
        for (Artikel artikel : Produkte.getArticles()) {
            if (artikel.getArtikelnummer() == artikelnummer) {


                    System.out.print("Geben Sie den neue bestand ein: ");
                    String newBestandInput = scanner.nextLine();
                    int newBestand = Integer.parseInt(newBestandInput);
                    Produkte.updateStock(artikelnummer, newBestand);
                    System.out.println("Lagerbestand für Artikel " + artikelnummer + " auf " + newBestand + " aktualisiert.");
                    Mitarbeitermenu ();
                    return;

            }
        }
        System.out.println("Artikel mit Artikelnummer  nicht gefunden.");
        Mitarbeitermenu ();
    }

}



