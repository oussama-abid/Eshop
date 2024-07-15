package ui;

import Entities.*;
import domain.EShop;
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

/**
 * The {@code MainLayout} class serves as the base class for the application's main layout and functionality.
 * It extends the {@code Application} class and provides the structure for various scenes and components
 * used throughout the application, such as login, registration, and the main menu.
 * <p>
 * This class manages user authentication, scene transitions, and data loading for different user roles.
 * It contains various UI components and methods to handle the application's main operations.
 * </p>
 *
 * @see Application
 * @see Nutzer
 * @see EShop
 * @see Kunde
 * @see Mitarbeiter
 * @see Artikel
 * @see Event
 * @see WarenkorbArtikel
 */
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

    /**
     * Loads the list of articles from the shop.
     */
    protected void loadArtikel() {
        artikelListe = shop.getArtikelListe();
    }

    /**
     * Loads the list of customers from the shop.
     */
    protected void loadKunde() {
        kundenList = shop.getKundenList();
    }

    /**
     * Loads the list of employees from the shop.
     */
    protected void loadMitarbeiter() {
        mitarbeiterList = shop.getMitarbeiterlist();
    }

    /**
     * Loads the shop's event history.
     */
    protected void loadEvents() {
        shopVerlauf = shop.ShopVerlaufAnzeigen();
    }

    /**
     * Displays the main menu based on the authenticated user's role.
     * Initializes the customer or employee section accordingly.
     */
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
