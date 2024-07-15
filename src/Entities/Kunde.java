package Entities;

/**
 * This class represents a customer in the Eshop system, extending the {@link Nutzer} class.
 * It includes additional details such as the customer's address and shopping cart.
 *
 * @see Nutzer
 */
public class Kunde extends Nutzer {

    private Adresse adresse;
    private Warenkorb warenkorb;

    /**
     * Constructs a new customer with the specified details and initializes a shopping cart for him.
     *
     * @param nutzerNummer    the unique identifier for the customer
     * @param name            the name of the customer
     * @param benutzerkennung the username of the customer
     * @param passwort        the password of the customer
     * @param adresse         the address of the customer
     */
    public Kunde(int nutzerNummer, String name, String benutzerkennung, String passwort, Adresse adresse) {
        super(nutzerNummer, name, benutzerkennung, passwort);
        this.adresse = adresse;
        this.warenkorb = new Warenkorb(); // Initializes an empty shopping cart for the customer
    }


    /**
     * Gets the address of the customer.
     *
     * @return the address of the customer
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * Sets the address of the customer.
     *
     * @param adresse the address to set
     */
    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * Gets the shopping cart of the customer.
     *
     * @return the shopping cart of the customer
     */
    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    /**
     * Sets the shopping cart of the customer.
     *
     * @param warenkorb the shopping cart to set
     */
    public void setWarenkorb(Warenkorb warenkorb) {
        this.warenkorb = warenkorb;
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return a string representation of the customer
     */
    @Override
    public String toString() {
        return "Kunde," + super.toString() + "," + adresse.toString();
    }
}
