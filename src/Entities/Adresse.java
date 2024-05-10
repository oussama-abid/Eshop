package Entities;

public class Adresse {
    private String straße;
    private String stadt;
    private String bundesland;
    private int postleitzahl;
    private String land;


    public Adresse(String straße, String stadt, String bundesland, int postleitzahl, String land) {
        this.straße = straße;
        this.stadt = stadt;
        this.bundesland = bundesland;
        this.postleitzahl = postleitzahl;
        this.land = land;
    }

    public String getStraße() {
        return straße;
    }

    public void setStraße(String straße) {
        this.straße = straße;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public String getBundesland() {
        return bundesland;
    }

    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    public int getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(int postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    @Override
    public String toString() {

        return "land:" + getLand() + ", bundesland=" + getBundesland() + ", stadt=" + getStadt() + ", straße :" + getStraße()+ ", PLZ :" + getPostleitzahl();

    }
}

