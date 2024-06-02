package Entities;

public class Mitarbeiter extends Nutzer
{

    public Mitarbeiter(int nutzerNummer, String name, String benutzerkennung, String passwort) {
        super(nutzerNummer, name, benutzerkennung, passwort);



    }


    @Override
    public String toString() {
        return "Mitarbeiter," + super.toString();
    }

}
