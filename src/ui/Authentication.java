package ui;

import Entities.*;
import Exceptions.FalscheLoginDaten;
import Exceptions.NutzernameExistiertBereits;
import Exceptions.Plzexception;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * The {@code Authentication} class handles the creation of login and registration scenes for the application.
 * It manages user authentication, including login and registration functionalities, and provides methods to display
 * appropriate alerts and switch between scenes.
 * <p>
 * The class relies on the {@code MainLayout} to interact with the main application layout and manage the scenes.
 * </p>
 *
 * @see MainLayout
 * @see Nutzer
 * @see FalscheLoginDaten
 * @see NutzernameExistiertBereits
 * @see Plzexception
 */
public class Authentication {

    private MainLayout mainLayout;

    /**
     * Constructs an {@code Authentication} instance with the specified {@code MainLayout}.
     *
     * @param mainLayout the main layout of the application
     */
    public Authentication(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    /**
     * Creates and returns the login scene with the specified CSS styles applied.
     *
     * @param css the CSS stylesheet to be applied to the login scene
     * @return the login scene
     */
    public Scene createLoginScene(String css) {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setPrefSize(400, 400);
        mainLayout.loginUsernameField.setPromptText("Username");
        mainLayout.loginPasswordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginNutzer());
        Hyperlink goToRegisterLink = new Hyperlink("Don't have an account? Register here");
        goToRegisterLink.setOnAction(e -> mainLayout.stage.setScene(mainLayout.registerScene));
        loginLayout.getChildren().addAll(new Label("Login"), mainLayout.loginUsernameField, mainLayout.loginPasswordField, loginButton, goToRegisterLink);
        Scene loginScene = new Scene(loginLayout);
        loginScene.getStylesheets().add(css);
        return loginScene;
    }

    /**
     * Creates and returns the registration scene with the specified CSS styles applied.
     *
     * @param css the CSS stylesheet to be applied to the registration scene
     * @return the registration scene
     */
    public Scene createRegisterScene(String css) {
        VBox registerLayout = new VBox(10);
        registerLayout.setPadding(new Insets(20));
        registerLayout.setPrefSize(400, 400);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("benutzerkennung");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("passwort");

        TextField streetField = new TextField();
        streetField.setPromptText("straße");

        TextField cityField = new TextField();
        cityField.setPromptText("stadt");

        TextField stateField = new TextField();
        stateField.setPromptText("bundesland");

        TextField zipCodeField = new TextField();
        zipCodeField.setPromptText("postleitzahl");

        TextField countryField = new TextField();
        countryField.setPromptText("land");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerUser(nameField, usernameField, passwordField, streetField, cityField, stateField, zipCodeField, countryField));

        Hyperlink goToLoginLink = new Hyperlink("Already have an account? Login here");
        goToLoginLink.setOnAction(e -> mainLayout.stage.setScene(mainLayout.loginScene));

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

        Scene registerScene = new Scene(registerLayout);
        registerScene.getStylesheets().add(css);
        return registerScene;
    }

    /**
     * Handles the user registration process, including validating input data and registering the user.
     *
     * @param nameField        the text field for the user's name
     * @param usernameField    the text field for the user's username
     * @param passwordField    the password field for the user's password
     * @param streetField      the text field for the user's street
     * @param cityField        the text field for the user's city
     * @param stateField       the text field for the user's state
     * @param zipCodeField     the text field for the user's postal code
     * @param countryField     the text field for the user's country
     */
    private void registerUser(TextField nameField, TextField usernameField, PasswordField passwordField, TextField streetField, TextField cityField, TextField stateField, TextField zipCodeField, TextField countryField) {
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
            mainLayout.shop.checkUniqueUsername(benutzerkennung);
            Kunde kunde = mainLayout.shop.registriereKunde(name, benutzerkennung, passwort, straße, stadt, bundesland, postleitzahl, land);
            showAlert("Registration successful. You can now log in.");
            mainLayout.stage.setScene(mainLayout.loginScene);
        } catch (NutzernameExistiertBereits e) {
            showAlert(e.getMessage());
        } catch (Plzexception e) {
            showAlert(e.getMessage());
        }
    }

    /**
     * Handles the user login process, including validating input data and logging the user in.
     */
    private void loginNutzer() {
        String benutzerkennung = mainLayout.loginUsernameField.getText();
        String passwort = mainLayout.loginPasswordField.getText();
        try {
            Nutzer nutzer = mainLayout.shop.login(benutzerkennung, passwort);
            mainLayout.authuser = nutzer;
            mainLayout.showMainMenu();
        } catch (FalscheLoginDaten e) {
            showAlert(e.getMessage());
        }
    }

    /**
     * Displays an alert with the specified message.
     *
     * @param message the message to be displayed in the alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
