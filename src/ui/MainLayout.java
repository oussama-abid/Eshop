package ui;

import Entities.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public abstract class MainLayout extends Application {

    protected Stage stage;
    protected Scene loginScene;
    protected Scene registerScene;
    protected Scene mainScene;

    protected TextField loginUsernameField = new TextField();
    protected PasswordField loginPasswordField = new PasswordField();

    protected VBox header;
    protected Label cartItemCountLabel;

    protected Nutzer authuser;
    protected EShop shop = new EShop();

    protected List<Kunde> kundenList = new ArrayList<>();
    protected List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
    protected List<Event> shopVerlauf = new ArrayList<>();
    protected List<Artikel> artikelListe = new ArrayList<>();

    protected TableView<Artikel> artikelTableView = new TableView<>();
    protected TableView<Event> eventTableView = new TableView<>();
    protected TableView<Mitarbeiter> mitarbeiterTableView = new TableView<>();
    protected TableView<Kunde> kundeTableView = new TableView<>();
    protected TableView<WarenkorbArtikel> warenkorbArtikelTableView = new TableView<>();

    protected Label totalPriceLabel = new Label();

    protected double totalPrice;
    protected Warenkorb warenkorb;

    protected VBox mainLayout;

    protected void loadArtikel() {
        artikelListe = shop.getArtikelListe();
    }

    protected void loadKunde() {
        kundenList = shop.getKundenList();
    }

    protected void loadMitarbeiter() {
        mitarbeiterList = shop.getMitarbeiterlist();
    }

    protected void loadEvents() {
        shopVerlauf = shop.ShopVerlaufAnzeigen();
    }

    protected void showMainMenu() {
        mainLayout.getChildren().clear();
        header = new VBox();
        header.getStyleClass().add("header");

        if (authuser instanceof Kunde) {
            warenkorb = shop.getWarenkorb(authuser);
            CustomerSection customerSection = new CustomerSection(this);
            customerSection.initialize();
        } else if (authuser instanceof Mitarbeiter) {
            EmployeeSection employeeSection = new EmployeeSection(this);
            employeeSection.initialize();
        }

        stage.setScene(mainScene);
    }
}
