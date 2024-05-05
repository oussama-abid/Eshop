package Entities;

public class Mitarbeiter extends User
{

    public Mitarbeiter(int usernummer, String name, String benutzerkennung, String passwort) {
        super(usernummer, name, benutzerkennung, passwort);



    }

    @Override
    public String toString() {
        return "Mitarbeiter: nummer=" + getUsernummer() + ", Name=" + getName() + ", Benutzerkennung=" + getBenutzerkennung();
    }

}
