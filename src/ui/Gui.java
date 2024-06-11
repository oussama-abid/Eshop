package ui;

import Entities.*;
import Exceptions.NutzernameExistiertBereits;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gui extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene registerScene;
    private Scene kundenmenu;

    private TextField loginUsernameField;
    private TextField loginPasswordField;

    private TextField nameField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField streetField;
    private TextField cityField;
    private TextField stateField;
    private TextField zipCodeField;
    private TextField countryField;

    private Nutzer authuser;
    private EShop shop = new EShop();

    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterlist = new ArrayList<>();
    private List<Event> ShopVerlauf = new ArrayList<>();
    private Map<LocalDate, Map<Artikel, Integer>> history = new HashMap<>();
    private List<Artikel> artikelListe = new ArrayList<>();

    private TableView<Artikel> artikelTableView;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // Create login scene
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setPrefSize(400, 300);
        loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username");
        loginPasswordField = new TextField();
        loginPasswordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginNutzer());
        Hyperlink goToRegisterLink = new Hyperlink("Don't have an account? Register here");
        goToRegisterLink.setOnAction(e -> stage.setScene(registerScene));
        loginLayout.getChildren().addAll(new Label("Login"), loginUsernameField, loginPasswordField, loginButton, goToRegisterLink);
        loginScene = new Scene(loginLayout);

// Create register scene
        VBox registerLayout = new VBox(10);
        registerLayout.setPadding(new Insets(20));
        registerLayout.setPrefSize(400, 400);

        nameField = new TextField();
        nameField.setPromptText("Name");

        usernameField = new TextField();
        usernameField.setPromptText("benutzerkennung");

        passwordField = new PasswordField();
        passwordField.setPromptText("passwort");

        streetField = new TextField();
        streetField.setPromptText("straße");

        cityField = new TextField();
        cityField.setPromptText("stadt");

        stateField = new TextField();
        stateField.setPromptText("bundesland");

        zipCodeField = new TextField();
        zipCodeField.setPromptText("postleitzahl");

        countryField = new TextField();
        countryField.setPromptText("land");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerUser());

        Hyperlink goToLoginLink = new Hyperlink("Already have an account? Login here");
        goToLoginLink.setOnAction(e -> stage.close());

        registerLayout.getChildren().addAll(
                new Label("Register"),
                nameField,
                usernameField,
                passwordField,
                streetField,
                cityField,
                stateField,
                zipCodeField,
                countryField,
                registerButton,
                goToLoginLink
        );

        registerScene = new Scene(registerLayout);

        // Create TableView for displaying articles
        artikelTableView = new TableView<>();
        artikelTableView.setPrefSize(600, 400);

        // Create columns for TableView
        TableColumn<Artikel, String> artikelNummerColumn = new TableColumn<>("Artikel Nummer");
        artikelNummerColumn.setCellValueFactory(new PropertyValueFactory<>("artikelnummer"));

        TableColumn<Artikel, String> bezeichnungColumn = new TableColumn<>("Bezeichnung");
        bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));

        TableColumn<Artikel, Integer> bestandColumn = new TableColumn<>("Bestand");
        bestandColumn.setCellValueFactory(new PropertyValueFactory<>("bestand"));

        TableColumn<Artikel, Double> preisColumn = new TableColumn<>("Preis");
        preisColumn.setCellValueFactory(new PropertyValueFactory<>("preis"));

        // Add columns to TableView
        artikelTableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, bestandColumn, preisColumn);

        // Create header for kundenmenu
        Button cartButton = new Button("Cart");
        cartButton.setOnAction(e -> handleCart());
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        HBox header = new HBox(10);
        header.getChildren().addAll(cartButton, logoutButton);

        // Create layout for kundenmenu
        VBox kundenmenuLayout = new VBox(10);
        kundenmenuLayout.setPadding(new Insets(20));
        kundenmenuLayout.setPrefSize(400, 300);
        kundenmenuLayout.getChildren().addAll(header, artikelTableView);
        kundenmenu = new Scene(kundenmenuLayout);

        // Set initial scene
        stage.setScene(loginScene);
        stage.setTitle("Eshop");
        stage.show();
    }

    private void registerUser() {
        String name = nameField.getText();
        String benutzerkennung = usernameField.getText();
        String passwort = passwordField.getText();
        String straße = streetField.getText();
        String stadt = cityField.getText();
        String bundesland = stateField.getText();
        int postleitzahl = 0;
        try {
            postleitzahl = Integer.parseInt(zipCodeField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid postal code. Please enter a valid number.");
            return;
        }
        String land = countryField.getText();

        try {
            shop.checkUniqueUsername(benutzerkennung);
            shop.registriereKunde(name, benutzerkennung, passwort, straße, stadt, bundesland, postleitzahl, land);
            showAlert("Registration successful. You can now log in.");
            stage.setScene(loginScene);
        } catch (NutzernameExistiertBereits e) {
            showAlert(e.getMessage());
        }
    }



    private void loginNutzer() {
        String benutzerkennung = loginUsernameField.getText();
        String passwort = loginPasswordField.getText();

        Nutzer nutzer = shop.login(benutzerkennung, passwort);

        if (nutzer != null) {
            authuser = nutzer;
            showAlert("Login erfolgreich.");
            showMainMenu();
        } else {
            showAlert("Falscher Benutzername oder Passwort.");
        }
    }

    private void showMainMenu() {
        if (authuser instanceof Kunde) {
            stage.setScene(kundenmenu);
            zeigeArtikelliste();
        } else if (authuser instanceof Mitarbeiter) {
            //stage.setScene(mitarbeitermenu);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void zeigeArtikelliste() {
        artikelListe = shop.getArtikelListe();
        ObservableList<Artikel> artikelData = FXCollections.observableArrayList(artikelListe);
        artikelTableView.setItems(artikelData);
    }

    private void handleCart() {
        // Handle cart button action
    }

    private void handleLogout() {
        authuser = null;
        showAlert("Logout erfolgreich.");
        stage.setScene(loginScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
