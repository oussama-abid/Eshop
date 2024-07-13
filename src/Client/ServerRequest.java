package Client;

import Entities.Artikel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ServerRequest {

    private BufferedReader in;
    private PrintStream out;

    public ServerRequest(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;
    }
    public  List<Artikel> requestArticleList() throws IOException {
        List<Artikel> articles = new ArrayList<>();

        out.println("1"); // Send command to request article list
        String response = in.readLine(); // Read response from server

        if (response.startsWith("artikellist,")) {
            String[] parts = response.split(",", 2);
            if (parts.length == 2) {
                int numArticles = Integer.parseInt(parts[1].trim());

                for (int i = 0; i < numArticles; i++) {
                    int artikelnummer = Integer.parseInt(in.readLine());
                    String bezeichnung = in.readLine();
                    int bestand = Integer.parseInt(in.readLine());
                    float preis = Float.parseFloat(in.readLine());
                    // Since istMassenartikel is not sent from server, assuming false
                    boolean istMassenartikel = false;
                    articles.add(new Artikel(artikelnummer, bezeichnung, bestand, preis, istMassenartikel));
                }
            } else {
                System.err.println("Unexpected response format: " + response);
            }
        } else {
            System.err.println("Unexpected response format: " + response);
        }

        return articles; // Return list of articles retrieved from server
    }


}
