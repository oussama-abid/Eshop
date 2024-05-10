package Entities;

public class Kunde extends User
{

    private Adresse adresse;






    public Kunde(int usernummer, String name, String benutzerkennung, String passwort, Adresse adresse) {
        super(usernummer, name, benutzerkennung, passwort);
        this.adresse = adresse;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }


    @Override
    public String toString() {
        return "Kunde: nummer=" + getUsernummer() + ", Name=" + getName() + ", Benutzerkennung=" + getBenutzerkennung() + ", Adresse :" + adresse;
    }
}
