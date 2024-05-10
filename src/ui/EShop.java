package ui;

import Entities.*;
import domain.*;


import java.time.LocalDate;
import java.util.List;

public class EShop {

    private PersonenVerwaltung PersonenVerwaltung;
    private ArtikelVerwaltung Produkte;
    private  VerlaufVerwaltung VerlaufVerwaltung;
    public EShop() {
        this.PersonenVerwaltung = new PersonenVerwaltung();
        this.Produkte = new ArtikelVerwaltung();
        this.VerlaufVerwaltung = new VerlaufVerwaltung();
        registriereMitarbeiter( "admin", "admin", "admin");
        registriereKunde( "user", "user", "user", "str","stdt","xx",55555,"de");
    }

    // Mitarbeiter funktionen


    public User login(String benutzerkennung, String passwort) {
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

    public void ArtikelHinzufuegen(Artikel art) {

        Produkte.ArtikelHinzufuegen(art);
    }

    public List<Artikel>  getArtikelListe(){

        return Produkte.getArtikelListe();
    }

    public List <Event>  ShopVerlaufAnzeigen(){
        return VerlaufVerwaltung.getEventList();
    }



    public void Ereignisfesthalten(String operation, Artikel artikel, int quantity, User user) {
        VerlaufVerwaltung.Ereignisfesthalten(operation,artikel,quantity,user);
    }
}