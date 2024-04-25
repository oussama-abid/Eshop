package Entities;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb
{
    private List<WarenkorbArtikel> items = new ArrayList<>();


    public Warenkorb(List<WarenkorbArtikel> items) {
        super();
        this.items = items;
    }

    public List<WarenkorbArtikel> getItems() {
        return items;
    }

    public void setItems(List<WarenkorbArtikel> items) {
        this.items = items;
    }











}
