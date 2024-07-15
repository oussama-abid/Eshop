package ui;

import Entities.*;
import Exceptions.AnzahlException;
import Exceptions.Artikelnamenichtgefunden;
import Exceptions.PackungsGrosseException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * The  CustomerSection class is responsible for managing and displaying
 * the UI components related to the customer section of the application.
 * It handles the main eshop view (displaying all articles available in the eshop),shopping cart, and all related operations such as
 * adding items to the cart, updating quantities, and checking out.
 */
public class CustomerSection {

    private MainLayout mainLayout;

    /**
     * Constructor to initialize the CustomerSection with the main layout.
     *
     * @param mainLayout the main layout of the application
     */
    public CustomerSection(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    /**
     * Initializes the customer section by setting up the header and showing the eshop .
     */
    public void initialize() {
        mainLayout.header.getChildren().clear();

        Button shopButton = new Button("Shop");
        shopButton.setOnAction(e -> showShop());

        Button cartButton = new Button();
        cartButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("cart.png"))));
        cartButton.setOnAction(e -> handleCart());

        if (mainLayout.cartItemCountLabel == null) {
            mainLayout.cartItemCountLabel = new Label("0");
            mainLayout.cartItemCountLabel.getStyleClass().add("cart-item-count");
        }

        HBox cartBox = new HBox(10);
        cartBox.getChildren().addAll(cartButton, mainLayout.cartItemCountLabel);
        cartBox.getStyleClass().add("cart-box");

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> handleLogout());

        HBox kundeHeader = new HBox(10);
        kundeHeader.getChildren().addAll(cartBox, shopButton, logoutButton);
        kundeHeader.getStyleClass().add("kunde-header");

        mainLayout.header.getChildren().add(kundeHeader);
        showShop();
    }

    /**
     * Shows the shop view by clearing the main layout and setting up the table of articles.
     */
    private void showShop() {
        mainLayout.mainLayout.getChildren().clear();
        mainLayout.artikelTableView = createArtikelTableView();

        VBox artikelContent = new VBox();
        artikelContent.getChildren().addAll(mainLayout.artikelTableView);
        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, artikelContent);
    }

    /**
     * Creates and returns a TableView of articles.
     *
     * @return the TableView of articles
     */
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
                    openQuantityDialog(artikel);
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

    /**
     * Opens a dialog to enter the quantity of an article to be added to the cart.
     *
     * @param artikel the article to be added to the cart
     */
    private void openQuantityDialog(Artikel artikel) {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Menge wählen");
        dialog.setHeaderText("Geben sie die gewünschte Menge ein von " + artikel.getBezeichnung());
        dialog.setContentText("Menge:");
        dialog.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        dialog.showAndWait().ifPresent(quantity -> {
            try {
                mainLayout.shop.inWarenKorbLegen(artikel, Integer.parseInt(quantity), mainLayout.authuser);
                updateCartItemCount();
            } catch (AnzahlException | PackungsGrosseException e) {
                showAlert(e.getMessage());
            }
        });
    }

    /**
     * Updates the cart item count label with the current number of items in the cart.
     */
    private void updateCartItemCount() {
        if (mainLayout.cartItemCountLabel != null) {
            int itemCount = mainLayout.warenkorb.getWarenkorbListe().size();
            mainLayout.cartItemCountLabel.setText(String.valueOf(itemCount));
        } else {
            System.err.println("Cart item count label is null.");
        }
    }

    /**
     * Handles the display of the cart view.
     */
    private void handleCart() {
        mainLayout.mainLayout.getChildren().clear();
        mainLayout.warenkorbArtikelTableView = createWarenkorbTableView();
        BorderPane cartLayout = new BorderPane();
        VBox cartContent = new VBox();
        Button backButton = new Button("Zurück zur Homepage");
        backButton.setOnAction(e -> showShop());
        mainLayout.totalPriceLabel = calculateTotalPrice();
        VBox cartBox = new VBox(10);
        Button buyButton = new Button("Kaufen");
        buyButton.setOnAction(e -> kaufen());
        Button emptyCartButton = new Button("Warenkorb leeren");
        emptyCartButton.setOnAction(e -> warenkorbLeeren());

        cartBox.getChildren().addAll(mainLayout.warenkorbArtikelTableView, mainLayout.totalPriceLabel, buyButton, emptyCartButton, backButton);
        cartLayout.setCenter(cartBox);

        cartContent.getChildren().addAll(cartBox);
        mainLayout.mainLayout.getChildren().addAll(mainLayout.header, cartContent);
    }

    /**
     * Creates and returns a TableView of items in the cart.
     *
     * @return the TableView of cart items
     */
    private TableView<WarenkorbArtikel> createWarenkorbTableView() {
        mainLayout.warenkorbArtikelTableView.getColumns().clear();
        mainLayout.warenkorbArtikelTableView.setPrefSize(800, 600);
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
                    changeQuantityDialog(artikel);
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

        mainLayout.warenkorbArtikelTableView.getColumns().addAll(artikelColumn, anzahlColumn, preisColumn, gesamtPreisColumn, actionColumn);
        List<WarenkorbArtikel> warenkorbListe = mainLayout.warenkorb.getWarenkorbListe();
        ObservableList<WarenkorbArtikel> data = FXCollections.observableArrayList(warenkorbListe);
        mainLayout.warenkorbArtikelTableView.setItems(data);
        return mainLayout.warenkorbArtikelTableView;
    }

    /**
     * Opens a dialog to change the quantity of an article in the cart.
     *
     * @param artikel the article in the cart to change quantity
     */
    private void changeQuantityDialog(WarenkorbArtikel artikel) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(artikel.getAnzahl()));
        dialog.setTitle("Menge ändern");
        dialog.setHeaderText("Ändern der Menge für " + artikel.getArtikel().getBezeichnung());
        dialog.setContentText("Neue Menge:");
        dialog.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        dialog.showAndWait().ifPresent(quantity -> {
            try {
                int neueMenge = Integer.parseInt(quantity);
                if (neueMenge <= 0) {
                    throw new AnzahlException("Die Menge muss größer als 0 sein.");
                }
                mainLayout.shop.artikelMengeaendern(artikel.getArtikel().getArtikelnummer(), neueMenge, mainLayout.authuser);
                updateCartItemCount();
                mainLayout.totalPriceLabel = calculateTotalPrice();
                mainLayout.warenkorbArtikelTableView.setItems(FXCollections.observableArrayList(mainLayout.warenkorb.getWarenkorbListe()));
            } catch (AnzahlException e) {
                showAlert(e.getMessage());
            } catch (NumberFormatException e) {
                showAlert("Bitte geben Sie eine gültige Zahl ein.");
            } catch (Artikelnamenichtgefunden e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Calculates and updates the total price label for the items in the cart.
     *
     * @return the updated total price label
     */
    private Label calculateTotalPrice() {
        if (mainLayout.warenkorb != null && !mainLayout.warenkorb.getWarenkorbListe().isEmpty()) {
            mainLayout.totalPrice = mainLayout.warenkorb.calculateTotalPrice();
            mainLayout.totalPrice = Math.round(mainLayout.totalPrice * 100.0) / 100.0;
            mainLayout.totalPriceLabel.setText("Gesamtpreis: " + String.format("%.2f EUR", mainLayout.totalPrice));
        } else {
            mainLayout.totalPriceLabel.setText("Der Warenkorb ist leer.");
        }
        return mainLayout.totalPriceLabel;
    }

    /**
     * Clears the shopping cart by removing all items.
     */
    private void warenkorbLeeren() {
        mainLayout.shop.Warenkorbleeren(mainLayout.authuser);
        updateCartItemCount();
        mainLayout.totalPriceLabel = calculateTotalPrice();
        showAlert("Warenkorb erfolgreich geleert.");
    }

    /**
     * Completes the purchase process, creating a new invoice for the customer.
     * Updates the UI accordingly after the purchase.
     */
    private void kaufen() {
        if (mainLayout.warenkorb.getWarenkorbListe().isEmpty()) {
            showAlert("Ihr Warenkorb ist leer. Bitte fügen Sie Artikel hinzu, bevor Sie fortfahren.");
            return;
        }
        try {
            Date date = new Date();
            Kunde kunde = (Kunde) mainLayout.authuser;
            Rechnung rechnung = new Rechnung(date, mainLayout.warenkorb.calculateTotalPrice(), kunde);
            showAlert("Einkauf erfolgreich. Rechnung wurde erstellt.");
            showRechnung(rechnung);
        } catch (Exception e) {
            showAlert("Es gibt ein Problem.");
        }

        mainLayout.shop.kaufen(mainLayout.authuser);
        updateCartItemCount();
        mainLayout.totalPriceLabel = calculateTotalPrice();
        mainLayout.artikelTableView = createArtikelTableView();
        mainLayout.warenkorbArtikelTableView = createWarenkorbTableView();
    }

    /**
     * Shows the details of the invoice in an alert dialog.
     *
     * @param rechnung the invoice to display
     */
    private void showRechnung(Rechnung rechnung) {
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
        Label adressLabel = new Label("Adresse: " + rechnung.getKunde().getAdresse());
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

        content.getChildren().addAll(dateLabel, kundeLabel, adressLabel, totalLabel, table);
        alert.getDialogPane().setContent(content);

        alert.showAndWait();
    }

    /**
     * Handles the logout action by resetting the authenticated user and navigating to the login scene.
     */
    private void handleLogout() {
        mainLayout.authuser = null;
        showAlert("Logout erfolgreich.");
        mainLayout.stage.setScene(mainLayout.loginScene);
    }

    /**
     * Shows an alert dialog with the given message.
     *
     * @param message the message to display in the alert dialog
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
