package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestProcessor implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private EshopServer server;

    public ClientRequestProcessor(Socket socket, EshopServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received request: " + request);
                String response = handleRequest(request);
                out.println(response);

                // Benachrichtigen Sie alle Clients über die Änderung
                server.notifyClients("Client made a change: " + request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleRequest(String request) {
        // Hier wird die Anfrage verarbeitet und eine Antwort generiert
        System.out.println("Handling request: " + request);
        return "Request received: " + request;
    }
}
