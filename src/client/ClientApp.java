package client;

import client.controller.Controller;

import java.awt.*;

public class ClientApp {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new Controller());
    }

}


