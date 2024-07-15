package Server;

import Entities.Artikel;
import Entities.Kunde;
import Entities.Nutzer;
import Exceptions.FalscheLoginDaten;
import Exceptions.MassengutException;
import domain.EShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientRequestProcessor handles client requests on the server side .
 * It reads commands from clients, processes them, and sends appropriate responses.
 */
class ClientRequestProcessor implements Runnable {

    private final EShop eShop;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;
    List<Artikel> artikels;

    /**
     * Constructs a ClientRequestProcessor instance for a connected client socket.
     *
     * @param socket The client socket representing the connected client.
     * @param eShop  The instance of EShop handling business logic.
     */
    public ClientRequestProcessor(Socket socket, EShop eShop) {
        this.eShop = eShop;
        clientSocket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e2) {
            }
            System.err.println("Exception while setting up stream: " + e);
            return;
        }

        System.out.println("Connected to " + clientSocket.getInetAddress()
                + ":" + clientSocket.getPort());

        // Initialize artikels
        artikels = new ArrayList<>(); // Initialize artikels with an empty list
    }

    /**
     * Continuously reads commands from the client, processes them, and sends responses.
     */
    public void run() {
        String input = "";

        try {
            while ((input = in.readLine()) != null) {
                if (input.startsWith("login,")) {
                    handleLogin(input);
                } else if (input.startsWith("addartikel,")) {
                    addartikel(input);
                } else if (input.equals("3")) {
                    displayKunden();
                } else if (input.equals("1")) {
                    displayArtikel();
                } else if (input.equals("Q")) {
                    disconnect();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading input from client: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    /**
     * loading the list of articles .
     */
    private void displayArtikel() {
        List<Artikel> artikels = eShop.getArtikelListe();
        sendartikelstoclient(artikels);
    }

    /**
     * Sends a list of articles to the client.
     *
     * @param artikels The list of articles to send.
     */
    private void sendartikelstoclient(List<Artikel> artikels) {
        out.println("artikellist," + artikels.size()); // Prefix the response
        for (Artikel artikel : artikels) {
            sendartikeltoclient(artikel);
        }
    }

    /**
     * Sends a single article.
     *
     * @param artikel The article to send.
     */
    private void sendartikeltoclient(Artikel artikel) {
        out.println(artikel.getArtikelnummer() + "," + artikel.getBezeichnung() + "," + artikel.getBestand() + "," + artikel.getPreis() + "," + artikel.isIstMassenartikel());
    }

    /**
     * loading the list of customers.
     */
    private void displayKunden() {
        List<Kunde> kunden = eShop.getKundenList();
        sendKundenToClient(kunden);
    }

    /**
     * Sends a list of customers to the client.
     *
     * @param kunden The list of customers to send.
     */
    private void sendKundenToClient(List<Kunde> kunden) {
        out.println("customerlist," + kunden.size());
        for (Kunde kunde : kunden) {
            sendKundeToClient(kunde);
        }
    }

    /**
     * Sends a single customer.
     *
     * @param kunde The customer to send.
     */
    private void sendKundeToClient(Kunde kunde) {
        out.println(kunde.getName());
        out.println(kunde.getAdresse());
    }

    /**
     * Handles the login request from the client.
     *
     * @param input The login command received from the client.
     */
    private void handleLogin(String input) {
        String[] parts = input.split(",");
        if (parts.length == 3 && parts[0].equals("login")) {
            String benutzerkennung = parts[1];
            String passwort = parts[2];

            try {
                Nutzer nutzer = eShop.login(benutzerkennung, passwort);
                out.println("success");
            } catch (FalscheLoginDaten e) {
                out.println(e.getMessage());
            }
        } else {
            out.println("invalid request");
        }
    }

    /**
     * Handles the add article request from the client.
     *
     * @param input The add article command received from the client.
     */
    private void addartikel(String input) {
        String[] parts = input.split(",");
        if (parts.length == 6 && parts[0].equals("addartikel")) {
            String bezeichnung = parts[1];
            int bestand = Integer.parseInt(parts[2]);
            float preis = Float.parseFloat(parts[3]);
            boolean istMassenartikel = Boolean.parseBoolean(parts[4]);
            int packungsGrosse = Integer.parseInt(parts[5]);

            try {
                Artikel art = eShop.ArtikelHinzufuegen(bezeichnung, bestand, preis, istMassenartikel, packungsGrosse);

                String message = "Neuer Artikel hinzugefügt: " + art.toString();
                EshopServer.broadcastMessage(message); // Notify all clients about the new article

            } catch (MassengutException e) {
                out.println("Error adding article: " + e.getMessage());
            }
        } else {
            out.println("Invalid addartikel command format");
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param message The message to send.
     */
    public void sendToClient(String message) {
        out.println(message);
    }

    /**
     * Disconnects the client from the server.
     */
    private void disconnect() {
        try {
            out.println("session end");
            clientSocket.close();

            System.out.println("Connection to " + clientSocket.getInetAddress()
                    + ":" + clientSocket.getPort() + " closed by client");
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
