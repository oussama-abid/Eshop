package Entities;

public class Kunde extends User {

    private String Adresse;





    public Kunde(int usernummer, String name, String benutzerkennung, String passwort, String usertype, String Adresse) {
        super(usernummer, name, benutzerkennung, passwort, usertype);
        this.Adresse=Adresse;

    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }


}
