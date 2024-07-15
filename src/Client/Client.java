package Client;

import Entities.Nutzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private BufferedReader userInputReader;
    private Nutzer authuser;

    public Client() {
        this.authuser = null;
        String serverAddress = "localhost";
        int port = 9801;
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            userInputReader = new BufferedReader(new InputStreamReader(System.in));

            // Start login menu
            loginMenu();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginMenu() {
        try {
            String userInput;
            boolean menu = false;
            while (!menu) {
                if (authuser == null) {
                    System.out.print("Wollen Sie sich einloggen (L) oder den Shop verlassen (Q)? : ");
                    userInput = readInputAndSendToServer();
                    switch (userInput) {
                        case "L":
                            loginNutzer();
                            break;
                        case "Q":
                            closeConnections();
                            menu = true;
                            break;
                        default:
                            System.out.println("----------------------");
                    }
                } else {
                    closeConnections();
                    menu = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readInputAndSendToServer() throws IOException {
        String userInput = userInputReader.readLine().trim().toUpperCase(); // Adjusted to uppercase for case insensitivity
        out.println(userInput);
        return userInput;
    }

    private void loginNutzer() throws IOException {
        System.out.println("Geben Sie Ihre Login-Informationen ein:");
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = userInputReader.readLine();
        System.out.print("Passwort: ");
        String passwort = userInputReader.readLine();
        out.println("login," + benutzerkennung + "," + passwort);
        String response = in.readLine();
        if (response.equals("success")) {
            System.out.println("Login erfolgreich.");
            showMainMenu();

        } else {
            System.out.println("Login fehlgeschlagen: " + response);
        }
    }


    private void closeConnections() throws IOException {
        userInputReader.close();
        in.close();
        out.close();
        socket.close();
    }

    private void showMainMenu() throws IOException {
        try {
            String userInput;
            boolean menu = false;
            while (!menu) {
                System.out.println(" 1. Artikelliste ausgeben");
                System.out.println(" 2. Artikel hinzufuegen");
                System.out.println(" 3. Kundenliste ausgeben");
                System.out.println(" Q. close eshop");
                userInput = readInputAndSendToServer();
                switch (userInput) {
                    case "1":
                        displayartikelListe();
                        break;
                    case "2":
                        addartikel();
                        break;
                    case "3":
                        displayKundenListe();
                        break;

                    default:
                        System.out.println("----------------------");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void addartikel() throws IOException
    {
        System.out.println("Geben Sie neue Artikel ein:");
        System.out.print("Bezeichnung: ");
        String bezeichnung = userInputReader.readLine();

        System.out.print("Bestand: ");
        int bestand = Integer.parseInt(userInputReader.readLine());

        System.out.print("Preis: ");
        float preis = Float.parseFloat(userInputReader.readLine());
        boolean istMassenartikel = false;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Ist der artikel ein Massenartikel? (ja/nein): ");
            String input = userInputReader.readLine().trim().toLowerCase();
            switch (input) {
                case "ja":
                    istMassenartikel = true;
                    validInput = true;
                    break;
                case "nein":
                    istMassenartikel = false;
                    validInput = true;
                    break;
                default:
                    System.out.println("Fehler: Bitte geben Sie 'ja' oder 'nein'.");
            }
        }

        int packungsGrosse = 0;
        if (istMassenartikel) {
            System.out.print("Packungsgröße: ");
            packungsGrosse = Integer.parseInt(userInputReader.readLine());
        }

        out.println("addartikel," + bezeichnung + "," + bestand + "," + preis + "," + istMassenartikel + "," + packungsGrosse);


        String response = in.readLine();
        if (response.startsWith("Neuer Artikel hinzugefügt")) {
            System.out.println(response);
            showMainMenu();

        } else {
            System.out.println(response);
        }
    }

    private void displayartikelListe() throws IOException {
        String response = in.readLine();
        if (response.startsWith("artikellist,")) {
            String[] parts = response.split(",", 2);
            if (parts.length == 2) {
                int numCustomers = Integer.parseInt(parts[1].trim());
                System.out.println("nb articels " + numCustomers + ":");
                for (int i = 0; i < numCustomers; i++) {
                    String Artikelnummer = in.readLine();
                    String Bezeichnung = in.readLine();
                    String Bestand = in.readLine();
                    String preis = in.readLine();
                    System.out.println("artikel " + (i + 1) + ":");
                    System.out.println("Artikelnummer: " + Artikelnummer);
                    System.out.println("Bezeichnung: " + Bezeichnung);
                    System.out.println("Bestand: " + Bestand);
                    System.out.println("preis: " + preis);
                }
            } else {
                System.out.println("Invalid response format from server.");
            }
        } else {
            System.out.println("Unexpected response from server: " + response);
        }
    }
    private void displayKundenListe() throws IOException {
        String response = in.readLine();
        if (response.startsWith("customerlist,")) {
            String[] parts = response.split(",", 2);
            if (parts.length == 2) {
                int numCustomers = Integer.parseInt(parts[1].trim());
                for (int i = 0; i < numCustomers; i++) {
                    String name = in.readLine();
                    String address = in.readLine();
                    System.out.println("Kunde " + (i + 1) + ":");
                    System.out.println("Name: " + name);
                    System.out.println("Adresse: " + address);
                }
            } else {
                System.out.println("Invalid response format from server.");
            }
        } else {
            System.out.println("Unexpected response from server: " + response);
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
    }
}
