package Entities;

public class User
{
    private int usernummer;
    private String name;
    private String Benutzerkennung;
    private String Passwort;
//    private String usertype;





    public User (int usernummer, String name, String benutzerkennung, String passwort, String usertype) {
        super();
        this.usernummer = usernummer;
        this.name = name;
        Benutzerkennung = benutzerkennung;
        Passwort = passwort;
//		this.usertype = usertype;
    }



    public int getUsernummer() {
        return usernummer;
    }
    public void setUsernummer(int usernummer) {
        this.usernummer = usernummer;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getBenutzerkennung() {
        return Benutzerkennung;
    }
    public void setBenutzerkennung(String benutzerkennung) {
        Benutzerkennung = benutzerkennung;
    }


    public String getPasswort() {
        return Passwort;
    }

    public void setPasswort(String passwort) {
        Passwort = passwort;
    }

/* 	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	} */

}
