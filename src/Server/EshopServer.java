package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EshopServer {

    private ServerSocket serverSocket;
    private List<ClientObserver> clients;
    private ThreadPoolExecutor threadPool;

    public EshopServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new CopyOnWriteArrayList<>();
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10); // Konfigurieren Sie die Thread-Pool-Größe nach Bedarf
    }

    public void start() {
        System.out.println("Server started...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientRequestProcessor clientProcessor = new ClientRequestProcessor(clientSocket, this);
                threadPool.execute(clientProcessor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerClient(ClientObserver client) {
        clients.add(client);
    }

    public void notifyClients(String message) {
        for (ClientObserver client : clients) {
            client.update(message);
        }
    }

    public static void main(String[] args) {
        try {
            EshopServer server = new EshopServer(8081);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
