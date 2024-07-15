package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a shopping cart that contains a list of items from the Eshop selected by the customer.
 * It provides methods to add items to the cart, calculate the total price, remove items,
 * and display the items of the shopping cart.
 *
 */
public class Warenkorb {

    private List<WarenkorbArtikel> WarenkorbListe;

    /**
     * Constructs an empty shopping cart.
     */
    public Warenkorb() {
        this.WarenkorbListe = new ArrayList<>();
    }

    /**
     * Gets the list of items in the shopping cart.
     *
     * @return the list of items in the shopping cart
     */
    public List<WarenkorbArtikel> getWarenkorbListe() {
        return WarenkorbListe;
    }

    /**
     * Sets the list of items in the shopping cart.
     *
     * @param warenkorbListe the list of items to set
     */
    public void setWarenkorbListe(List<WarenkorbArtikel> warenkorbListe) {
        WarenkorbListe = warenkorbListe;
    }

    /**
     * Adds an artikel to the shopping cart.
     *
     * @param artikel the artikel to add
     */
    public void addItem(WarenkorbArtikel artikel) {
        WarenkorbListe.add(artikel);
    }

    /**
     * Calculates the total price of all items in the shopping cart
     * by iterating through the list and summing up the total price of each item
     * (price per piece * quantity added to the cart).
     *
     * @return the total price of all items in the shopping cart
     */
    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (WarenkorbArtikel artikel : WarenkorbListe) {
            totalPrice += artikel.getAnzahl() * artikel.getArtikel().getPreis();
        }
        return totalPrice;
    }

    /**
     * Removes an item from the shopping cart.
     *
     * @param artikel the item to remove
     */
    public void removeItem(WarenkorbArtikel artikel) {
        WarenkorbListe.remove(artikel);
    }

    /**
     * Clears the shopping cart by removing all items.
     * Prints a message indicating that the shopping cart has been cleared.
     */
    public void Warenkorbleeren() {
        WarenkorbListe.clear();
        System.out.println("Warenkorb geleert.");
    }

    /**
     * Returns a string representation of the shopping cart.
     * If the shopping cart is empty, it returns a message indicating it is empty.
     * Otherwise, it displays each item in the shopping cart with its details:
     * article name, quantity added to the cart, price per piece,
     * total price for each item, and finally the total price of all items in the cart.
     *
     * @return a string representation of the shopping cart
     */
    @Override
    public String toString() {
        if (WarenkorbListe.isEmpty()) {
            return "Der Warenkorb ist leer.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s %-10s %-10s %-10s%n", "Artikel", "Anzahl", "Preis", "Insgesamt"));
            sb.append("----------------------------------------------------\n");
            for (WarenkorbArtikel artikel : WarenkorbListe) {
                sb.append(String.format("%-20s %-10d $%-9.2f $%-10.2f%n", artikel.getArtikel().getBezeichnung(), artikel.getAnzahl(), artikel.getArtikel().getPreis(), (artikel.getArtikel().getPreis() * artikel.getAnzahl())));
            }
            sb.append("-----------------------------------------------------\n");
            sb.append(String.format("Gesamtpreis: %.2f%n", calculateTotalPrice()));
            return sb.toString();
        }
    }


}
