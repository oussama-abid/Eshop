package ui;

import Entities.*;
import Exceptions.AnzahlException;
import Exceptions.FalscheLoginDaten;
import Exceptions.NutzernameExistiertBereits;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gui extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene registerScene;
    private Scene mainScene;

    private TextField loginUsernameField;
    private PasswordField loginPasswordField;

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



    private VBox header;
    private Button cartButton;
    private Label cartItemCountLabel;


    private Nutzer authuser;
    private EShop shop = new EShop();

    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
    private List<Event> shopVerlauf = new ArrayList<>();
    private Map<LocalDate, Map<Artikel, Integer>> history = new HashMap<>();
    private List<Artikel> artikelListe = new ArrayList<>();

    private TableView<Artikel> artikelTableView;
    private TableView<WarenkorbArtikel> cartTableView;
    private VBox mainLayout;


    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setWidth(1000);
        stage.setHeight(1000);

        String css = getClass().getResource("styles.css").toExternalForm();

        // Login scene
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setPrefSize(400, 400);
        loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username");
        loginPasswordField = new PasswordField();
        loginPasswordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginNutzer());
        Hyperlink goToRegisterLink = new Hyperlink("Don't have an account? Register here");
        goToRegisterLink.setOnAction(e -> stage.setScene(registerScene));
        loginLayout.getChildren().addAll(new Label("Login"), loginUsernameField, loginPasswordField, loginButton, goToRegisterLink);
        loginScene = new Scene(loginLayout);
        loginScene.getStylesheets().add(css);

        // Register scene
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
        registerScene.getStylesheets().add(css);



        // Create the main layout and scene
        mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setPrefSize(500, 500);
        mainScene = new Scene(mainLayout);
        mainScene.getStylesheets().add(css);

        // Show initial scene
        stage.setScene(loginScene);
        stage.setTitle("Eshop");
        stage.show();
    }




    //sections

    private void showMainMenu() {

        mainLayout.getChildren().clear();

        header = new VBox();
        header.getStyleClass().add("header");

        if (authuser instanceof Kunde) {

            Button cartButton = new Button();
            cartButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("cart.png"))));
            cartButton.setOnAction(e -> handleCart());


            if (cartItemCountLabel == null) {
                cartItemCountLabel = new Label("0");
                cartItemCountLabel.getStyleClass().add("cart-item-count");
            }

            HBox cartBox = new HBox(10);
            cartBox.getChildren().addAll(cartButton, cartItemCountLabel);
            cartBox.getStyleClass().add("cart-box");

            Button logoutButton = new Button("Logout");
            logoutButton.getStyleClass().add("logout-button");
            logoutButton.setOnAction(e -> handleLogout());

            HBox kundeHeader = new HBox(10);
            kundeHeader.getChildren().addAll(cartBox, logoutButton);
            kundeHeader.getStyleClass().add("kunde-header");

            header.getChildren().add(kundeHeader);
            kundeSection();
        } else if (authuser instanceof Mitarbeiter) {
            Button artikelButton = new Button("Artikel");
            artikelButton.setOnAction(e -> showArtikelSection());

            Button kundeManagementButton = new Button("Kunde Management");
            kundeManagementButton.setOnAction(e -> showKundeManagementSection());

            Button mitarbeiterManagementButton = new Button("Mitarbeiter Management");
            mitarbeiterManagementButton.setOnAction(e -> showMitarbeiterManagementSection());

            HBox navBar = new HBox(10);
            navBar.getChildren().addAll(artikelButton, kundeManagementButton, mitarbeiterManagementButton);
            navBar.getStyleClass().add("nav-bar");

            Button logoutButton = new Button("Logout");
            logoutButton.getStyleClass().add("logout-button");
            logoutButton.setOnAction(e -> handleLogout());

            HBox mitarbeiterHeader = new HBox(10);
            mitarbeiterHeader.getChildren().addAll(navBar, logoutButton);
            HBox.setHgrow(navBar, Priority.ALWAYS);
            mitarbeiterHeader.setAlignment(Pos.CENTER_RIGHT);
            mitarbeiterHeader.getStyleClass().add("mitarbeiter-header");

            header.getChildren().add(mitarbeiterHeader);
            showArtikelSection();
        }



        stage.setScene(mainScene);
    }

    private void kundeSection() {
        mainLayout.getChildren().clear();
        TableView<Artikel> artikelTableView = showartikelListe();

        VBox artikelContent = new VBox();
        artikelContent.getChildren().addAll(artikelTableView);
        mainLayout.getChildren().addAll(header, artikelContent);

    }

    private void showArtikelSection() {
        mainLayout.getChildren().clear();



        Hyperlink addArtikel = new Hyperlink("Artikel hinzufügen");
        addArtikel.setOnAction(e -> handleAddArtikel());

        Hyperlink history = new Hyperlink("shop history");
        history.setOnAction(e -> shopHistorieAnsehen());



        
        TableView<Artikel> artikelTableView = showartikelListe();

        VBox artikelContent = new VBox();
        artikelContent.getChildren().addAll(artikelTableView);
        mainLayout.getChildren().addAll(header,addArtikel,history, artikelContent);

    }

    private void showKundeManagementSection() {
        mainLayout.getChildren().clear();
        TableView<Kunde> kundeTableView = showKundenListe();

        VBox kundemanagmentcontent = new VBox();
        kundemanagmentcontent.getChildren().addAll(kundeTableView);
        mainLayout.getChildren().addAll(header, kundeTableView);
    }

    private void showMitarbeiterManagementSection() {
        mainLayout.getChildren().clear();
        Hyperlink addMitarbeiter = new Hyperlink("add Mitarbeiter");
        addMitarbeiter.setOnAction(e -> addMitarbeiter());
        TableView<Mitarbeiter> mitarbeiterTableView = showMitarbeiterListe();

        VBox mitarbeitermanagmentcontent = new VBox();
        mitarbeitermanagmentcontent.getChildren().addAll(mitarbeiterTableView);
        mainLayout.getChildren().addAll(header,addMitarbeiter, mitarbeiterTableView);
    }


  // liste
    private TableView<Artikel> showartikelListe() {
        TableView<Artikel> tableView = new TableView<>();
        tableView.setPrefSize(800, 400);

        TableColumn<Artikel, Integer> artikelNummerColumn = new TableColumn<>("Artikel Nummer");
        artikelNummerColumn.setCellValueFactory(new PropertyValueFactory<>("artikelnummer"));
        artikelNummerColumn.setPrefWidth(150);

        TableColumn<Artikel, String> bezeichnungColumn = new TableColumn<>("Bezeichnung");
        bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        bezeichnungColumn.setPrefWidth(150);

        TableColumn<Artikel, Integer> bestandColumn = new TableColumn<>("Bestand");
        bestandColumn.setCellValueFactory(new PropertyValueFactory<>("bestand"));
        bestandColumn.setPrefWidth(150);

        TableColumn<Artikel, Double> preisColumn = new TableColumn<>("Preis");
        preisColumn.setCellValueFactory(new PropertyValueFactory<>("preis"));
        preisColumn.setPrefWidth(150);

        TableColumn<Artikel, Integer> packung = new TableColumn<>("packungsGrosse");
        packung.setCellValueFactory(new PropertyValueFactory<>("packungsGrosse"));
        packung.setPrefWidth(150);

        TableColumn<Artikel, Void> actionColumn = new TableColumn<>("");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("+");

            {
                btn.setOnAction(event -> {
                    Artikel artikel = getTableView().getItems().get(getIndex());
                    if (authuser instanceof Kunde) {
                        openQuantityDialog(artikel);
                    } else if (authuser instanceof Mitarbeiter) {
                        updateQuantityDialog(artikel);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
        actionColumn.setPrefWidth(40);

        tableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, bestandColumn, preisColumn,packung, actionColumn);

        artikelTableView = tableView;

        artikelListe = shop.getArtikelListe();
        ObservableList<Artikel> artikelData = FXCollections.observableArrayList(artikelListe);
        artikelTableView.setItems(artikelData);

        return tableView;
    }
    private TableView<Mitarbeiter> showMitarbeiterListe() {
        TableView<Mitarbeiter> mitarbeiterTableView = new TableView<>();
        mitarbeiterTableView.setPrefSize(800, 400);


        TableColumn<Mitarbeiter, Integer> mitarbeiterNummerColumn = new TableColumn<>("Mitarbeiternummer");
        mitarbeiterNummerColumn.setCellValueFactory(new PropertyValueFactory<>("nutzerNummer"));
        mitarbeiterNummerColumn.setPrefWidth(200);

        TableColumn<Mitarbeiter, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        TableColumn<Mitarbeiter, String> benutzerkennungColumn = new TableColumn<>("Benutzerkennung");
        benutzerkennungColumn.setCellValueFactory(new PropertyValueFactory<>("benutzerkennung"));
        benutzerkennungColumn.setPrefWidth(200);



        mitarbeiterTableView.getColumns().addAll(mitarbeiterNummerColumn, nameColumn, benutzerkennungColumn);

        List<Mitarbeiter> mitarbeiterListe = shop.getMitarbeiterlist();
        ObservableList<Mitarbeiter> mitarbeiterData = FXCollections.observableArrayList(mitarbeiterListe);
        mitarbeiterTableView.setItems(mitarbeiterData);

        return mitarbeiterTableView;
    }
    private TableView<Kunde> showKundenListe() {

        VBox kundenListeLayout = new VBox(10);
        kundenListeLayout.setPadding(new Insets(20));


        TableView<Kunde> kundenTableView = new TableView<>();
        kundenTableView.setPrefSize(800, 400);


        TableColumn<Kunde, Integer> kundenNummerColumn = new TableColumn<>("Kundennummer");
        kundenNummerColumn.setCellValueFactory(new PropertyValueFactory<>("nutzerNummer"));

        TableColumn<Kunde, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Kunde, String> benutzerkennungColumn = new TableColumn<>("Benutzerkennung");
        benutzerkennungColumn.setCellValueFactory(new PropertyValueFactory<>("benutzerkennung"));


        TableColumn<Kunde, String> straßeColumn = new TableColumn<>("Straße");
        straßeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse().getStraße()));

        TableColumn<Kunde, String> stadtColumn = new TableColumn<>("Stadt");
        stadtColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse().getStadt()));

        TableColumn<Kunde, String> bundeslandColumn = new TableColumn<>("Bundesland");
        bundeslandColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse().getBundesland()));

        TableColumn<Kunde, Integer> postleitzahlColumn = new TableColumn<>("Postleitzahl");
        postleitzahlColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAdresse().getPostleitzahl()));

        TableColumn<Kunde, String> landColumn = new TableColumn<>("Land");
        landColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse().getLand()));


        kundenTableView.getColumns().addAll(kundenNummerColumn, nameColumn, benutzerkennungColumn, straßeColumn, stadtColumn, bundeslandColumn, postleitzahlColumn, landColumn);


        List<Kunde> kundenListe = shop.getKundenList();
        ObservableList<Kunde> kundenData = FXCollections.observableArrayList(kundenListe);
        kundenTableView.setItems(kundenData);


        kundenListeLayout.getChildren().add(kundenTableView);


        return kundenTableView;
    }
    private TableView<Event> showverlaufliste() {
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

        List<Event> eventList = shop.ShopVerlaufAnzeigen();
        ObservableList<Event> eventData = FXCollections.observableArrayList(eventList);
        eventTableView.setItems(eventData);


        return eventTableView;
    }
    private void zeigeArtikelliste() {
        artikelListe = shop.getArtikelListe();
        ObservableList<Artikel> artikelData = FXCollections.observableArrayList(artikelListe);
        artikelTableView.setItems(artikelData);
    }

//auth
private void registerUser() {
    String name = nameField.getText();
    String benutzerkennung = usernameField.getText();
    String passwort = passwordField.getText();
    String straße = streetField.getText();
    String stadt = cityField.getText();
    String bundesland = stateField.getText();
    int postleitzahl;
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
            showAlert("Login erfolgreich.");
            showMainMenu();
        } catch (FalscheLoginDaten e) {
            showAlert(e.getMessage());
        }
    }
    private void handleLogout() {
        authuser = null;
        showAlert("Logout erfolgreich.");
        stage.setScene(loginScene);
    }

//dialog
    private void updateQuantityDialog(Artikel artikel) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Bestand ändern");
        dialog.setHeaderText("Artikel: " + artikel.getBezeichnung());
        dialog.setContentText("Bestand ändern :");
        dialog.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        TextField packungsGrosseField = new TextField();
        packungsGrosseField.setPromptText("Packungsgröße");
        packungsGrosseField.setVisible(false);
        CheckBox changePackungsGrosseCheckbox;

        if (artikel instanceof Massenartikel) {
            changePackungsGrosseCheckbox = new CheckBox("Packungsgröße ändern");
            changePackungsGrosseCheckbox.setSelected(false);
            changePackungsGrosseCheckbox.setOnAction(e -> {
                packungsGrosseField.setText(Integer.toString(((Massenartikel) artikel).getPackungsGrosse()));
                packungsGrosseField.setVisible(changePackungsGrosseCheckbox.isSelected());
            });
        } else {
            changePackungsGrosseCheckbox = null;
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Bestand (positive für Einlagerung, negative für Auslagerung)"),
                dialog.getEditor()
        );

        if (changePackungsGrosseCheckbox != null) {
            content.getChildren().addAll(
                    changePackungsGrosseCheckbox,
                    packungsGrosseField
            );
        }

        dialog.getDialogPane().setContent(content);

        dialog.showAndWait().ifPresent(quantity -> {
            try {
                int newBestand = Integer.parseInt(quantity);
                shop.BestandAendern(artikel.getArtikelnummer(), newBestand);
                String ereignisTyp = newBestand >= 0 ? "Einlagerung" : "Auslagerung";
                shop.Ereignisfesthalten(ereignisTyp, artikel, Math.abs(newBestand), authuser);

                if (changePackungsGrosseCheckbox != null && changePackungsGrosseCheckbox.isSelected() && artikel instanceof Massenartikel) {
                    int neuePackungsGrosse = Integer.parseInt(packungsGrosseField.getText());
                    if (neuePackungsGrosse > 0) {
                        ((Massenartikel) artikel).setPackungsGrosse(neuePackungsGrosse);
                        System.out.println("Packungsgröße für Artikel " + artikel.getArtikelnummer() + " aktualisiert. Neue Packungsgröße: " + neuePackungsGrosse);
                    } else {
                        showAlert("Fehler: Bitte geben Sie eine Zahl größer als 0 ein.");
                    }
                }
                showArtikelSection();
            } catch (NumberFormatException e) {
                showAlert("Fehler: Bitte geben Sie eine gültige Zahl ein.");
            } catch (Exception e) {
                showAlert("Fehler: " + e.getMessage());
            }
        });
    }
    private void openQuantityDialog(Artikel artikel) {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Enter Quantity");
        dialog.setHeaderText("Enter quantity for Artikel " + artikel.getBezeichnung());
        dialog.setContentText("Quantity:");
        dialog.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        dialog.showAndWait().ifPresent(quantity -> {
            try {
                shop.inWarenKorbLegen(artikel, Integer.parseInt(quantity), authuser);
                updateCartItemCount();
            } catch (AnzahlException e) {
                showAlert(e.getMessage());
            }
        });
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    //cart
    private void updateCartItemCount() {
        if (cartItemCountLabel != null) {
            int itemCount = ((Kunde) authuser).getWarenkorb().getWarenkorbListe().size();
            cartItemCountLabel.setText(String.valueOf(itemCount));
        } else {
            System.err.println("Cart item count label is null.");
        }
    }
    private void handleCart() {
        BorderPane cartLayout = new BorderPane();
        Scene cartScene = new Scene(cartLayout, 600, 400);
        Button backButton = new Button("Zurück zur Homepage");
        backButton.setOnAction(e -> stage.setScene(mainScene));
        TableView<WarenkorbArtikel> cartTableView = new TableView<>();

        TableColumn<WarenkorbArtikel, String> artikelColumn = new TableColumn<>("Artikel");
        artikelColumn.setCellValueFactory(cellData -> {
            Artikel artikel = cellData.getValue().getArtikel();
            if (artikel != null) {
                return new SimpleStringProperty(artikel.getBezeichnung());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<WarenkorbArtikel, Integer> anzahlColumn = new TableColumn<>("Anzahl");
        anzahlColumn.setCellValueFactory(new PropertyValueFactory<>("anzahl"));

        TableColumn<WarenkorbArtikel, Double> preisColumn = new TableColumn<>("Preis");
        preisColumn.setCellValueFactory(new PropertyValueFactory<>("preis"));

        TableColumn<WarenkorbArtikel, Double> gesamtPreisColumn = new TableColumn<>("Gesamtpreis");
        gesamtPreisColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtPreis"));

        TableColumn<WarenkorbArtikel, Void> actionColumn = new TableColumn<>("");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("change quantity");

            {
                btn.setOnAction(event -> {
                    WarenkorbArtikel artikel = getTableView().getItems().get(getIndex());
                    ArtikelMengeändern(artikel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        cartTableView.getColumns().addAll(artikelColumn, anzahlColumn, preisColumn, gesamtPreisColumn, actionColumn);

        Label totalPriceLabel = new Label();

        VBox cartBox = new VBox(10);
        Button buyButton = new Button("Kaufen");
        buyButton.setOnAction(e -> {
            try {
                kaufen();
                showAlert("Einkauf erfolgreich. Rechnung wurde erstellt.");
                artikelListe = shop.getArtikelListe();
            } catch (Exception ex) {
                showAlert("es gibt ein fehler");
            }
        });

        Button emptyCartButton = new Button("Warenkorb leeren");
        emptyCartButton.setOnAction(e -> {
            shop.Warenkorbleeren(authuser);
            showAlert("Warenkorb erfolgreich geleert.");
        });

        cartBox.getChildren().addAll(cartTableView, totalPriceLabel, buyButton, emptyCartButton, backButton);
        cartLayout.setCenter(cartBox);
        updateCartContent(cartTableView, totalPriceLabel);
        stage.setScene(cartScene);
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
    private void updateCartContent(TableView<WarenkorbArtikel> cartTableView, Label totalPriceLabel) {

        Warenkorb warenkorb = shop.getWarenkorb(authuser);
        if (warenkorb != null && !warenkorb.getWarenkorbListe().isEmpty()) {
            ObservableList<WarenkorbArtikel> cartItems = FXCollections.observableArrayList(warenkorb.getWarenkorbListe());
            cartTableView.setItems(cartItems);


            double totalPrice = warenkorb.calculateTotalPrice();
            totalPriceLabel.setText("Gesamtpreis: " + String.format("%.2f", totalPrice));
        } else {
            cartTableView.setItems(FXCollections.emptyObservableList());
            totalPriceLabel.setText("Der Warenkorb ist leer.");
        }
    }
    private void ArtikelMengeändern(WarenkorbArtikel artikel) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ändern der Artikelmenge");
        dialog.setHeaderText("Geben Sie die neue Anzahl für " + artikel.getArtikel().getBezeichnung() + " ein:");
        dialog.setContentText("Neue Anzahl:");


        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int neueAnzahl = Integer.parseInt(result.get().trim());


                shop.artikelMengeaendern(artikel.getArtikel().getArtikelnummer(), neueAnzahl, authuser);
                System.out.println("Anzahl erfolgreich geändert.");
            } catch (NumberFormatException e) {

                showAlert("Ungültige Eingabe");

            } catch (Exception e) {
                showAlert( e.getMessage());
            }
        }
    }


    //mitarbeiter
    private void addMitarbeiter() {
        mainLayout.getChildren().clear();
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
        mainLayout.getChildren().addAll(header, addMitarbeiterLayout);
    }
    private void handleAddArtikel() {

        mainLayout.getChildren().clear();

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


        mainLayout.getChildren().addAll(header, addArtikelLayout);
    }
    private void shopHistorieAnsehen() {
        mainLayout.getChildren().clear();


        TableView<Event> eventTableView = showverlaufliste();
        TextField filterField = new TextField();
        filterField.setPromptText("Filter by Artikel/Nutzer");

        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> {
            String filterText = filterField.getText().toLowerCase();
            ObservableList<Event> eventList = FXCollections.observableArrayList(shop.ShopVerlaufAnzeigen());
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


        ComboBox<String> artikelComboBox = new ComboBox<>();
        List<String> bezeichnungList = shop.getArtikelListe().stream().map(Artikel::getBezeichnung).collect(Collectors.toList());
        artikelComboBox.setItems(FXCollections.observableArrayList(bezeichnungList));
        artikelComboBox.setPromptText("Artikel auswählen");


        NumberAxis xAxis = new NumberAxis(1, 30, 1);
        xAxis.setLabel("Tage");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Bestand");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Bestandshistorie");


        Button showHistoryButton = new Button("Historie anzeigen");
        showHistoryButton.setOnAction(e -> {
            String selectedBezeichnung = artikelComboBox.getValue();
            if (selectedBezeichnung != null && !selectedBezeichnung.isEmpty()) {
                Artikel selectedArtikel = findArtikelByBezeichnung(selectedBezeichnung);
                if (selectedArtikel != null) {
                    drawStockHistory(lineChart, selectedArtikel);
                } else {
                    showAlert("Artikel nicht gefunden.");
                }
            } else {
                showAlert("Bitte wählen Sie einen Artikel aus.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(new Label("Shop Historie"), filterField, filterButton,eventTableView, artikelComboBox, showHistoryButton, lineChart);

        mainLayout.getChildren().addAll(header, vbox);
    }
    private Artikel findArtikelByBezeichnung(String bezeichnung) {
        for (Artikel artikel : shop.getArtikelListe()) {
            if (artikel.getBezeichnung().equals(bezeichnung)) {
                return artikel;
            }
        }
        return null;
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



}
