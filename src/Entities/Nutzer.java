package Entities;

/**
 * This class represents a user in the Eshop system, containing details such as
 * user number, name, username, and password.
 *
 */
public class Nutzer {
    private int nutzerNummer;
    private String name;
    private String Benutzerkennung;
    private String Passwort;

    /**
     * Constructs a new Nutzer with the specified details.
     *
     * @param nutzerNummer the unique identifier for the user
     * @param name the name of the user
     * @param benutzerkennung the username of the user
     * @param passwort the password of the user
     */
    public Nutzer(int nutzerNummer, String name, String benutzerkennung, String passwort) {
        super();
        this.nutzerNummer = nutzerNummer;
        this.name = name;
        this.Benutzerkennung = benutzerkennung;
        this.Passwort = passwort;
    }

    /**
     * Gets the user number.
     *
     * @return the user number
     */
    public int getNutzerNummer() {
        return nutzerNummer;
    }

    /**
     * Sets the user number.
     *
     * @param nutzerNummer the user number to set
     */
    public void setNutzerNummer(int nutzerNummer) {
        this.nutzerNummer = nutzerNummer;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getBenutzerkennung() {
        return Benutzerkennung;
    }

    /**
     * Sets the username of the user.
     *
     * @param benutzerkennung the username to set
     */
    public void setBenutzerkennung(String benutzerkennung) {
        this.Benutzerkennung = benutzerkennung;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPasswort() {
        return Passwort;
    }

    /**
     * Sets the password of the user.
     *
     * @param passwort the password to set
     */
    public void setPasswort(String passwort) {
        this.Passwort = passwort;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return nutzerNummer + "," + name + "," + Benutzerkennung + "," + Passwort;
    }
}
