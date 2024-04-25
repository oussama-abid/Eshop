package domain;

import java.util.ArrayList;
import java.util.List;


import Entities.Kunde;
import Entities.Mitarbeiter;
import Entities.User;


public class PersonenVerwaltung
{

    private List<Mitarbeiter> Mitarbeiter = new ArrayList<>();
    private List<Kunde> Kunde = new ArrayList<>();

    public PersonenVerwaltung() {

    }

    public void registriereMitarbeiter(int nummer, String name, String benutzerkennung, String passwort) {
        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzerkennung, passwort, passwort);
        // employee.setUsertype("Mitarbeiter");
        Mitarbeiter.add(mitarbeiter);
    }

    public void registriereKunde(int nummer, String name, String benutzerkennung, String passwort,String adresse) {
        Kunde kunde = new Kunde(nummer, name, benutzerkennung, passwort, passwort, adresse);
        //kunde.setUsertype("kunde");

        Kunde.add(kunde);
    }


    public  User login(String benutzerkennung, String passwort) {
        for (Kunde kunde: Kunde) {
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