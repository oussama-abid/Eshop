package Entities;

public class Kunde extends User
{

    private String Adresse;






    public Kunde(int usernummer, String name, String benutzerkennung, String passwort,String Adresse) {
        super(usernummer, name, benutzerkennung, passwort);
        this.Adresse=Adresse;

    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    @Override
    public String toString() {
        return "Kunde: nummer=" + getUsernummer() + ", Name=" + getName() + ", Benutzerkennung=" + getBenutzerkennung()+ ", adresse=" + getAdresse();
    }
}
