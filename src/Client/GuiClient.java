package Client;

import Entities.Artikel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class GuiClient extends Application {

    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private Stage primaryStage; // Reference to the primary stage
    private VBox header;
    private VBox mainLayout;
    private TableView<Artikel> artikelTable;
    private TextField loginUsernameField = new TextField();
    private PasswordField loginPasswordField = new PasswordField();
    private ServerRequest serverRequest;
    private Thread messageListenerThread;
    public GuiClient() {
        String serverAddress = "localhost";
        int port = 9800;
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            serverRequest = new ServerRequest(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize mainLayout
        mainLayout = new VBox();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");
        // Initially show login menu
        showLoginMenu();
    }

    private void showLoginMenu() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPrefSize(400, 400);
        loginLayout.setSpacing(10);
        loginLayout.setStyle("-fx-padding: 20;");
        loginUsernameField.setPromptText("Username");
        loginPasswordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginNutzer());

        loginLayout.getChildren().addAll(new Label("Login"), loginUsernameField, loginPasswordField, loginButton);
        Scene loginScene = new Scene(loginLayout);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void loginNutzer() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        new Thread(() -> {
            try {
                out.println("login," + username + "," + password);
                String response = in.readLine();
                Platform.runLater(() -> {
                    if (response.equals("success")) {
                        showMainMenu();
                    } else {
                        showAlert("Login failed: " + response);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showMainMenu() {
        if (header == null) {
            header = new VBox();
        }
        header.getChildren().clear();

        Button artikelButton = new Button("Artikel");
        artikelButton.setOnAction(e -> showArtikelSection());

        Button kundeManagementButton = new Button("Kunde Management");
        kundeManagementButton.setOnAction(e -> showKundeManagementSection());

        HBox navBar = new HBox(10);
        navBar.getChildren().addAll(artikelButton, kundeManagementButton);
        navBar.getStyleClass().add("nav-bar");

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> handleLogout());

        HBox mitarbeiterHeader = new HBox(10);
        mitarbeiterHeader.getChildren().addAll(navBar, logoutButton);
        HBox.setHgrow(navBar, Priority.ALWAYS);
        mitarbeiterHeader.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        header.getChildren().add(mitarbeiterHeader);

        if (mainLayout == null) {
            mainLayout = new VBox();
        }
        mainLayout.setPrefSize(800, 600); // Set preferred size for main layout
        mainLayout.getChildren().clear();
        mainLayout.getChildren().addAll(header);

        showArtikelSection(); // Display initial section

        Scene mainScene = new Scene(mainLayout);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        // Start listening for server messages
        //  listenForServerMessages();
    }

    private void listenForServerMessages() {
        new Thread(() -> {
            try {
                String message;
                while (!Thread.interrupted()) { // Check interruption flag to exit the loop
                    message = in.readLine();
                    if (message == null) {
                        break; // Exit loop if end of stream is reached
                    }
                    if (message.startsWith("Neuer Artikel hinzugefügt: ")) {
                        final String alertMessage = "New article added: " + message.substring(24);
                        Platform.runLater(() -> showArtikelSection()
                        );
                        showArtikelSection();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle IOException gracefully
            }
        }).start();
    }




    private void showArtikelSection() {
        // Clear existing content
        mainLayout.getChildren().clear();
        mainLayout.setStyle("-fx-padding: 10;");

        VBox artikelContent = new VBox(10);
        artikelContent.setPadding(new Insets(10));

        Label titleLabel = new Label("Artikel List");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        artikelContent.getChildren().add(titleLabel);

        artikelTable = new TableView<>();
        TableColumn<Artikel, Integer> artikelnummerCol = new TableColumn<>("Artikelnummer");
        artikelnummerCol.setCellValueFactory(new PropertyValueFactory<>("artikelnummer"));

        TableColumn<Artikel, String> bezeichnungCol = new TableColumn<>("Bezeichnung");
        bezeichnungCol.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));

        TableColumn<Artikel, Integer> bestandCol = new TableColumn<>("Bestand");
        bestandCol.setCellValueFactory(new PropertyValueFactory<>("bestand"));

        TableColumn<Artikel, Float> preisCol = new TableColumn<>("Preis");
        preisCol.setCellValueFactory(new PropertyValueFactory<>("preis"));

        TableColumn<Artikel, Boolean> istMassenartikelCol = new TableColumn<>("Ist Massenartikel");
        istMassenartikelCol.setCellValueFactory(new PropertyValueFactory<>("istMassenartikel"));

        artikelTable.getColumns().addAll(artikelnummerCol, bezeichnungCol, bestandCol, preisCol, istMassenartikelCol);
        artikelContent.getChildren().add(artikelTable);

        mainLayout.getChildren().addAll(header, artikelContent);

        // Fetch articles from server asynchronously
        new Thread(() -> {
            try {
                List<Artikel> articles = serverRequest.requestArticleList();

                // Update UI on JavaFX application thread
                Platform.runLater(() -> {
                    // Access artikelTable and update its items
                    if (artikelTable != null) {
                        artikelTable.getItems().setAll(articles);
                    } else {
                        System.err.println("artikelTable is null, cannot update items.");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception appropriately
            }
        }).start();
    }
    private void showKundeManagementSection() {
        // Implement functionality for showing customer management section
        showAlert("Kundenverwaltung wird in Zukunft implementiert.");
    }

    private void handleLogout() {
        // Implement logout functionality
        showLoginMenu(); // After logout, go back to login screen
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        closeConnections();
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
