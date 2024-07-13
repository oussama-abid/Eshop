package ui;

import Client.*;

public class   Main {

    public static void main(String[] args) {

        //Gui.launch(Gui.class, args);
        //EShop shop = new EShop();
        // Cui cui = new Cui(shop);



        //  Client/server

        GuiClient.launch(GuiClient.class, args);
        Client client = new Client();
    }
}
