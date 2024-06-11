package ui;

import Entities.*;
import Exceptions.NutzernameExistiertBereits;
import Exceptions.Plzexception;
import domain.*;


import java.util.List;

public class EShop {

    private PersonenVerwaltung PersonenVerwaltung;
    private ArtikelVerwaltung Produkte;
    private  VerlaufVerwaltung VerlaufVerwaltung;
    private WarenkorbVerwaltung warenkorbVerwaltung;

    public EShop() {
        this.PersonenVerwaltung = new PersonenVerwaltung();
        this.Produkte = new ArtikelVerwaltung();
        this.VerlaufVerwaltung = new VerlaufVerwaltung();
        this.warenkorbVerwaltung = new WarenkorbVerwaltung();



        PersonenVerwaltung.loadUsers("benutzers.txt");
        Produkte.loadartikel("artikel.txt");
        VerlaufVerwaltung.loadevents("events.txt");

    }



                                                 // Mitarbeiter funktionen


    public Nutzer login(String benutzerkennung, String passwort) {
        return PersonenVerwaltung.login(benutzerkennung, passwort);
    }

    public void registriereMitarbeiter( String name, String benutzerkennung, String passwort)  {
         PersonenVerwaltung.registriereMitarbeiter( name,  benutzerkennung,  passwort);
    }

    public void registriereKunde( String name, String benutzerkennung, String passwort , String straße, String stadt, String bundesland, int postleitzahl, String land)  {
        PersonenVerwaltung.registriereKunde( name,  benutzerkennung,  passwort,straße, stadt, bundesland, postleitzahl, land);
    }

    public List<Kunde> getKundenList() {
        return PersonenVerwaltung.getKundenList();
    }

    public List<Mitarbeiter> getMitarbeiterlist() {
        return PersonenVerwaltung.getMitarbeiterlist();
    }




                                             // artikel funktionen


    public void BestandAendern(int artikelnummer, int newBestand) {
   Produkte.BestandAendern(artikelnummer,newBestand);
    }

    public Artikel ArtikelHinzufuegen(String Bezeichnung, int bestand, float Preis,boolean istMassenartikel, int packungsGrosse) {

       return Produkte.ArtikelHinzufuegen(Bezeichnung, bestand, Preis,istMassenartikel,packungsGrosse);
    }


    public List<Artikel>  getArtikelListe(){

        return Produkte.getArtikelListe();
    }

    public List <Event>  ShopVerlaufAnzeigen(){
        return VerlaufVerwaltung.getEventList();
    }

    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, Nutzer nutzer) {
        VerlaufVerwaltung.Ereignisfesthalten(operation,artikel,quantity, nutzer);
    }
    public void kundeEreignisfesthalten(String operation, Nutzer authuser) {
        VerlaufVerwaltung.kundeEreignisfesthalten(operation,authuser);
    }

    public Artikel findeArtikelDurchID(int artikelnummer) {
        return Produkte.SucheArtikelPerID(artikelnummer);
    }

    public void inWarenKorbLegen(Artikel artikel, int anzahl, Nutzer authuser) {
    warenkorbVerwaltung.inWarenKorbLegen(artikel,anzahl,authuser);
    }

    public Warenkorb getWarenkorb(Nutzer authuser) {
      return  warenkorbVerwaltung.getWarenkorb(authuser);
    }

    public void kaufen(Nutzer authuser) {
        warenkorbVerwaltung.kaufen(authuser);
    }

    public void Warenkorbleeren(Nutzer authuser) {
        warenkorbVerwaltung.Warenkorbleeren(authuser);
    }

    public void articlebestandanderen(Nutzer authuser) {
        Produkte.articlebestandanderen(authuser);
    }

    public boolean checkUniqueUsername(String benutzerkennung) throws NutzernameExistiertBereits {
        return PersonenVerwaltung.checkUniqueUsername(benutzerkennung);
    }

    public Artikel sucheartiklemitname(String suchbegriff) {
        return Produkte.sucheartiklemitname(suchbegriff);
    }

    public void artikelMengeaendern(String Artikelname, int neueAnzahl, Nutzer authuser) {
        warenkorbVerwaltung.artikelMengeaendern(Artikelname,neueAnzahl,authuser);

    }

    public boolean checkArtikelwarenkorb(String artikelname, Nutzer authuser) {
        return warenkorbVerwaltung.checkArtikelwarenkorb(artikelname,authuser);
    }

    public List<Artikel> suchemitname(String suchbegriff) {
        return  Produkte.suchemitname(suchbegriff);

    }

    public boolean validatePlz(int postleitzahl) throws Plzexception {
        return PersonenVerwaltung.validatePlz(postleitzahl);
    }

    public List<Artikelhistory> ShophistoryAnzeigen() {
       return VerlaufVerwaltung.getArticleQuantitiesPerDay();
    }


}