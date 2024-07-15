package Entities;

/**
 * This class represents an address, containing details such as street, city, state, postal code, and country.
 *
 */
public class Adresse {
    private String straße;
    private String stadt;
    private String bundesland;
    private int postleitzahl;
    private String land;

    /**
     * Constructs a new Adresse with the specified details.
     *
     * @param straße the street
     * @param stadt the city
     * @param bundesland the state
     * @param postleitzahl the postal code
     * @param land the country
     */
    public Adresse(String straße, String stadt, String bundesland, int postleitzahl, String land) {
        this.straße = straße;
        this.stadt = stadt;
        this.bundesland = bundesland;
        this.postleitzahl = postleitzahl;
        this.land = land;
    }

    /**
     * Gets the street .
     *
     * @return the street
     */
    public String getStraße() {
        return straße;
    }

    /**
     * Sets the street .
     *
     * @param straße the street to set
     */
    public void setStraße(String straße) {
        this.straße = straße;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getStadt() {
        return stadt;
    }

    /**
     * Sets the city.
     *
     * @param stadt the city to set
     */
    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    /**
     * Gets the state or province.
     *
     * @return the state or province
     */
    public String getBundesland() {
        return bundesland;
    }

    /**
     * Sets the state or province.
     *
     * @param bundesland the state or province to set
     */
    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    /**
     * Gets the postal code.
     *
     * @return the postal code
     */
    public int getPostleitzahl() {
        return postleitzahl;
    }

    /**
     * Sets the postal code.
     *
     * @param postleitzahl the postal code to set
     */
    public void setPostleitzahl(int postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    public String getLand() {
        return land;
    }

    /**
     * Sets the country.
     *
     * @param land the country to set
     */
    public void setLand(String land) {
        this.land = land;
    }

    /**
     * Returns a string representation of the address.
     *
     * @return a string representation of the address
     */
    @Override
    public String toString() {
        return "Land: " + getLand() + ", Bundesland: " + getBundesland() + ", Stadt: " + getStadt() +
                ", Straße: " + getStraße() + ", PLZ: " + getPostleitzahl();
    }
}
