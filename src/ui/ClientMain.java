package ui;

import Server.eShopClient;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientMain extends Application {

    private eShopClient client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Gui gui = new Gui();

        try {
            client = new eShopClient("localhost", 8081); // Sicherstellen, dass die Adresse und der Port korrekt sind
            client.connect();
            gui.setClient(client);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Verbindung zum Server fehlgeschlagen. Bitte starten Sie den Server und versuchen Sie es erneut.");
            return;
        }

        gui.start(primaryStage);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
