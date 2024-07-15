package Client;

import Entities.Artikel;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerRequest {

    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private Thread messageListenerThread;
    private boolean newartikel = false;

    private GuiClient guiClient;

    public ServerRequest(GuiClient guiClient) {
        this.guiClient = guiClient;
        startconnection();
    }

    public List<Artikel> requestArticleList() throws IOException, InterruptedException {
        final List<Artikel> articles = new ArrayList<>();

        Thread thread = new Thread(() -> {
            try {
                out.println("1"); // Send command to request article list
                String response = in.readLine(); // Read response from server

                if (response.startsWith("artikellist,")) {
                    String[] parts = response.split(",", 2);
                    if (parts.length == 2) {
                        int numArticles = Integer.parseInt(parts[1].trim());

                        for (int i = 0; i < numArticles; i++) {
                            String artikelData = in.readLine();
                            String[] artikelParts = artikelData.split(",");
                            int artikelnummer = Integer.parseInt(artikelParts[0]);
                            String bezeichnung = artikelParts[1];
                            int bestand = Integer.parseInt(artikelParts[2]);
                            float preis = Float.parseFloat(artikelParts[3]);
                            boolean istMassenartikel = Boolean.parseBoolean(artikelParts[4]);
                            articles.add(new Artikel(artikelnummer, bezeichnung, bestand, preis, istMassenartikel));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception appropriately
            }
        });
        thread.start();
        thread.join(); // Wait for the thread to finish
      //  listenForServerMessages();
        return articles;
    }
    private void listenForServerMessages() {
        new Thread(() -> {
            try {
                String message;
                while (!Thread.interrupted()) { // Check interruption flag to exit the loop
                    message = in.readLine();
                    if (message == null) {
                        break; // Exit loop if end of stream is reached
                    }
                    if (message.startsWith("Neuer Artikel hinzugefügt: ")) {
                        final String alertMessage = "New article added: " + message.substring(24);
                        Platform.runLater(() -> {
                            guiClient.showArtikelSection();

                        });
                        guiClient.showArtikelSection();


                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle IOException gracefully
            }
        }).start();
    }

    public boolean newarticle() {
        return newartikel;
    }

    public String login(String username, String password) throws IOException {
        out.println("login," + username + "," + password);
        String response = in.readLine();
        return response;
    }

    public String addarticle(String bezeichnung, int bestand, float preis, boolean istMassenartikel) throws IOException {
        out.println("addartikel," + bezeichnung + "," + bestand + "," + preis + "," + istMassenartikel + "," + 0);
        String response = in.readLine();
        return response;
    }

    public void startconnection() {
        String serverAddress = "localhost";
        int port = 9801;
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnections() {
        try {
            if (in!= null) in.close();
            if (out!= null) out.close();
            if (socket!= null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}