package client.view;

import client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UsersListPanel extends JPanel {

    private ChatClient chatClient;
    private ViewController viewController;
    private List<String> userList;

    public UsersListPanel(ChatClient chatClient, ViewController viewController){

        this.chatClient = chatClient;
        this.viewController = viewController;

        this.setVisible(true);

        try {
            userList = pobierzListeUzytkowniow();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // GridLayout gridLayout = new GridLayout(10, 1);

        //this.setLayout(GridLayout);

    }

    private List<String> pobierzListeUzytkowniow() throws IOException {
        return chatClient.getAllUsers();
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 500);
    }

}
