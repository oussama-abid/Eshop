package Entities;

public class Nutzer
{
    private int nutzerNummer;
    private String name;
    private String Benutzerkennung;
    private String Passwort;




    public Nutzer(int nutzerNummer, String name, String benutzerkennung, String passwort) {
        super();
        this.nutzerNummer = nutzerNummer;
        this.name = name;
        Benutzerkennung = benutzerkennung;
        Passwort = passwort;
    }



    public int getNutzerNummer() {
        return nutzerNummer;
    }
    public void setNutzerNummer(int nutzerNummer) {
        this.nutzerNummer = nutzerNummer;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getBenutzerkennung() {
        return Benutzerkennung;
    }
    public void setBenutzerkennung(String benutzerkennung) {
        Benutzerkennung = benutzerkennung;
    }


    public String getPasswort() {
        return Passwort;
    }

    public void setPasswort(String passwort) {
        Passwort = passwort;
    }



}
