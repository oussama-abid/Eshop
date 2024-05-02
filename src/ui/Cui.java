package ui;

import Entities.Artikel;
import Entities.Kunde;
import Entities.User;
import domain.PersonenVerwaltung;
import domain.Eshopproducts;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Cui {
    private Scanner scanner;
    private PersonenVerwaltung userManagement;   // TODO: löschen (-> ui.EShop)
    private EShop shop;
    private Eshopproducts Produkte;
    private boolean isFirstTime = true;
    private List<Kunde> kundenliste = new ArrayList<>();


    public Cui() {
        scanner = new Scanner(System.in);
        userManagement = new PersonenVerwaltung();
        shop = new EShop();
        Produkte = new Eshopproducts();
        actions();
    }

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
        System.out.print("Nummer: ");                            //sollte dies nicht vom System gemacht werden? (frage Eyüphan) ist da die Telefonnummer?

        int nummer = Integer.parseInt(scanner.nextLine());

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Adresse: ");
        String Adresse = scanner.nextLine();
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        userManagement.registriereKunde(nummer, name, benutzerkennung, passwort,Adresse);
        System.out.println("Sie wurden erfolgreich registriert. Sie können sich nun einloggen. ");

//        actions();
    }

    private void loginUser() {
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Geben sie ihre Login Informationen ein.");
            System.out.print("Benutzerkennung: ");
            String benutzerkennung = scanner.nextLine();
            System.out.print("Passwort: ");
            String passwort = scanner.nextLine();

//            user user = userManagement.login(benutzerkennung, passwort);
            User user = shop.login(benutzerkennung, passwort);

            if (user != null) {
                if(user instanceof Kunde) {
                    loggedIn = true;
                    System.out.println("Herzlich Willkommen bei unserem E-shop, Viel Spas!");
                    showeshop();
                }
                else {
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
                           /* case "1":
                              zeigeArtikelliste();
                                isValid = true;
                                break;                       */
                            case "2":
                                FuegeArtikelHinzu();
                                isValid = true;
                                break;
                /*              case "3":
                                aendereBestand();   //funktion muss noch geamcht werden
                                isValid = true;
                                break;
                                case "4":
                                ZeigeKundenListe();   //funktion muss noch geamcht werden
                                isValid = true;
                                break;
                            case "5":
                                ZeigeMitarbeiterListe();   //funktion muss noch gemacht werden
                                isValid = true;
                                break;
                            case "6":
                               registriereMitarbeiter();              //funktion muss noch gemacht werden
                                isValid = true;
                                break;                          */
                            case "7":
                                isValid = true; // Allow exit to break the loop
                                break;
                            default:
                                System.out.println("Was möchten sie machen?");
                        }
                    }
                    scanner.close();
                }

            } else {
                System.out.println("Falscher Benutzername oder Passwort");
            }
        }
    }

    private void FuegeArtikelHinzu() {
        System.out.println("Artikelbeschreibung: ");
        System.out.print("Nummer: ");                //sollte dies nicht vom system automatisch passieren? (eyüphan frage)
        int nummer = Integer.parseInt(scanner.nextLine());

        System.out.print("Bezeichnung: ");
        String Bezeichnung = scanner.nextLine();
        System.out.print("Bestand: ");
        int Bestand = scanner.nextInt();
        System.out.print("Preis: ");
        float Preis;
        Preis = Float.parseFloat(scanner.nextLine());
        Artikel art = new Artikel(nummer, Bezeichnung, Bestand, Preis);
        Produkte.addArticle(art);
        System.out.println("Artikel wurde Hinzugefuegt");

//        actions();

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

    }
}



