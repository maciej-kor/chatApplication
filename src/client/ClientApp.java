package client;

import client.view.ViewController;

import java.awt.*;

public class ClientApp {

    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {

        ChatClient chatClient = new ChatClient(SERVER_NAME, SERVER_PORT);

        EventQueue.invokeLater(() -> new ViewController(chatClient));

    }

}
