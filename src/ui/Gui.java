package ui;

import Entities.*;
import Exceptions.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.Optional;




public class Gui extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene registerScene;
    private Scene kundenmenu;
    private Scene mitarbeitermenu;

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

    private BorderPane mitarbeitermenuLayout;
    private TabPane tabPane;

    private TextField bezeichnungField;
    private TextField bestandField;
    private TextField preisField;
    private CheckBox massenartikelCheckBox;
    private TextField packungsGrosseField;

    private Nutzer authuser;
    private EShop shop = new EShop();

    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterlist = new ArrayList<>();
    private List<Event> ShopVerlauf = new ArrayList<>();
    private Map<LocalDate, Map<Artikel, Integer>> history = new HashMap<>();
    private ObservableList<Artikel> artikelListe;

    private TableView<Artikel> artikelTableView;

    public void start(Stage primaryStage) {
        stage = primaryStage;

        // Setup the login scene
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setPrefSize(400, 400);

        tabPane = new TabPane();

        loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username");
        loginPasswordField = new PasswordField(); // Change to PasswordField for better security
        loginPasswordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginNutzer());
        Hyperlink goToRegisterLink = new Hyperlink("Don't have an account? Register here");
        goToRegisterLink.setOnAction(e -> stage.setScene(registerScene));
        loginLayout.getChildren().addAll(new Label("Login"), loginUsernameField, loginPasswordField, loginButton, goToRegisterLink);
        loginScene = new Scene(loginLayout);

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
        goToLoginLink.setOnAction(e -> stage.setScene(loginScene));

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

        // Setup the artikelTableView
        artikelTableView = createArtikelTableView();

        // Setup customer menu
        Button cartButton = new Button("Warenkorb");
        cartButton.setOnAction(e -> handleCart());
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        Button artikelListeAnzeigenButton = new Button("Artikel Liste anzeigen");
        artikelListeAnzeigenButton.setOnAction(e -> zeigeArtikelliste());
        Button zumWarenkorbHinzufügenButton = new Button("Artikel zum Warenkorb Hinzufügen");
        zumWarenkorbHinzufügenButton.setOnAction(e -> inWarenKorbLegen());

        VBox kundenmenuLayout = new VBox(10);
        kundenmenuLayout.setPadding(new Insets(20));
        kundenmenuLayout.setPrefSize(500, 500);

        // Adding buttons and artikelTableView to the layout
        kundenmenuLayout.getChildren().addAll(cartButton, artikelListeAnzeigenButton, logoutButton, zumWarenkorbHinzufügenButton, artikelTableView);

        kundenmenu = new Scene(kundenmenuLayout);
        artikelTableView = createArtikelTableView();

        // Setup employee menu
        mitarbeitermenuLayout = new BorderPane();
        mitarbeitermenuLayout.setPadding(new Insets(20));
        mitarbeitermenuLayout.setPrefSize(500, 500);
        VBox buttonBox = new VBox(10);

        Button addArtikelButton = new Button("Artikel hinzufügen");
        addArtikelButton.setOnAction(e -> handleAddArtikel());

        Button artikelListeAnzeigenButton2 = new Button("Artikel Liste anzeigen");
        artikelListeAnzeigenButton2.setOnAction(e -> zeigeArtikelliste());

        Button changeBestandButton = new Button("Bestand ändern");
        changeBestandButton.setOnAction(e -> handleChangeBestand());

        Button addNewMitarbeiterButton = new Button("Mitarbeiter hinzufügen");
        addNewMitarbeiterButton.setOnAction(e -> addMitarbeiter());

        Button showVerlaufButton = new Button("Shop Verlauf ansehen");
        showVerlaufButton.setOnAction(e -> handleShopVerlauf());

        Button shopHistoryAnsehenButton = new Button("Shop Historie ansehen");
        shopHistoryAnsehenButton.setOnAction(e -> shopHistorieAnsehen());

        Button kundenlisteAusgebenButton = new Button("Kundenliste ausgeben");
        kundenlisteAusgebenButton.setOnAction(e -> showKundenListe());

        Button mitarbeiterlisteAusgebenButton = new Button("Mitarbeiterliste ausgeben");
        mitarbeiterlisteAusgebenButton.setOnAction(e -> showMitarbeiterListe());

        Button mitarbeiterLogoutButton = new Button("Logout");
        mitarbeiterLogoutButton.setOnAction(e -> handleLogout());

        buttonBox.getChildren().addAll(
                addArtikelButton,
                artikelListeAnzeigenButton2,
                changeBestandButton,
                addNewMitarbeiterButton,
                showVerlaufButton,
                shopHistoryAnsehenButton,
                kundenlisteAusgebenButton,
                mitarbeiterlisteAusgebenButton,
                mitarbeiterLogoutButton
        );

        mitarbeitermenuLayout.setLeft(buttonBox);
        mitarbeitermenuLayout.setBottom(artikelTableView); // Always show the article list at the bottom

        mitarbeitermenu = new Scene(mitarbeitermenuLayout);

        // Show the login scene initially
        stage.setScene(loginScene); // Ensure the login scene is set initially
        stage.setTitle("Eshop");
        stage.show();
    }

    private TableView<Artikel> createArtikelTableView() {
        TableView<Artikel> tableView = new TableView<>();
        tableView.setPrefSize(500, 500);

        TableColumn<Artikel, Integer> artikelNummerColumn = new TableColumn<>("Artikel Nummer");
        artikelNummerColumn.setCellValueFactory(new PropertyValueFactory<>("artikelnummer"));

        TableColumn<Artikel, String> bezeichnungColumn = new TableColumn<>("Bezeichnung");
        bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));

        TableColumn<Artikel, Integer> bestandColumn = new TableColumn<>("Bestand");
        bestandColumn.setCellValueFactory(new PropertyValueFactory<>("bestand"));

        TableColumn<Artikel, Double> preisColumn = new TableColumn<>("Preis");
        preisColumn.setCellValueFactory(new PropertyValueFactory<>("preis"));

        TableColumn<Artikel, String> packungsGrosseColumn = new TableColumn<>("Packungsgröße");
        packungsGrosseColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Massenartikel) {
                return new SimpleObjectProperty<>(String.valueOf(((Massenartikel) cellData.getValue()).getPackungsGrosse()));
            } else {
                return new SimpleObjectProperty<>("");
            }
        });

        TableColumn<Artikel, Void> changeStockColumn = new TableColumn<>("Bestand ändern");
        changeStockColumn.setCellFactory(col -> new TableCell<Artikel, Void>() {
            private final Button changeButton = new Button("+");

            {
                changeButton.setOnAction(e -> {
                    Artikel artikel = getTableView().getItems().get(getIndex());
                    openChangeStockDialog(artikel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(changeButton);
                }
            }
        });

        tableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, bestandColumn, preisColumn, packungsGrosseColumn, changeStockColumn);

        return tableView;
    }

    private void openChangeStockDialog(Artikel artikel) {
        VBox changeBestandLayout = new VBox(10);
        changeBestandLayout.setPadding(new Insets(20));

        TextField bestandField = new TextField();
        bestandField.setPromptText("Bestand (positive für Einlagerung, negative für Auslagerung)");

        CheckBox massenartikelCheckBox = new CheckBox("Ist Massenartikel");
        TextField packungsGrosseField = new TextField();
        packungsGrosseField.setPromptText("Neue Packungsgröße");
        packungsGrosseField.setDisable(true);

        massenartikelCheckBox.setOnAction(e -> packungsGrosseField.setDisable(!massenartikelCheckBox.isSelected()));

        Button changeBestandButton = new Button("Bestand ändern");
        changeBestandButton.setOnAction(e -> {
            try {
                // Validate and parse stock change quantity
                int bestand;
                try {
                    bestand = Integer.parseInt(bestandField.getText());
                } catch (NumberFormatException ex) {
                    showAlert("Bitte geben Sie eine gültige Zahl für den Bestand ein.");
                    return;
                }

                // Update stock and log event
                shop.BestandAendern(artikel.getArtikelnummer(), bestand);
                String ereignisTyp = bestand >= 0 ? "Einlagerung" : "Auslagerung";
                shop.Ereignisfesthalten(ereignisTyp, artikel, Math.abs(bestand), authuser);

                // Optionally update package size for mass articles
                if (artikel instanceof Massenartikel && massenartikelCheckBox.isSelected()) {
                    try {
                        int neuePackungsGrosse = Integer.parseInt(packungsGrosseField.getText());
                        if (neuePackungsGrosse <= 0) {
                            showAlert("Die Packungsgröße muss größer als 0 sein.");
                            return;
                        }
                        ((Massenartikel) artikel).setPackungsGrosse(neuePackungsGrosse);
                    } catch (NumberFormatException ex) {
                        showAlert("Bitte geben Sie eine gültige Zahl für die Packungsgröße ein.");
                        return;
                    }
                }

                // Show success message and close the dialog
                showAlert("Lagerbestand für Artikel " + artikel.getArtikelnummer() + " aktualisiert. Neuer Bestand: " + artikel.getBestand());
                zeigeArtikelliste();
                artikelTableView.refresh();
                mitarbeitermenuLayout.setCenter(null); // Clear the center content
            } catch (Artikelnichtgefunden ex) {
                showAlert("Fehler: " + ex.getMessage());
            } catch (Exception ex) {
                showAlert("Fehler: " + ex.getMessage());
            }
        });

        changeBestandLayout.getChildren().addAll(
                new Label("Bestand"), bestandField,
                massenartikelCheckBox,
                new Label("Packungsgröße"), packungsGrosseField,
                changeBestandButton
        );

        mitarbeitermenuLayout.setCenter(changeBestandLayout); // Update the center content
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
        try {
            Nutzer nutzer = shop.login(benutzerkennung, passwort);
            authuser = nutzer;
            showMainMenu();
        } catch (FalscheLoginDaten e) {
            showAlert(e.getMessage());
        }
    }

    private void showMainMenu() {
        if (authuser instanceof Kunde) {
            stage.setScene(kundenmenu);
            zeigeArtikelliste();
        } else if (authuser instanceof Mitarbeiter) {
            stage.setScene(mitarbeitermenu);
            zeigeArtikelliste();  // Load articles when employee menu is shown
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
        artikelListe = FXCollections.observableArrayList(shop.getArtikelListe());
        if (artikelListe == null || artikelListe.isEmpty()) {
            System.out.println("No articles found in the shop.");
        } else {
            System.out.println("Articles found: " + artikelListe.size());
        }
        artikelTableView.setItems(artikelListe);
    }


    private void handleCart() {
        Stage cartStage = new Stage();
        cartStage.setTitle("Warenkorb");

        VBox cartLayout = new VBox(10);
        cartLayout.setPadding(new Insets(20));

        TableView<WarenkorbArtikel> cartTableView = new TableView<>();
        cartTableView.setPrefSize(600, 400);

        TableColumn<WarenkorbArtikel, Integer> artikelNummerColumn = new TableColumn<>("Artikel Nummer");
        artikelNummerColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArtikel().getArtikelnummer()));

        TableColumn<WarenkorbArtikel, String> bezeichnungColumn = new TableColumn<>("Bezeichnung");
        bezeichnungColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArtikel().getBezeichnung()));

        TableColumn<WarenkorbArtikel, Integer> anzahlColumn = new TableColumn<>("Anzahl");
        anzahlColumn.setCellValueFactory(new PropertyValueFactory<>("anzahl"));

        TableColumn<WarenkorbArtikel, Float> preisColumn = new TableColumn<>("Preis");
        preisColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArtikel().getPreis()));

        TableColumn<WarenkorbArtikel, Float> gesamtPreisColumn = new TableColumn<>("Gesamtpreis");
        gesamtPreisColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArtikel().getPreis() * cellData.getValue().getAnzahl()));

        cartTableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, anzahlColumn, preisColumn, gesamtPreisColumn);

        // Load the cart items
        Warenkorb warenkorb = shop.getWarenkorb(authuser);
        ObservableList<WarenkorbArtikel> cartData = FXCollections.observableArrayList(warenkorb.getWarenkorbListe());
        cartTableView.setItems(cartData);

        // Controls for modifying cart
        TextField artikelNummerField = new TextField();
        artikelNummerField.setPromptText("Artikelnummer");

        TextField neueAnzahlField = new TextField();
        neueAnzahlField.setPromptText("Neue Anzahl");

        Button updateQuantityButton = new Button("Anzahl ändern");
        updateQuantityButton.setOnAction(e -> {
            String artikelNummer = artikelNummerField.getText();
            int neueAnzahl;
            try {
                neueAnzahl = Integer.parseInt(neueAnzahlField.getText());
                shop.artikelMengeaendern(Integer.parseInt(artikelNummer), neueAnzahl, authuser);
                showAlert("Anzahl erfolgreich geändert.");
                cartData.setAll(warenkorb.getWarenkorbListe());
            } catch (NumberFormatException ex) {
                showAlert("Bitte geben Sie eine gültige Anzahl ein.");
            } catch (Exception ex) {
                showAlert("Fehler beim Ändern der Anzahl: " + ex.getMessage());
            }
        });

        Button removeButton = new Button("Artikel entfernen");
        removeButton.setOnAction(e -> {
            String artikelNummer = artikelNummerField.getText();
            try {
                if (shop.checkArtikelwarenkorb(artikelNummer, authuser)) {
                    shop.artikelMengeaendern(Integer.parseInt(artikelNummer), 0, authuser); // Setting the quantity to 0 to remove the item
                    showAlert("Artikel erfolgreich entfernt.");
                    cartData.setAll(warenkorb.getWarenkorbListe());
                } else {
                    showAlert("Artikel nicht im Warenkorb gefunden.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Bitte geben Sie eine gültige Artikelnummer ein.");
            } catch (Exception ex) {
                showAlert("Fehler beim Entfernen des Artikels: " + ex.getMessage());
            }
        });

        Button checkoutButton = new Button("Kaufen");
        checkoutButton.setOnAction(e -> {
            kaufen(); // Call the new kaufen method
            showAlert("Einkauf erfolgreich. Rechnung wurde erstellt.");
            zeigeArtikelliste(); // Aktualisiere die Artikelliste nach dem Kauf
            cartStage.close();
        });

        Button clearCartButton = new Button("Warenkorb leeren");
        clearCartButton.setOnAction(e -> {
            shop.Warenkorbleeren(authuser);
            showAlert("Warenkorb erfolgreich geleert.");
            cartData.clear();
        });

        HBox modifyBox = new HBox(10);
        modifyBox.getChildren().addAll(artikelNummerField, neueAnzahlField, updateQuantityButton, removeButton);

        HBox actionBox = new HBox(10);
        actionBox.getChildren().addAll(checkoutButton, clearCartButton);

        // Total price label
        Label totalPriceLabel = new Label("Gesamtpreis: " + warenkorb.calculateTotalPrice());

        cartLayout.getChildren().addAll(cartTableView, totalPriceLabel, modifyBox, actionBox);

        Scene cartScene = new Scene(cartLayout, 700, 500);
        cartStage.setScene(cartScene);
        cartStage.show();
    }
    private void kaufen() {
        Warenkorb warenkorb = shop.getWarenkorb(authuser);

        if (warenkorb.getWarenkorbListe().isEmpty()) {
            showAlert("Ihr Warenkorb ist leer. Bitte fügen Sie Artikel hinzu, bevor Sie fortfahren.");
            return;
        }

        shop.articlebestandanderen(authuser);
        shop.kundeEreignisfesthalten("Auslagerung", authuser);
        shop.kaufen(authuser);
    }


    private void handleLogout() {
        authuser = null;
        showAlert("Logout erfolgreich.");
        stage.setScene(loginScene);
    }

    private void handleAddArtikel() {
        VBox addArtikelLayout = new VBox(10);
        addArtikelLayout.setPadding(new Insets(20));

        TextField bezeichnungField = new TextField();
        bezeichnungField.setPromptText("Bezeichnung");

        TextField bestandField = new TextField();
        bestandField.setPromptText("Bestand");

        TextField preisField = new TextField();
        preisField.setPromptText("Preis");

        CheckBox massenartikelCheckBox = new CheckBox("Ist Massenartikel");

        TextField packungsGrosseField = new TextField();
        packungsGrosseField.setPromptText("Packungsgröße");
        packungsGrosseField.setDisable(true);

        massenartikelCheckBox.setOnAction(e -> packungsGrosseField.setDisable(!massenartikelCheckBox.isSelected()));

        Button addArtikelButton = new Button("Artikel hinzufügen");
        addArtikelButton.setOnAction(e -> {
            String bezeichnung = bezeichnungField.getText();
            int bestand;
            float preis;
            int packungsGrosse = 0;
            boolean istMassenartikel = massenartikelCheckBox.isSelected();

            try {
                bestand = Integer.parseInt(bestandField.getText());
                preis = Float.parseFloat(preisField.getText());
                if (istMassenartikel) {
                    packungsGrosse = Integer.parseInt(packungsGrosseField.getText());
                }
            } catch (NumberFormatException ex) {
                showAlert("Bitte geben Sie gültige Werte ein.");
                return;
            }

            try {
                shop.ArtikelHinzufuegen(bezeichnung, bestand, preis, istMassenartikel, packungsGrosse);
                showAlert("Artikel erfolgreich hinzugefügt.");
                zeigeArtikelliste(); // Aktualisiere die Artikelliste
                mitarbeitermenuLayout.setCenter(null); // Clear the center content
            } catch (Exception ex) {
                showAlert("Fehler beim Hinzufügen des Artikels: " + ex.getMessage());
            }
        });

        addArtikelLayout.getChildren().addAll(
                new Label("Bezeichnung"), bezeichnungField,
                new Label("Bestand"), bestandField,
                new Label("Preis"), preisField,
                massenartikelCheckBox,
                new Label("Packungsgröße"), packungsGrosseField,
                addArtikelButton
        );

        mitarbeitermenuLayout.setCenter(addArtikelLayout); // Update the center content
    }


    private void handleShopVerlauf() {
        Stage stage = new Stage();
        stage.setTitle("Shop Verlauf");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TableView<Event> eventTableView = new TableView<>();
        eventTableView.setPrefSize(800, 600);

        TableColumn<Event, String> operationColumn = new TableColumn<>("Operation");
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));

        TableColumn<Event, LocalDate> dateColumn = new TableColumn<>("Datum");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Event, String> artikelColumn = new TableColumn<>("Artikel");
        artikelColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArticle().getBezeichnung()));

        TableColumn<Event, Integer> quantityColumn = new TableColumn<>("Menge");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Event, String> nutzerColumn = new TableColumn<>("Nutzer");
        nutzerColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUser().getName()));

        eventTableView.getColumns().addAll(operationColumn, dateColumn, artikelColumn, quantityColumn, nutzerColumn);

        ObservableList<Event> eventList = FXCollections.observableArrayList(shop.ShopVerlaufAnzeigen());
        eventTableView.setItems(eventList);

        TextField filterField = new TextField();
        filterField.setPromptText("Filter by Artikel/Nutzer");

        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> {
            String filterText = filterField.getText().toLowerCase();
            if (filterText.isEmpty()) {
                eventTableView.setItems(eventList);
            } else {
                ObservableList<Event> filteredList = FXCollections.observableArrayList();
                for (Event event : eventList) {
                    if (event.getArticle().getBezeichnung().toLowerCase().contains(filterText) || event.getUser().getName().toLowerCase().contains(filterText)) {
                        filteredList.add(event);
                    }
                }
                eventTableView.setItems(filteredList);
            }
        });

        vbox.getChildren().addAll(new Label("Shop Verlauf"), filterField, filterButton, eventTableView);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void handleChangeBestand() {
        Stage changeBestandStage = new Stage();
        changeBestandStage.setTitle("Bestand ändern");

        VBox changeBestandLayout = new VBox(10);
        changeBestandLayout.setPadding(new Insets(20));

        TextField artikelNummerField = new TextField();
        artikelNummerField.setPromptText("Artikelnummer");

        TextField bestandField = new TextField();
        bestandField.setPromptText("Bestand (positive für Einlagerung, negative für Auslagerung)");

        CheckBox massenartikelCheckBox = new CheckBox("Ist Massenartikel");
        TextField packungsGrosseField = new TextField();
        packungsGrosseField.setPromptText("Neue Packungsgröße");
        packungsGrosseField.setDisable(true);

        massenartikelCheckBox.setOnAction(e -> packungsGrosseField.setDisable(!massenartikelCheckBox.isSelected()));

        Button changeBestandButton = new Button("Bestand ändern");
        changeBestandButton.setOnAction(e -> {
            try {
                // Validate and parse article number
                int artikelnummer = Integer.parseInt(artikelNummerField.getText());
                Artikel artikel = shop.findeArtikelDurchID(artikelnummer);

                if (artikel == null) {
                    showAlert("Artikel nicht gefunden.");
                    return;
                }

                // Validate and parse stock change quantity
                int bestand;
                try {
                    bestand = Integer.parseInt(bestandField.getText());
                } catch (NumberFormatException ex) {
                    showAlert("Bitte geben Sie eine gültige Zahl für den Bestand ein.");
                    return;
                }

                // Update stock and log event
                shop.BestandAendern(artikelnummer, bestand);
                String ereignisTyp = bestand >= 0 ? "Einlagerung" : "Auslagerung";
                shop.Ereignisfesthalten(ereignisTyp, artikel, Math.abs(bestand), authuser);

                // Optionally update package size for mass articles
                if (artikel instanceof Massenartikel && massenartikelCheckBox.isSelected()) {
                    try {
                        int neuePackungsGrosse = Integer.parseInt(packungsGrosseField.getText());
                        if (neuePackungsGrosse <= 0) {
                            showAlert("Die Packungsgröße muss größer als 0 sein.");
                            return;
                        }
                        ((Massenartikel) artikel).setPackungsGrosse(neuePackungsGrosse);
                    } catch (NumberFormatException ex) {
                        showAlert("Bitte geben Sie eine gültige Zahl für die Packungsgröße ein.");
                        return;
                    }
                }

                // Show success message and close the stage
                showAlert("Lagerbestand für Artikel " + artikelnummer + " aktualisiert. Neuer Bestand: " + artikel.getBestand());
                changeBestandStage.close();
                zeigeArtikelliste();
                artikelTableView.refresh();
            } catch (Artikelnichtgefunden ex) {
                showAlert("Fehler: " + ex.getMessage());
            } catch (Exception ex) {
                showAlert("Fehler: " + ex.getMessage());
            }
        });

        changeBestandLayout.getChildren().addAll(
                new Label("Artikelnummer"), artikelNummerField,
                new Label("Bestand"), bestandField,
                massenartikelCheckBox,
                new Label("Packungsgröße"), packungsGrosseField,
                changeBestandButton
        );

        Scene changeBestandScene = new Scene(changeBestandLayout, 400, 300);
        changeBestandStage.setScene(changeBestandScene);
        changeBestandStage.show();
    }


    private void addMitarbeiter() {
        VBox addMitarbeiterLayout = new VBox(10);
        addMitarbeiterLayout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField benutzerkennungField = new TextField();
        benutzerkennungField.setPromptText("Benutzerkennung");

        PasswordField passwortField = new PasswordField();
        passwortField.setPromptText("Passwort");

        Button addMitarbeiterButton = new Button("Mitarbeiter hinzufügen");
        addMitarbeiterButton.setOnAction(e -> {
            String name = nameField.getText();
            String benutzerkennung = benutzerkennungField.getText();
            String passwort = passwortField.getText();

            if (name.isEmpty() || benutzerkennung.isEmpty() || passwort.isEmpty()) {
                showAlert("Bitte füllen Sie alle Felder aus.");
                return;
            }

            try {
                shop.checkUniqueUsername(benutzerkennung);
                shop.registriereMitarbeiter(name, benutzerkennung, passwort);
                showAlert("Mitarbeiter erfolgreich hinzugefügt.");
                mitarbeitermenuLayout.setCenter(null); // Clear the center content
            } catch (NutzernameExistiertBereits ex) {
                showAlert("Benutzerkennung existiert bereits.");
            } catch (Exception ex) {
                showAlert("Fehler beim Hinzufügen des Mitarbeiters: " + ex.getMessage());
            }
        });

        addMitarbeiterLayout.getChildren().addAll(
                new Label("Name"), nameField,
                new Label("Benutzerkennung"), benutzerkennungField,
                new Label("Passwort"), passwortField,
                addMitarbeiterButton
        );

        mitarbeitermenuLayout.setCenter(addMitarbeiterLayout); // Update the center content
    }
    private void shopHistorieAnsehen() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<Artikel> artikelComboBox = new ComboBox<>(FXCollections.observableArrayList(shop.getArtikelListe()));
        artikelComboBox.setPromptText("Artikel auswählen");

        NumberAxis xAxis = new NumberAxis(1, 30, 1);
        xAxis.setLabel("Tage");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Bestand");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Bestandshistorie");

        Button showHistoryButton = new Button("Historie anzeigen");
        showHistoryButton.setOnAction(e -> {
            Artikel selectedArtikel = artikelComboBox.getSelectionModel().getSelectedItem();
            if (selectedArtikel != null) {
                drawStockHistory(lineChart, selectedArtikel);
            } else {
                showAlert("Bitte wählen Sie einen Artikel aus.");
            }
        });

        vbox.getChildren().addAll(new Label("Shop Historie"), artikelComboBox, showHistoryButton, lineChart);

        mitarbeitermenuLayout.setCenter(vbox); // Update the center content
    }

    private void drawStockHistory(LineChart<Number, Number> lineChart, Artikel artikel) {
        List<Artikelhistory> historyList = shop.ShophistoryAnzeigen().stream()
                .filter(history -> history.getArticle().getArtikelnummer() == artikel.getArtikelnummer())
                .collect(Collectors.toList());

        Optional<LocalDate> earliestDateOpt = historyList.stream()
                .map(Artikelhistory::getDate)
                .min(LocalDate::compareTo);

        if (!earliestDateOpt.isPresent()) {
            showAlert("Keine Verlaufsdaten verfügbar.");
            return;
        }

        LocalDate earliestDate = earliestDateOpt.get();
        LocalDate today = LocalDate.now();

        int daysOfData = (int) ChronoUnit.DAYS.between(earliestDate, today) + 1;
        daysOfData = Math.min(daysOfData, 30);

        int[] stockData = new int[daysOfData];

        for (int i = 0; i < daysOfData; i++) {
            LocalDate date = today.minusDays(daysOfData - 1 - i);
            Optional<Artikelhistory> historyForDay = historyList.stream()
                    .filter(history -> history.getDate().equals(date))
                    .findFirst();
            stockData[i] = historyForDay.map(Artikelhistory::getTotalQuantity).orElse(0);
        }

        int initialStock = artikel.getBestand() - Arrays.stream(stockData).sum();
        int[] cumulativeStock = IntStream.range(0, daysOfData)
                .map(i -> initialStock + IntStream.of(stockData).limit(i + 1).sum())
                .toArray();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Bestand");

        for (int i = 0; i < daysOfData; i++) {
            series.getData().add(new XYChart.Data<>(i + 1, cumulativeStock[i]));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    private void showKundenListe() {
        VBox kundenListeLayout = new VBox(10);
        kundenListeLayout.setPadding(new Insets(20));

        TableView<Kunde> kundenTableView = new TableView<>();
        kundenTableView.setPrefSize(600, 400);

        TableColumn<Kunde, Integer> kundenNummerColumn = new TableColumn<>("Kundennummer");
        kundenNummerColumn.setCellValueFactory(new PropertyValueFactory<>("kundennummer"));

        TableColumn<Kunde, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Kunde, String> benutzerkennungColumn = new TableColumn<>("Benutzerkennung");
        benutzerkennungColumn.setCellValueFactory(new PropertyValueFactory<>("benutzerkennung"));

        TableColumn<Kunde, String> adresseColumn = new TableColumn<>("Adresse");
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        kundenTableView.getColumns().addAll(kundenNummerColumn, nameColumn, benutzerkennungColumn, adresseColumn);

        List<Kunde> kundenListe = shop.getKundenList();
        ObservableList<Kunde> kundenData = FXCollections.observableArrayList(kundenListe);
        kundenTableView.setItems(kundenData);

        kundenListeLayout.getChildren().add(kundenTableView);

        mitarbeitermenuLayout.setCenter(kundenListeLayout); // Update the center content
    }

    private void showMitarbeiterListe() {
        VBox mitarbeiterListeLayout = new VBox(10);
        mitarbeiterListeLayout.setPadding(new Insets(20));

        TableView<Mitarbeiter> mitarbeiterTableView = new TableView<>();
        mitarbeiterTableView.setPrefSize(600, 400);

        TableColumn<Mitarbeiter, Integer> mitarbeiterNummerColumn = new TableColumn<>("Mitarbeiternummer");
        mitarbeiterNummerColumn.setCellValueFactory(new PropertyValueFactory<>("mitarbeiternummer"));

        TableColumn<Mitarbeiter, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Mitarbeiter, String> benutzerkennungColumn = new TableColumn<>("Benutzerkennung");
        benutzerkennungColumn.setCellValueFactory(new PropertyValueFactory<>("benutzerkennung"));

        mitarbeiterTableView.getColumns().addAll(mitarbeiterNummerColumn, nameColumn, benutzerkennungColumn);

        List<Mitarbeiter> mitarbeiterListe = shop.getMitarbeiterlist();
        ObservableList<Mitarbeiter> mitarbeiterData = FXCollections.observableArrayList(mitarbeiterListe);
        mitarbeiterTableView.setItems(mitarbeiterData);

        mitarbeiterListeLayout.getChildren().add(mitarbeiterTableView);

        mitarbeitermenuLayout.setCenter(mitarbeiterListeLayout); // Update the center content
    }

    private void inWarenKorbLegen() {
        Stage stage = new Stage();
        stage.setTitle("Artikel in den Warenkorb legen");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label artikelLabel = new Label("Artikel auswählen:");
        ComboBox<Artikel> artikelComboBox = new ComboBox<>(FXCollections.observableArrayList(shop.getArtikelListe()));
        artikelComboBox.setPromptText("Artikel auswählen");

        Label anzahlLabel = new Label("Anzahl:");
        TextField anzahlField = new TextField();
        anzahlField.setPromptText("Anzahl");

        Button addButton = new Button("Hinzufügen");
        addButton.setOnAction(e -> {
            Artikel selectedArtikel = artikelComboBox.getSelectionModel().getSelectedItem();
            if (selectedArtikel == null) {
                showAlert("Bitte wählen Sie einen Artikel aus.");
                return;
            }

            try {
                int anzahl = Integer.parseInt(anzahlField.getText());
                if (anzahl > 0) {
                    if (selectedArtikel instanceof Massenartikel) {
                        Massenartikel massenartikel = (Massenartikel) selectedArtikel;
                        int packungsGrosse = massenartikel.getPackungsGrosse();
                        if (anzahl % packungsGrosse != 0) {
                            showAlert("Die Anzahl muss ein Vielfaches der Packungsgröße von " + packungsGrosse + " sein.");
                            return;
                        }
                    }
                    shop.inWarenKorbLegen(selectedArtikel, anzahl, authuser);
                    showAlert("Artikel wurde erfolgreich in den Warenkorb gelegt.");
                    stage.close(); // Schließe das Fenster nach erfolgreichem Hinzufügen
                    zeigeArtikelliste(); // Aktualisiere die Artikelliste nach dem Hinzufügen
                } else {
                    showAlert("Bitte geben Sie eine gültige Anzahl ein.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Bitte geben Sie eine gültige Zahl für die Anzahl ein.");
            } catch (AnzahlException ex) {
                showAlert(ex.getMessage());
            }
        });

        vbox.getChildren().addAll(artikelLabel, artikelComboBox, anzahlLabel, anzahlField, addButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }



}
