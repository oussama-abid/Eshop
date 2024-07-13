package ui;

import Entities.*;
import Exceptions.NutzernameExistiertBereits;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeeSection {

    private MainLayout mainLayout;

    public EmployeeSection(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    public void initialize() {
        mainLayout.header.getChildren().clear();

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

        mainLayout.header.getChildren().add(mitarbeiterHeader);
        showArtikelSection();
    }

    private void showArtikelSection() {
        mainLayout.mainLayout.getChildren().clear();

        Hyperlink addArtikel = new Hyperlink("Artikel hinzufügen");
        addArtikel.setOnAction(e -> handleAddArtikel());

        Hyperlink history = new Hyperlink("Shop Historie");
        history.setOnAction(e -> shopHistorieAnsehen());

        mainLayout.artikelTableView = createArtikelTableView();

        VBox artikelContent = new VBox();

        artikelContent.getChildren().addAll(mainLayout.artikelTableView);
        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, addArtikel, history, artikelContent);
    }

    private TableView<Artikel> createArtikelTableView() {
        mainLayout.artikelTableView.getColumns().clear();
        mainLayout.artikelTableView.setPrefSize(800, 400);

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
                    updateQuantityDialog(artikel);
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

        mainLayout.artikelTableView.getColumns().addAll(artikelNummerColumn, bezeichnungColumn, bestandColumn, preisColumn, packung, actionColumn);

        mainLayout.loadArtikel();

        ObservableList<Artikel> artikelData = FXCollections.observableArrayList(mainLayout.artikelListe);
        mainLayout.artikelTableView.setItems(artikelData);

        return mainLayout.artikelTableView;
    }

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
                int neuePackungsGrosse = 0;
                if (artikel instanceof Massenartikel) {
                    neuePackungsGrosse = ((Massenartikel) artikel).getPackungsGrosse();
                    if (changePackungsGrosseCheckbox != null && changePackungsGrosseCheckbox.isSelected()) {
                        if (neuePackungsGrosse > 0) {
                            neuePackungsGrosse = Integer.parseInt(packungsGrosseField.getText());
                        } else {
                            showAlert("Fehler: Bitte geben Sie eine Zahl größer als 0 ein.");
                        }
                    }
                }
                mainLayout.shop.BestandAendern(artikel.getArtikelnummer(), newBestand, neuePackungsGrosse);
                String ereignisTyp = newBestand >= 0 ? "Einlagerung" : "Auslagerung";
                mainLayout.shop.Ereignisfesthalten(ereignisTyp, artikel, Math.abs(newBestand), mainLayout.authuser);
                showArtikelSection();
            } catch (NumberFormatException e) {
                showAlert("Fehler: Bitte geben Sie eine gültige Zahl ein.");
            } catch (Exception e) {
                showAlert("Fehler: " + e.getMessage());
            }
        });
    }

    private void handleAddArtikel() {
        mainLayout.mainLayout.getChildren().clear();

        VBox addArtikelLayout = new VBox(10);
        addArtikelLayout.setPadding(new Insets(20));

        TextField bezeichnungField = new TextField();
        bezeichnungField.setPromptText("Bezeichnung");

        TextField bestandField = new TextField();
        bestandField.setPromptText("Bestand");

        TextField preisField = new TextField();
        preisField.setPromptText("Preis");

        TextField packungsGrosseField = new TextField();
        packungsGrosseField.setPromptText("Packungsgröße");
        packungsGrosseField.setDisable(true);

        CheckBox massenartikelCheckBox = new CheckBox("Ist Massenartikel");
        massenartikelCheckBox.setOnAction(e -> packungsGrosseField.setDisable(!massenartikelCheckBox.isSelected()));

        Button addArtikelButton = new Button("Artikel hinzufügen");
        addArtikelButton.setOnAction(e -> addArtikel(bezeichnungField, bestandField, preisField, massenartikelCheckBox, packungsGrosseField));

        addArtikelLayout.getChildren().addAll(
                new Label("Bezeichnung"), bezeichnungField,
                new Label("Bestand"), bestandField,
                new Label("Preis"), preisField,
                massenartikelCheckBox,
                new Label("Packungsgröße"), packungsGrosseField,
                addArtikelButton
        );

        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, addArtikelLayout);
    }

    private void addArtikel(TextField bezeichnungField, TextField bestandField, TextField preisField, CheckBox massenartikelCheckBox, TextField packungsGrosseField) {
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
            Artikel art = mainLayout.shop.ArtikelHinzufuegen(bezeichnung, bestand, preis, istMassenartikel, packungsGrosse);
            mainLayout.shop.Ereignisfesthalten("neuer Artikel", art, art.getBestand(), mainLayout.authuser);
            showAlert("Artikel erfolgreich hinzugefügt.");
        } catch (Exception ex) {
            showAlert("Fehler beim Hinzufügen des Artikels: " + ex.getMessage());
        }
    }

    private void shopHistorieAnsehen() {
        mainLayout.mainLayout.getChildren().clear();

        TableView<Event> eventTableView = createVerlaufTableView();
        TextField filterField = new TextField();
        filterField.setPromptText("Filter by Artikel/Nutzer");

        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> {
            String filterText = filterField.getText().toLowerCase();
            ObservableList<Event> eventList = FXCollections.observableArrayList(mainLayout.shop.ShopVerlaufAnzeigen());
            if (filterText.isEmpty()) {
                eventTableView.setItems(eventList);
            } else {
                List<Event> filteredEvents = mainLayout.shop.filterevents(filterText);
                ObservableList<Event> filteredList = FXCollections.observableArrayList(filteredEvents);
                eventTableView.setItems(filteredList);
            }
        });

        ComboBox<String> artikelComboBox = new ComboBox<>();
        List<String> bezeichnungList = mainLayout.shop.getArtikelListe().stream().map(Artikel::getBezeichnung).collect(Collectors.toList());
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
        vbox.getChildren().addAll(new Label("Shop Historie"), filterField, filterButton, eventTableView, artikelComboBox, showHistoryButton, lineChart);

        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, vbox);
    }

    private TableView<Event> createVerlaufTableView() {
        mainLayout.eventTableView.getColumns().clear();
        mainLayout.eventTableView.setPrefSize(800, 600);

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

        mainLayout.eventTableView.getColumns().addAll(operationColumn, dateColumn, artikelColumn, quantityColumn, nutzerColumn);

        mainLayout.loadEvents();
        ObservableList<Event> eventData = FXCollections.observableArrayList(mainLayout.shopVerlauf);
        mainLayout.eventTableView.setItems(eventData);

        return mainLayout.eventTableView;
    }

    private void drawStockHistory(LineChart<Number, Number> lineChart, Artikel artikel) {
        List<Artikelhistory> historyList = mainLayout.shop.ShophistoryAnzeigen().stream()
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

    private Artikel findArtikelByBezeichnung(String bezeichnung) {
        for (Artikel artikel : mainLayout.shop.getArtikelListe()) {
            if (artikel.getBezeichnung().equals(bezeichnung)) {
                return artikel;
            }
        }
        return null;
    }

    private void showKundeManagementSection() {
        mainLayout.mainLayout.getChildren().clear();
        mainLayout.kundeTableView = createKundenTableView();

        VBox kundeManagementContent = new VBox();
        kundeManagementContent.getChildren().addAll(mainLayout.kundeTableView);
        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, kundeManagementContent);
    }

    private TableView<Kunde> createKundenTableView() {
        mainLayout.kundeTableView.getColumns().clear();
        mainLayout.kundeTableView.setPrefSize(800, 400);

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

        mainLayout.kundeTableView.getColumns().addAll(kundenNummerColumn, nameColumn, benutzerkennungColumn, straßeColumn, stadtColumn, bundeslandColumn, postleitzahlColumn, landColumn);

        mainLayout.loadKunde();
        ObservableList<Kunde> kundenData = FXCollections.observableArrayList(mainLayout.kundenList);
        mainLayout.kundeTableView.setItems(kundenData);

        return mainLayout.kundeTableView;
    }

    private void showMitarbeiterManagementSection() {
        mainLayout.mainLayout.getChildren().clear();
        Hyperlink addMitarbeiter = new Hyperlink("Füge Mitarbeiter hinzu");
        addMitarbeiter.setOnAction(e -> addMitarbeiter());

        mainLayout.mitarbeiterTableView = createMitarbeiterTableView();

        VBox mitarbeiterManagementContent = new VBox();
        mitarbeiterManagementContent.getChildren().addAll(mainLayout.mitarbeiterTableView);
        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, addMitarbeiter, mitarbeiterManagementContent);
    }

    private TableView<Mitarbeiter> createMitarbeiterTableView() {
        mainLayout.mitarbeiterTableView.getColumns().clear();
        mainLayout.mitarbeiterTableView.setPrefSize(800, 400);

        TableColumn<Mitarbeiter, Integer> mitarbeiterNummerColumn = new TableColumn<>("Mitarbeiternummer");
        mitarbeiterNummerColumn.setCellValueFactory(new PropertyValueFactory<>("nutzerNummer"));
        mitarbeiterNummerColumn.setPrefWidth(200);

        TableColumn<Mitarbeiter, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        TableColumn<Mitarbeiter, String> benutzerkennungColumn = new TableColumn<>("Benutzerkennung");
        benutzerkennungColumn.setCellValueFactory(new PropertyValueFactory<>("benutzerkennung"));
        benutzerkennungColumn.setPrefWidth(200);

        mainLayout.mitarbeiterTableView.getColumns().addAll(mitarbeiterNummerColumn, nameColumn, benutzerkennungColumn);

        mainLayout.loadMitarbeiter();
        ObservableList<Mitarbeiter> mitarbeiterData = FXCollections.observableArrayList(mainLayout.mitarbeiterList);
        mainLayout.mitarbeiterTableView.setItems(mitarbeiterData);

        return mainLayout.mitarbeiterTableView;
    }

    private void addMitarbeiter() {
        mainLayout.mainLayout.getChildren().clear();
        VBox addMitarbeiterLayout = new VBox(10);
        addMitarbeiterLayout.setPadding(new Insets(20));

        TextField mitarbeiterNameField = new TextField();
        mitarbeiterNameField.setPromptText("Name");

        TextField mitarbeiterUsernameField = new TextField();
        mitarbeiterUsernameField.setPromptText("Benutzerkennung");

        PasswordField mitarbeiterPasswordField = new PasswordField();
        mitarbeiterPasswordField.setPromptText("Passwort");

        Button addMitarbeiterButton = new Button("Mitarbeiter hinzufügen");
        addMitarbeiterButton.setOnAction(e -> registerMitarbeiter(mitarbeiterNameField, mitarbeiterUsernameField, mitarbeiterPasswordField));

        addMitarbeiterLayout.getChildren().addAll(
                new Label("Name"), mitarbeiterNameField,
                new Label("Benutzerkennung"), mitarbeiterUsernameField,
                new Label("Passwort"), mitarbeiterPasswordField,
                addMitarbeiterButton
        );

        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, addMitarbeiterLayout);
    }

    private void registerMitarbeiter(TextField mitarbeiterNameField, TextField mitarbeiterUsernameField, PasswordField mitarbeiterPasswordField) {
        String name = mitarbeiterNameField.getText();
        String benutzerkennung = mitarbeiterUsernameField.getText();
        String passwort = mitarbeiterPasswordField.getText();

        if (name.isEmpty() || benutzerkennung.isEmpty() || passwort.isEmpty()) {
            showAlert("Bitte füllen Sie alle Felder aus.");
            return;
        }

        try {
            mainLayout.shop.checkUniqueUsername(benutzerkennung);
            mainLayout.shop.registriereMitarbeiter(name, benutzerkennung, passwort);
            showAlert("Mitarbeiter erfolgreich hinzugefügt.");
        } catch (NutzernameExistiertBereits ex) {
            showAlert(ex.getMessage());
        }
    }

    private void handleLogout() {
        mainLayout.authuser = null;
        showAlert("Logout erfolgreich.");
        mainLayout.stage.setScene(mainLayout.loginScene);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
