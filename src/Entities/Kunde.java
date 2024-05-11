package Entities;

public class Kunde extends User
{

    private Adresse adresse;

    private Warenkorb warenkorb;





    public Kunde(int usernummer, String name, String benutzerkennung, String passwort, Adresse adresse) {
        super(usernummer, name, benutzerkennung, passwort);
        this.adresse = adresse;
        this.warenkorb = new Warenkorb();
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    public void setWarenkorb(Warenkorb warenkorb) {
        this.warenkorb = warenkorb;
    }

    @Override
    public String toString() {
        return "Kunde: nummer=" + getUsernummer() + ", Name=" + getName() + ", Benutzerkennung=" + getBenutzerkennung() + ", Adresse :" + adresse;
    }
}
