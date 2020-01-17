package client;

import client.view.ViewController;

import java.awt.*;

public class ClientApp {

    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new Controller());
    }

}


