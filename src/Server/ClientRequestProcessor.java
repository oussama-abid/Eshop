package Server;

import Entities.Kunde;
import ui.EShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

class ClientRequestProcessor implements Runnable {

    private EShop eShop;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;

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
    }

    public void run() {
        String input = "";

        out.println("Server to Client: Ready for  requests!");

        do {
            try {
                input = in.readLine();
            } catch (Exception e) {
                System.out.println("--->Error reading from client (action): ");
                System.out.println(e.getMessage());
                continue;
            }

            // Process input:
            if (input == null) {
                input = "q";
            } else if (input.equals("a")) {
                displayKunden();
            }

        } while (!input.equals("q"));

        disconnect();
    }

    private void displayKunden() {
        List<Kunde> kunden = eShop.getKundenList();
        sendKundenToClient(kunden);
    }

    private void sendKundenToClient(List<Kunde> kunden) {

        out.println(kunden.size());
        for (Kunde kunde : kunden) {
            sendKundeToClient(kunde);
        }
    }

    private void sendKundeToClient(Kunde kunde) {
        out.println(kunde.getName());
        out.println(kunde.getAdresse());
    }

    private void disconnect() {
        try {
            out.println("session end");
            clientSocket.close();

            System.out.println("Connection to " + clientSocket.getInetAddress()
                    + ":" + clientSocket.getPort() + " closed by client");
        } catch (Exception e) {
            System.out.println("--->Error closing connection: ");
            System.out.println(e.getMessage());
            out.println("Error");
        }
    }
}
