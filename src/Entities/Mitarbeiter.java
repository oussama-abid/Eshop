package Entities;

/**
 * This class represents an employee in the  Eshop system, extending the {@link Nutzer} class.
 *
 * @see Nutzer
 */
public class Mitarbeiter extends Nutzer {

    /**
     * Constructs a new Mitarbeiter with the specified details.
     *
     * @param nutzerNummer the unique identifier for the employee
     * @param name the name of the employee
     * @param benutzerkennung the username of the employee
     * @param passwort the password of the employee
     */
    public Mitarbeiter(int nutzerNummer, String name, String benutzerkennung, String passwort) {
        super(nutzerNummer, name, benutzerkennung, passwort);
    }

    /**
     * Returns a string representation of the employee.
     *
     * @return a string representation of the employee
     */
    @Override
    public String toString() {
        return "Mitarbeiter," + super.toString();
    }
}
