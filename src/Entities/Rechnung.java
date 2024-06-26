package Entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rechnung
{
    private Date date;

    private double gesamtpreis;
    private Kunde kunde;




    public Rechnung(Date date, double gesamtpreis, Kunde kunde) {
        super();
        this.date = date;

        this.gesamtpreis = gesamtpreis;
        this.kunde = kunde;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public double getGesamtpreis() {
        return gesamtpreis;
    }
    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }
    public Kunde getKunde() {
        return kunde;
    }
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

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
