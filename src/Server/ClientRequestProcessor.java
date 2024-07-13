package Server;

import Entities.Artikel;
import Entities.Kunde;
import Entities.Nutzer;
import Exceptions.FalscheLoginDaten;
import Exceptions.MassengutException;
import ui.EShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ClientRequestProcessor implements Runnable {


    private final EShop eShop;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;
    List<Artikel> artikels;
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

    public void run() {
        String input = "";

        try {
            while ((input = in.readLine()) != null) {
                if (input.startsWith("login,")) {
                    handleLogin(input);
                }else if (input.startsWith("addartikel,")) {
                    addartikel(input);
                } else if (input.equals("3")) {
                    displayKunden();
                }else if (input.equals("1")) {
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



    private void displayArtikel() {
        List<Artikel> artikels = eShop.getArtikelListe();
        sendartikelstoclient(artikels);
    }



    private void sendartikelstoclient(List<Artikel> artikels) {
        out.println("artikellist," + artikels.size()); // Prefix the response
        for (Artikel artikel : artikels) {
            sendartikeltoclient(artikel);
        }
    }
    private void sendartikeltoclient(Artikel artikel) {
        out.println(artikel.getArtikelnummer());
        out.println(artikel.getBezeichnung());
        out.println(artikel.getBestand());
        out.println(artikel.getPreis());

    }

    private void displayKunden() {
        List<Kunde> kunden = eShop.getKundenList();
        sendKundenToClient(kunden);
    }
    private void sendKundenToClient(List<Kunde> kunden) {
        out.println("customerlist," + kunden.size());
        for (Kunde kunde : kunden) {
            sendKundeToClient(kunde);
        }
    }
    private void sendKundeToClient(Kunde kunde) {
        out.println(kunde.getName());
        out.println(kunde.getAdresse());
    }


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

    // Method to send a message to this client
    public void sendToClient(String message) {
        out.println(message);
    }



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
