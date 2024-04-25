
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usermanagment {

    private List<Mitarbeiter> employees = new ArrayList<>();
    private List<Kunde> customers = new ArrayList<>();
    public Usermanagment() {

    }

    public void registerEmployee(int nummer, String name, String benutzerkennung, String passwort) {
        Mitarbeiter employee = new Mitarbeiter(nummer, name, benutzerkennung, passwort, passwort);
        // employee.setUsertype("Mitarbeiter");
        employees.add(employee);
    }

    public void registerkunde(int nummer, String name, String benutzerkennung, String passwort,String adresse) {
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, passwort, passwort, adresse);
        //kunde.setUsertype("kunde");

        customers.add(kunde);
    }


    public User login(String benutzerkennung, String passwort) {
        for (Kunde kunde: customers) {
            if (benutzerkennung.equals(kunde.getBenutzerkennung())   &&  passwort.equals(kunde.getPasswort() )){
                return kunde;
            }
        }
        //    for(int i = 0; i < customers.size();i++){
        // 	if (benutzerkennung.equals(customers.get(i).getBenutzerkennung())   &&  passwort.equals(customers.get(i).getPasswort() )){
        // 		return customers.get(i);

        // 	}
//	   }
        return null;

    }






}
