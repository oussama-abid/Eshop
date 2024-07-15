package Server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            EshopServer server = new EshopServer(8081); // Port hier ist 8081
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
