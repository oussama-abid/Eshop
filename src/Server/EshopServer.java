package Server;

import domain.EShop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * EshopServer class manages the server-side operations of eshop .
 * It listens for client connections, accepts them, and processes client requests.
 */
public class EshopServer {
    public final static int DEFAULT_PORT = 9801;
    protected int port;
    protected ServerSocket serverSocket;
    private EShop shop;
    private static List<ClientRequestProcessor> clients = new ArrayList<>();

    /**
     * Constructs an EshopServer instance with the specified port number.
     *
     * @param port The port number on which the server will listen for client connections.
     * @throws IOException If an I/O error occurs while creating the server socket.
     */
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
            throw e; // Rethrow the exception to indicate server startup failure
        }
    }

    /**
     * Accepts client connection requests .
     * Creates a ClientRequestProcessor for each connected client and starts a new thread for each processor.
     */
    public void acceptClientConnectRequests() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                ClientRequestProcessor clientProcessor = new ClientRequestProcessor(clientSocket, shop);
                clients.add(clientProcessor);
                Thread t = new Thread(clientProcessor);
                t.start();
            }
        } catch (IOException e) {
            System.out.println(e + "Error while listening for connections");
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to be broadcasted.
     */
    public static void broadcastMessage(String message) {
        for (ClientRequestProcessor client : clients) {
            client.sendToClient(message);
        }
    }

    /**
     * Main method to start the EshopServer.
     *
     * @param args
     */
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

    /**
     * Handles standard exit on failure.
     *
     * @param e   The exception that caused the failure.
     * @param msg The error message to display.
     */
    private static void fail(Exception e, String msg) {
        System.err.println(msg + ": " + e);
        System.exit(1);
    }
}
