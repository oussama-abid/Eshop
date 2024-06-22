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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;

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

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // Setup the login scene
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setPrefSize(400, 400);
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

        // Setup employee menu
        VBox mitarbeitermenuLayout = new VBox(10);
        mitarbeitermenuLayout.setPadding(new Insets(20));
        mitarbeitermenuLayout.setPrefSize(500, 500);
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
        mitarbeitermenuLayout.getChildren().addAll(
                addArtikelButton,
                artikelListeAnzeigenButton2,
                changeBestandButton,
                addNewMitarbeiterButton,
                showVerlaufButton,
                shopHistoryAnsehenButton,
                kundenlisteAusgebenButton,
                mitarbeiterlisteAusgebenButton,
                mitarbeiterLogoutButton,
                artikelTableView
        );

        mitarbeitermenu = new Scene(mitarbeitermenuLayout);

        // Show the login scene initially
        stage.setScene(loginScene);
        stage.setTitle("Eshop");
        stage.show();
    }

    private TableView<Artikel> createArtikelTableView() {
        TableView<Artikel> tableView = new TableView<>();
        tableView.setPrefSize(900, 900);

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

        tableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, bestandColumn, preisColumn, packungsGrosseColumn);

        return tableView;
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
            shop.kaufen(authuser);
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


    private void handleLogout() {
        authuser = null;
        showAlert("Logout erfolgreich.");
        stage.setScene(loginScene);
    }

    private void handleAddArtikel() {
        Stage addArtikelStage = new Stage();
        addArtikelStage.setTitle("Artikel hinzufügen");

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
                addArtikelStage.close();
                zeigeArtikelliste();
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

        Scene addArtikelScene = new Scene(addArtikelLayout, 300, 400);
        addArtikelStage.setScene(addArtikelScene);
        addArtikelStage.show();
    }

    private void handleShopVerlauf() {
        // Implement shop history viewing logic
    }

    private void handleChangeBestand() {
        Stage changeBestandStage = new Stage();
        changeBestandStage.setTitle("Bestand ändern");

        VBox changeBestandLayout = new VBox(10);
        changeBestandLayout.setPadding(new Insets(20));

        TextField artikelNummerField = new TextField();
        artikelNummerField.setPromptText("Artikelnummer");

        TextField neuerBestandField = new TextField();
        neuerBestandField.setPromptText("Neuer Bestand");

        CheckBox massenartikelCheckBox = new CheckBox("Ist Massenartikel");

        TextField neuePackungsGrosseField = new TextField();
        neuePackungsGrosseField.setPromptText("Neue Packungsgröße");
        neuePackungsGrosseField.setDisable(true);

        massenartikelCheckBox.setOnAction(e -> neuePackungsGrosseField.setDisable(!massenartikelCheckBox.isSelected()));

        Button changeBestandButton = new Button("Bestand ändern");
        changeBestandButton.setOnAction(e -> {
            int artikelnummer;
            int neuerBestand;
            int neuePackungsGrosse = 0;
            boolean istMassenartikel = massenartikelCheckBox.isSelected();

            try {
                artikelnummer = Integer.parseInt(artikelNummerField.getText());
                neuerBestand = Integer.parseInt(neuerBestandField.getText());
                if (istMassenartikel) {
                    neuePackungsGrosse = Integer.parseInt(neuePackungsGrosseField.getText());
                    if (neuePackungsGrosse == 0) {
                        showAlert("Packungsgröße darf nicht null sein.");
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                showAlert("Bitte geben Sie gültige Werte ein.");
                return;
            }

            try {
                Artikel artikel = shop.findeArtikelDurchID(artikelnummer);
                if (artikel instanceof Massenartikel) {
                    ((Massenartikel) artikel).setPackungsGrosse(neuePackungsGrosse);
                }
                if (neuerBestand == 0) {
                    showAlert("Der Bestand darf nicht null sein.");
                    return;
                }
                shop.BestandAendern(artikelnummer, neuerBestand);
                showAlert("Bestand erfolgreich geändert.");
                changeBestandStage.close();
                zeigeArtikelliste(); // Aktualisiere die Artikel-Liste in der GUI
            } catch (Exception ex) {
                showAlert("Fehler beim Ändern des Bestands: " + ex.getMessage());
            }
        });

        changeBestandLayout.getChildren().addAll(
                new Label("Artikelnummer"), artikelNummerField,
                new Label("Neuer Bestand"), neuerBestandField,
                massenartikelCheckBox,
                new Label("Neue Packungsgröße"), neuePackungsGrosseField,
                changeBestandButton
        );

        Scene changeBestandScene = new Scene(changeBestandLayout, 300, 400);
        changeBestandStage.setScene(changeBestandScene);
        changeBestandStage.show();
    }

    private void addMitarbeiter() {
        Stage addMitarbeiterStage = new Stage();
        addMitarbeiterStage.setTitle("Mitarbeiter hinzufügen");

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
                addMitarbeiterStage.close();
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

        Scene addMitarbeiterScene = new Scene(addMitarbeiterLayout, 300, 250);
        addMitarbeiterStage.setScene(addMitarbeiterScene);
        addMitarbeiterStage.show();
    }

    private void shopHistorieAnsehen() {
        // Implement shop history viewing logic
    }

    private void showKundenListe() {
        Stage kundenListeStage = new Stage();
        kundenListeStage.setTitle("Kundenliste");

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

        VBox kundenListeLayout = new VBox(10);
        kundenListeLayout.setPadding(new Insets(20));
        kundenListeLayout.getChildren().add(kundenTableView);

        Scene kundenListeScene = new Scene(kundenListeLayout);
        kundenListeStage.setScene(kundenListeScene);
        kundenListeStage.show();
    }

    private void showMitarbeiterListe() {
        Stage mitarbeiterListeStage = new Stage();
        mitarbeiterListeStage.setTitle("Mitarbeiterliste");

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

        VBox mitarbeiterListeLayout = new VBox(10);
        mitarbeiterListeLayout.setPadding(new Insets(20));
        mitarbeiterListeLayout.getChildren().add(mitarbeiterTableView);

        Scene mitarbeiterListeScene = new Scene(mitarbeiterListeLayout);
        mitarbeiterListeStage.setScene(mitarbeiterListeScene);
        mitarbeiterListeStage.show();
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
