package ui;

import Server.EshopServer;
import Server.eShopClient;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // Starten Sie den Server in einem neuen Thread
        new Thread(() -> {
            try {
                EshopServer server = new EshopServer(8081);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Starten Sie die GUI
        Gui.launch(Gui.class, args);

        // Verbindung zum Server herstellen und sich als Client registrieren
        try {
            eShopClient client = new eShopClient("localhost", 8081);
            client.connect();
            // Registrieren Sie den Client beim Server
            // server.registerClient(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
