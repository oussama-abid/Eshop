package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 9800;

        try {
            Socket socket = new Socket(serverAddress, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            String serverMessage = in.readLine();
            System.out.println("Server: " + serverMessage);
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            do {
                System.out.println("Enter 'a' to list customers, 'q' to quit:");
                userInput = userInputReader.readLine().trim().toLowerCase();
                out.println(userInput);


                if (userInput.equals("a")) {

                    String response = in.readLine();
                    int numCustomers = Integer.parseInt(response);

                    for (int i = 0; i < numCustomers; i++) {
                        String name = in.readLine();
                        String address = in.readLine();
                        System.out.println("Customer " + (i + 1) + ":");
                        System.out.println("Name: " + name);
                        System.out.println("Address: " + address);
                    }
                } else if (!userInput.equals("q")) {
                    System.out.println("Invalid input. Please enter 'a' or 'q'.");
                }

            } while (!userInput.equals("q"));

            // Close resources
            userInputReader.close();
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}