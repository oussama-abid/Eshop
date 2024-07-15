package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class eShopClient implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientObserver observer;

    public eShopClient(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void connect() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                // Nachricht vom Server verarbeiten
                if (observer != null) {
                    observer.update(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRequest(String request) {
        out.println(request);
    }

    public String getResponse() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public void setObserver(ClientObserver observer) {
        this.observer = observer;
    }
}
