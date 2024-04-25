import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cui {
    private Scanner scanner;
    private Usermanagment userManagement;   // TODO: löschen (-> EShop)
    private EShop shop;
    private Eshopproducts products;
    private boolean isFirstTime = true;
    private List<Kunde> kunden = new ArrayList<>();
    public Cui() {
        scanner = new Scanner(System.in);
        userManagement = new Usermanagment();
        shop = new EShop();
        products = new Eshopproducts();
        actions();
    }

    private void actions() {
        boolean shouldExit = false;
        boolean isValid = false;
        while (!shouldExit) {
            System.out.print("register ? login ? add article ?: ");
            String input1 = scanner.nextLine();
            switch (input1) {
                case "2":
                    loginUser();
                    isValid = true;
                    break;
                case "1":
                    registerUser();
                    isValid = true;
                    break;
                case "3":
                    add();
                    isValid = true;
                    break;
                case "exit":
                    shouldExit = true; // Allow exit to break the loop
                    break;
                default:
                    System.out.println("choose from the list");
            }
        }
        scanner.close();
    }

    private void registerUser() {
        System.out.println("Enter registration infos:");
        System.out.print("Nummer: ");

        int nummer = Integer.parseInt(scanner.nextLine());

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Adresse: ");
        String Adresse = scanner.nextLine();
        System.out.print("Benutzerkennung: ");
        String benutzerkennung = scanner.nextLine();
        System.out.print("Passwort: ");
        String passwort = scanner.nextLine();

        userManagement.registerkunde(nummer, name, benutzerkennung, passwort,Adresse);
        System.out.println("Kunde registered .");

//        actions();
    }

    private void loginUser() {
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Enter login infos:");
            System.out.print("Benutzerkennung: ");
            String benutzerkennung = scanner.nextLine();
            System.out.print("Passwort: ");
            String passwort = scanner.nextLine();

//            user user = userManagement.login(benutzerkennung, passwort);
            User user = shop.login(benutzerkennung, passwort);

            if (user != null) {
                if(user instanceof Kunde) {
                    loggedIn = true;
                    System.out.println("LogED IN.");
                    showeshop();
                }
                else {
                    boolean isValid = false;
                    while (!isValid) {
                        System.out.print(" 1. Artikelliste ausgeben");
                        System.out.print(" 2. Artikel hinzufuegen");
                        System.out.print(" 3. Kundenliste ausgeben");
                        System.out.print(" 4. Mitarbeiterliste ausgeben");
                        System.out.print(" 5. Mitarbeiter hinzufuegen");
                        System.out.print(" 6. Logout");

                        String input1 = scanner.nextLine();
                        switch (input1) {
                            case "1":

                                isValid = true;
                                break;
                            case "2":
                                registerUser();
                                isValid = true;
                                break;
                            case "3":
                                add();
                                isValid = true;
                                break;
                            case "exit":
                                isValid = true; // Allow exit to break the loop
                                break;
                            default:
                                System.out.println("choose from the list");
                        }
                    }
                    scanner.close();
                }

            } else {
                System.out.println("Login failed try again.");
            }
        }
    }

    private void add() {
        System.out.println("Enter article details:");
        System.out.print("Nummer: ");
        int nummer = Integer.parseInt(scanner.nextLine());

        System.out.print("Bezeichnung: ");
        String Bezeichnung = scanner.nextLine();
        System.out.print("Bestand: ");
        String Bestand = scanner.nextLine();
        System.out.print("price: ");
        float price;
        price = Float.parseFloat(scanner.nextLine());
        Article art = new Article(nummer, Bezeichnung, Bestand, price);
        products.addArticle(art);
        System.out.println("article created.");

//        actions();

    }

    private void showeshop() {

        List<Article> articles = products.getArticles();
        System.out.println("-------- article : ----------");
        for (Article article : articles) {
            System.out.println("Article Number: " + article.getArtikelnummer());
            System.out.println("Bezeichnung: " + article.getBezeichnung());
            System.out.println("Bestand: " + article.getBestand());
            System.out.println("Price: " + article.getPrice());
            System.out.println("----------------------");
        }

    }
}
