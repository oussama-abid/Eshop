package Entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class Represents an invoice containing the date of the transaction, total price, and customer details.
 */
public class Rechnung {

    private Date date;
    private double gesamtpreis;
    private Kunde kunde;

    /**
     * Constructs a new invoice with the specified date, total price, and customer.
     *
     * @param date the date of the transaction
     * @param gesamtpreis the total price of the transaction
     * @param kunde the customer associated with the transaction
     */
    public Rechnung(Date date, double gesamtpreis, Kunde kunde) {
        this.date = date;
        this.gesamtpreis = gesamtpreis;
        this.kunde = kunde;
    }

    /**
     * Retrieves the date of the transaction.
     *
     * @return the date of the transaction
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction.
     *
     * @param date the date of the transaction to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retrieves the total price of the transaction.
     *
     * @return the total price of the transaction
     */
    public double getGesamtpreis() {
        return gesamtpreis;
    }

    /**
     * Sets the total price of the transaction.
     *
     * @param gesamtpreis the total price of the transaction to set
     */
    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }

    /**
     * Retrieves the customer associated with the invoice.
     *
     * @return the customer associated with the invoice
     */
    public Kunde getKunde() {
        return kunde;
    }

    /**
     * Sets the customer associated with the invoice.
     *
     * @param kunde the customer to set
     */
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    /**
     * Generates a string representation of the invoice, including the date, customer name, purchased items,
     * and total price.
     *
     * @return a string representation of the invoice
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);

        sb.append("Rechnung\n");
        sb.append("Datum: ").append(formattedDate).append("\n");
        sb.append("Kunde: ").append(kunde.getName()).append("\n");

        Warenkorb warenkorb = kunde.getWarenkorb();
        List<WarenkorbArtikel> warenkorbListe = warenkorb.getWarenkorbListe();

        if (warenkorbListe.isEmpty()) {
            sb.append("Der Warenkorb ist leer.\n");
        } else {
            sb.append("Gekaufte Artikel:\n");
            sb.append(String.format("%-20s %-10s %-10s %-10s%n", "Artikel", "Anzahl", "Einzelpreis", "Gesamtpreis"));
            for (WarenkorbArtikel item : warenkorbListe) {
                Artikel artikel = item.getArtikel();

                if (artikel != null) {
                    double einzelpreis = artikel.getPreis();
                    int anzahl = item.getAnzahl();
                    double gesamtpreis = einzelpreis * anzahl;
                    // Append each line item in formatted manner
                    sb.append(String.format("%-20s %-10d $%-9.2f $%-10.2f%n", artikel.getBezeichnung(), anzahl, einzelpreis, gesamtpreis));
                } else {
                    sb.append("null\n");
                }
            }
            sb.append(String.format("Gesamtpreis: %.2f EUR%n", gesamtpreis));
        }
        return sb.toString();
    }
}
