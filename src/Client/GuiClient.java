package Client;

import Entities.Artikel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GuiClient extends Application {



    private Stage primaryStage; // Reference to the primary stage
    private VBox header;
    private VBox mainLayout;
    private TableView<Artikel> artikelTable;
    private TextField loginUsernameField = new TextField();
    private PasswordField loginPasswordField = new PasswordField();
    private ServerRequest serverRequest;

    public GuiClient() {

       serverRequest =new ServerRequest(this);
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
        try {
            String response = serverRequest.login(username, password);

            if (response.equals("success")) {
                showMainMenu();
            } else {
                showAlert("Login Failed: " + response);
            }
        } catch (IOException e) {
            showAlert("Login problem: " + e.getMessage());
        }
    }





    public void showMainMenu() {
        if (header == null) {
            header = new VBox();
        }
        header.getChildren().clear();

        Button artikelButton = new Button("Artikel");
        artikelButton.setOnAction(e -> showArtikelSection());



        HBox navBar = new HBox(10);
        navBar.getChildren().addAll(artikelButton);
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

    }





    public void showArtikelSection() {
        // Clear existing content
        mainLayout.getChildren().clear();
        mainLayout.setStyle("-fx-padding: 10;");

        VBox artikelContent = new VBox(10);
        artikelContent.setPadding(new Insets(10));

        Label titleLabel = new Label("Artikel List");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        artikelContent.getChildren().add(titleLabel);
        Button addButton = new Button("Add New Artikel");
        addButton.setOnAction(e -> addNewArtikelForm());
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

        artikelContent.getChildren().addAll(addButton, artikelTable);

        mainLayout.getChildren().addAll(header, artikelContent);
        try {
            List<Artikel> articles = serverRequest.requestArticleList();
            if (artikelTable != null) {
                artikelTable.getItems().clear();
                artikelTable.getItems().addAll(articles);
            }
        } catch (IOException e) {
            showAlert("Fehler beim Laden der Artikelliste");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void addNewArtikelForm() {
        // Clear existing content
        mainLayout.getChildren().clear();
        mainLayout.setStyle("-fx-padding: 10;");

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(10));

        Label formTitleLabel = new Label("Add New Artikel");
        formTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        TextField bezeichnungField = new TextField();
        bezeichnungField.setPromptText("Bezeichnung");

        TextField bestandField = new TextField();
        bestandField.setPromptText("Bestand");

        TextField preisField = new TextField();
        preisField.setPromptText("Preis");

        CheckBox istMassenartikelCheckbox = new CheckBox("Ist Massenartikel");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String bezeichnung = bezeichnungField.getText().trim();
            int bestand = Integer.parseInt(bestandField.getText().trim());
            float preis = Float.parseFloat(preisField.getText().trim());
            boolean istMassenartikel = istMassenartikelCheckbox.isSelected();
            try {
               String response = serverRequest.addarticle(bezeichnung,bestand,preis,istMassenartikel);
                if (response.startsWith("Neuer Artikel hinzugefügt")) {
                    showArtikelSection();
                } else {
                    showAlert("error bei adding article" +response);
                }
            } catch (IOException ex) {
                showAlert("error bei adding article");
            }
        });

        formLayout.getChildren().addAll(formTitleLabel, bezeichnungField, bestandField, preisField, istMassenartikelCheckbox, submitButton);
        mainLayout.getChildren().addAll(header, formLayout);
    }




    private void handleLogout() {
        showLoginMenu();
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
     serverRequest.closeConnections();
    }

    public static void main(String[] args) {
        launch(args);
    }
}