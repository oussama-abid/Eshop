package ui;

import domain.PersonenVerwaltung;
import Entities.User;
import java.util.ArrayList;
import java.util.List;

public class  Main {
    public static void main(String[] args) {
        PersonenVerwaltung user = new PersonenVerwaltung();

        EShop shop = new EShop();


        Cui cui = new Cui(shop);
    }
}
