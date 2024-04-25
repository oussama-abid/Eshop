public class EShop {

    private Usermanagment userManagement;

    public EShop() {
        this.userManagement = new Usermanagment();
    }

    public User login(String benutzerkennung, String passwort) {
        return userManagement.login(benutzerkennung, passwort);
    }

    // public Article addArticle() {
    //     articleManagement.addArticle();
    // }
}
