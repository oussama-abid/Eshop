package Entities;

public class Mitarbeiter extends User
{

    public Mitarbeiter(int usernummer, String name, String benutzerkennung, String passwort, String usertype) {
        super(usernummer, name, benutzerkennung, passwort, usertype);
        usertype="Entities.Mitarbeiter";



    }



}
