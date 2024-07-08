package Server;

import ui.EShop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EshopServer {

    public final static int DEFAULT_PORT = 9800;

    protected int port;
    protected ServerSocket serverSocket;
    private EShop shop;

    public EshopServer(int port) throws IOException {
        shop = new EShop();

        if (port == 0)
            port = DEFAULT_PORT;
        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
            InetAddress ia = InetAddress.getLocalHost();
            System.out.println("Host: " + ia.getHostName());
            System.out.println("Server *" + ia.getHostAddress() + "* is listening on port " + port);
        } catch (IOException e) {
            System.out.println(e + "An exception occurred while creating the server socket");
        }
    }

    public void acceptClientConnectRequests() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientRequestProcessor c = new ClientRequestProcessor(clientSocket, shop);
                Thread t = new Thread(c);
                t.start();
            }
        } catch (IOException e) {
            System.out.println(e + "Error while listening for connections");
        }
    }

    public static void main(String[] args) {
        int port = 0;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                port = 0;
            }
        }
        try {
            EshopServer server = new EshopServer(port);
            server.acceptClientConnectRequests();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e, " - Failed to create EshopServer");
        }
    }

    // Standard exit on failure:
    private static void fail(Exception e, String msg) {
        System.err.println(msg + ": " + e);
        System.exit(1);
    }
}
