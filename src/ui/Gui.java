package ui;

import Entities.*;
import Exceptions.AnzahlException;
import Exceptions.FalscheLoginDaten;
import Exceptions.NutzernameExistiertBereits;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gui extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene registerScene;
    private Scene mainScene;

    private TextField loginUsernameField = new TextField();;
    private PasswordField loginPasswordField = new PasswordField();

    private TextField nameField = new TextField();
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private TextField streetField  = new TextField();
    private TextField cityField  = new TextField();
    private TextField stateField  = new TextField();
    private TextField zipCodeField  = new TextField();
    private TextField countryField  = new TextField();


    private TextField mitatbeiternameField  = new TextField();
    private TextField mitatbeiterusernameField  = new TextField();
    private PasswordField mitatbeiterpasswordField  = new PasswordField();


    private TextField bezeichnungField = new TextField();
    private TextField bestandField = new TextField();
    private TextField preisField = new TextField();
    private CheckBox massenartikelCheckBox = new CheckBox("Ist Massenartikel");
    private TextField packungsGrosseField = new TextField();


    private VBox header;
    private Label cartItemCountLabel;


    private Nutzer authuser;
    private EShop shop = new EShop();

    private List<Kunde> kundenList = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
    private List<Event> shopVerlauf = new ArrayList<>();
    private List<Artikel> artikelListe = new ArrayList<>();

    private TableView<Artikel> artikelTableView = new TableView<>();;
    private TableView<Event> eventTableView = new TableView<>();;
    private TableView<Mitarbeiter> mitarbeiterTableView = new TableView<>();;
    private TableView<Kunde> kundeTableView = new TableView<>();;
    private TableView<WarenkorbArtikel> warenkorbArtikelTableView = new TableView<>();;

    private Label totalPriceLabel = new Label();

    private  double totalPrice;
    Warenkorb warenkorb;


    private VBox mainLayout;


    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setWidth(1000);
        stage.setHeight(900);

        String css = getClass().getResource("styles.css").toExternalForm();

        // Login scene
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setPrefSize(400, 400);
        loginUsernameField.setPromptText("Username");
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

// loading data from eshop

    private void loadartikel(){
        artikelListe = shop.getArtikelListe();
    }
    private void loadkunde(){
        kundenList = shop.getKundenList();
    }
    private void loadmitarbeiter(){
        mitarbeiterList = shop.getMitarbeiterlist();
    }
    private void loadevents(){
        shopVerlauf = shop.ShopVerlaufAnzeigen();
    }



    //sections

    private void showMainMenu() {

        mainLayout.getChildren().clear();

        header = new VBox();
        header.getStyleClass().add("header");

        if (authuser instanceof Kunde) {
            warenkorb = shop.getWarenkorb(authuser);
            Button shopbutton = new Button("Shop");
            shopbutton.setOnAction(e -> kundeSection());

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
            kundeHeader.getChildren().addAll(cartBox,shopbutton, logoutButton);
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
        artikelTableView = showartikelListe();

        VBox artikelContent = new VBox();
        artikelContent.getChildren().addAll(artikelTableView);
        mainLayout.getChildren().addAll(header, artikelContent);

    }

    private void showArtikelSection() {
        mainLayout.getChildren().clear();


        Hyperlink addArtikel = new Hyperlink("Artikel hinzufügen");
        addArtikel.setOnAction(e -> handleAddArtikel());

        Hyperlink history = new Hyperlink("Shop Historie");
        history.setOnAction(e -> shopHistorieAnsehen());




        artikelTableView = showartikelListe();

        VBox artikelContent = new VBox();
        artikelContent.getChildren().addAll(artikelTableView);
        mainLayout.getChildren().addAll(header,addArtikel,history, artikelContent);

    }

    private void showKundeManagementSection() {
        mainLayout.getChildren().clear();
        kundeTableView = showKundenListe();

        VBox kundemanagmentcontent = new VBox();
        kundemanagmentcontent.getChildren().addAll(kundeTableView);
        mainLayout.getChildren().addAll(header, kundeTableView);
    }

    private void showMitarbeiterManagementSection() {
        mainLayout.getChildren().clear();
        Hyperlink addMitarbeiter = new Hyperlink("Füge Mitarbeiter hinzu");
        addMitarbeiter.setOnAction(e -> addMitarbeiter());
        TableView<Mitarbeiter> mitarbeiterTableView = showMitarbeiterListe();

        VBox mitarbeitermanagmentcontent = new VBox();
        mitarbeitermanagmentcontent.getChildren().addAll(mitarbeiterTableView);
        mainLayout.getChildren().addAll(header,addMitarbeiter, mitarbeiterTableView);
    }



    // generating tableviews

    private TableView<Artikel> showartikelListe() {
        // Clear any existing columns to avoid duplication
        artikelTableView.getColumns().clear();
        artikelTableView.setPrefSize(800, 400);

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

        TableColumn<Artikel, Integer> packung = new TableColumn<>("PackungsGrosse");
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

        artikelTableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, bestandColumn, preisColumn, packung, actionColumn);

        loadartikel();

        ObservableList<Artikel> artikelData = FXCollections.observableArrayList(artikelListe);
        artikelTableView.setItems(artikelData);

        return artikelTableView;
    }
    private TableView<Mitarbeiter> showMitarbeiterListe() {
        mitarbeiterTableView.getColumns().clear();
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

        loadmitarbeiter();
        ObservableList<Mitarbeiter> mitarbeiterData = FXCollections.observableArrayList(mitarbeiterList);
        mitarbeiterTableView.setItems(mitarbeiterData);

        return mitarbeiterTableView;
    }
    private TableView<Kunde> showKundenListe() {

        VBox kundenListeLayout = new VBox(10);
        kundenListeLayout.setPadding(new Insets(20));

        kundeTableView.getColumns().clear();
        kundeTableView.setPrefSize(800, 400);


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


        kundeTableView.getColumns().addAll(kundenNummerColumn, nameColumn, benutzerkennungColumn, straßeColumn, stadtColumn, bundeslandColumn, postleitzahlColumn, landColumn);


        loadkunde();
        ObservableList<Kunde> kundenData = FXCollections.observableArrayList(kundenList);
        kundeTableView.setItems(kundenData);


        kundenListeLayout.getChildren().add(kundeTableView);


        return kundeTableView;
    }
    private TableView<Event> showverlaufliste() {

        eventTableView.getColumns().clear();
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

        loadevents();
        ObservableList<Event> eventData = FXCollections.observableArrayList(shopVerlauf);
        eventTableView.setItems(eventData);


        return eventTableView;
    }
    private TableView<WarenkorbArtikel> showwarenkorbliste(){
        warenkorbArtikelTableView.getColumns().clear();
        warenkorbArtikelTableView.setPrefSize(800, 600);
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
        preisColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Math.round(cellData.getValue().getPreis() * 100.0) / 100.0));

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

        warenkorbArtikelTableView.getColumns().addAll(artikelColumn, anzahlColumn, preisColumn, gesamtPreisColumn, actionColumn);
        List<WarenkorbArtikel> warenkorbListe = warenkorb.getWarenkorbListe();
        ObservableList<WarenkorbArtikel> data = FXCollections.observableArrayList(warenkorbListe);
        warenkorbArtikelTableView.setItems(data);
        return warenkorbArtikelTableView;

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
        dialog.setTitle("Menge wählen");
        dialog.setHeaderText("Geben sie die gewünschte Menge ein von " + artikel.getBezeichnung());
        dialog.setContentText("Menge:");
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
            int itemCount = warenkorb.getWarenkorbListe().size();
            cartItemCountLabel.setText(String.valueOf(itemCount));

        } else {
            System.err.println("Cart item count label is null.");
        }
    }
    private void handleCart() {
        mainLayout.getChildren().clear();
        warenkorbArtikelTableView = showwarenkorbliste();
        BorderPane cartLayout = new BorderPane();
        VBox cartcontent = new VBox();
        Button backButton = new Button("Zurück zur Homepage");
        backButton.setOnAction(e -> kundeSection());
        totalPriceLabel =totalprice();
        VBox cartBox = new VBox(10);
        Button buyButton = new Button("Kaufen");
        buyButton.setOnAction(e -> kaufen());
        Button emptyCartButton = new Button("Warenkorb leeren");
        emptyCartButton.setOnAction(e -> warenkorbleeren());

        cartBox.getChildren().addAll(warenkorbArtikelTableView, totalPriceLabel, buyButton, emptyCartButton, backButton);
        cartLayout.setCenter(cartBox);

        cartcontent.getChildren().addAll(cartBox);
        mainLayout.getChildren().addAll(header, cartcontent);

    }
    private Label totalprice() {
        if (warenkorb != null && !warenkorb.getWarenkorbListe().isEmpty()) {
            totalPrice = warenkorb.getWarenkorbListe().stream()
                    .mapToDouble(item -> item.getArtikel().getPreis() * item.getAnzahl())
                    .sum();
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;  // Rounding to 2 decimal places
            totalPriceLabel.setText("Gesamtpreis: " + String.format("%.2f EUR", totalPrice));
        } else {
            totalPriceLabel.setText("Der Warenkorb ist leer.");
        }
        return totalPriceLabel;
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
                mengeanderen(artikel,neueAnzahl);
                kundesectionupdate();

            } catch (NumberFormatException e) {

                showAlert("Ungültige Eingabe");

            }
        }
    }
    private  void kundesectionupdate(){
        updateCartItemCount();
        totalPriceLabel =totalprice();
        artikelTableView = showartikelListe();
        warenkorbArtikelTableView = showwarenkorbliste();
    }


    //mitarbeiter

    private void addMitarbeiter() {
        mainLayout.getChildren().clear();
        VBox addMitarbeiterLayout = new VBox(10);
        addMitarbeiterLayout.setPadding(new Insets(20));
        mitatbeiternameField.setPromptText("Name");
        mitatbeiterusernameField.setPromptText("Benutzerkennung");
        mitatbeiterpasswordField.setPromptText("Passwort");
        Button addMitarbeiterButton = new Button("Mitarbeiter hinzufügen");
        addMitarbeiterButton.setOnAction(e ->registermitarbeiter());
        addMitarbeiterLayout.getChildren().addAll(
                new Label("Name"), mitatbeiternameField,
                new Label("Benutzerkennung"), mitatbeiterusernameField,
                new Label("Passwort"), mitatbeiterpasswordField,
                addMitarbeiterButton
        );
        mainLayout.getChildren().addAll(header, addMitarbeiterLayout);
    }
    private void handleAddArtikel() {

        mainLayout.getChildren().clear();

        VBox addArtikelLayout = new VBox(10);
        addArtikelLayout.setPadding(new Insets(20));

        bezeichnungField.setPromptText("Bezeichnung");
        bestandField.setPromptText("Bestand");
        preisField.setPromptText("Preis");
        packungsGrosseField.setPromptText("Packungsgröße");
        packungsGrosseField.setDisable(true);
        massenartikelCheckBox.setOnAction(e -> packungsGrosseField.setDisable(!massenartikelCheckBox.isSelected()));

        Button addArtikelButton = new Button("Artikel hinzufügen");
        addArtikelButton.setOnAction(e -> addartikel());

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


    //actions
    public  void registermitarbeiter(){

        String name = mitatbeiternameField.getText();
        String benutzerkennung = mitatbeiterusernameField.getText();
        String passwort = mitatbeiterpasswordField.getText();

        if (name.isEmpty() || benutzerkennung.isEmpty() || passwort.isEmpty()) {
            showAlert("Bitte füllen Sie alle Felder aus.");
            return;
        }

        try {
            shop.checkUniqueUsername(benutzerkennung);
            shop.registriereMitarbeiter(name, benutzerkennung, passwort);
            showAlert("Mitarbeiter erfolgreich hinzugefügt.");

        } catch (NutzernameExistiertBereits ex) {
            showAlert(ex.getMessage());
        }


    }
    public  void addartikel(){

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
            Artikel art =  shop.ArtikelHinzufuegen(bezeichnung, bestand, preis, istMassenartikel, packungsGrosse);
            shop.Ereignisfesthalten("neuer Artikel", art, art.getBestand(), authuser);
            showAlert("Artikel erfolgreich hinzugefügt.");

        } catch (Exception ex) {
            showAlert("Fehler beim Hinzufügen des Artikels: " + ex.getMessage());
        }



    }
    private Artikel findArtikelByBezeichnung(String bezeichnung) {
        for (Artikel artikel : shop.getArtikelListe()) {
            if (artikel.getBezeichnung().equals(bezeichnung)) {
                return artikel;
            }
        }
        return null;
    }
    private void mengeanderen( WarenkorbArtikel artikel,int neueAnzahl) {
        try {
            shop.artikelMengeaendern(artikel.getArtikel().getArtikelnummer(), neueAnzahl, authuser);

        }catch (Exception e) {
            showAlert( e.getMessage());
        }
    }
    private  void warenkorbleeren(){
        shop.Warenkorbleeren(authuser);
        kundesectionupdate();
        showAlert("Warenkorb erfolgreich geleert.");
    }
    private void kaufen() {

        if (warenkorb.getWarenkorbListe().isEmpty()) {
            showAlert("Ihr Warenkorb ist leer. Bitte fügen Sie Artikel hinzu, bevor Sie fortfahren.");
            return;
        }
        try {
            Date date = new Date();
            Kunde kunde = (Kunde) authuser;
            Rechnung Rechnung = new Rechnung(date, warenkorb.calculateTotalPrice(), kunde);
            showAlert("Einkauf erfolgreich. Rechnung wurde erstellt.");
            showrechnung(Rechnung);
           }
        catch (Exception e){
            showAlert("es gibt ein problem.");
        }
        shop.articlebestandanderen(authuser);
        shop.kundeEreignisfesthalten("Auslagerung", authuser);

        shop.kaufen(authuser);
        kundesectionupdate();






    }
    private void showrechnung(Rechnung rechnung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rechnung Details");
        alert.setHeaderText("Ihre Rechnung");

        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(10));

        LocalDateTime localDateTime = rechnung.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedDate = localDateTime.format(formatter);

        Label dateLabel = new Label("Datum: " + formattedDate);
        Label kundeLabel = new Label("Kunde: " + rechnung.getKunde().getName());
        Label adresslabel = new Label("adress: " + rechnung.getKunde().getAdresse());
        Label totalLabel = new Label("Gesamtpreis: " + String.format("%.2f EUR", rechnung.getGesamtpreis()));

        TableView<WarenkorbArtikel> table = new TableView<>();


        TableColumn<WarenkorbArtikel, String> artikelColumn = new TableColumn<>("Artikel");
        artikelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtikel().getBezeichnung()));

        TableColumn<WarenkorbArtikel, Integer> anzahlColumn = new TableColumn<>("Anzahl");
        anzahlColumn.setCellValueFactory(new PropertyValueFactory<>("anzahl"));

        TableColumn<WarenkorbArtikel, String> einzelpreisColumn = new TableColumn<>("Einzelpreis");
        einzelpreisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.format("%.2f", cellData.getValue().getArtikel().getPreis())
        ));

        TableColumn<WarenkorbArtikel, String> gesamtpreisColumn = new TableColumn<>("Gesamtpreis");
        gesamtpreisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.format("%.2f", cellData.getValue().getArtikel().getPreis() * cellData.getValue().getAnzahl())
        ));

        table.getColumns().addAll(artikelColumn, anzahlColumn, einzelpreisColumn, gesamtpreisColumn);


        table.getItems().addAll(rechnung.getKunde().getWarenkorb().getWarenkorbListe());





        content.getChildren().addAll(dateLabel, kundeLabel,adresslabel,totalLabel, table);
        alert.getDialogPane().setContent(content);

        alert.showAndWait();
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

}
