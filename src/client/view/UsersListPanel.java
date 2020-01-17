package client.view;

import client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UsersListPanel extends JPanel {

    private ChatClient chatClient;
    private ViewController viewController;
    private List<String> userList;

    public UsersListPanel(ChatClient chatClient, ViewController viewController) {

        this.chatClient = chatClient;
        this.viewController = viewController;

        this.setVisible(true);

        userList = chatClient.getUserList();
        Collections.sort(userList, (a, b) -> a.compareToIgnoreCase(b));

        this.setLayout(null);

        addButtons();

    }

    private List<String> getUserList() throws IOException {
        return chatClient.getAllUsers();
    }

    private void addButtons() {

        List<JButton> buttonList = new ArrayList<>();
        int x = 0, y = 50;

        for (int i = 0; i < userList.size(); i++) {

            JButton jButton = new JButton();
            jButton.setText(userList.get(i));
            jButton.setBounds(x * i, y * i, 150, 50);
            jButton.addActionListener(e -> {
                    viewController.createChatWindow(chatClient.getUserName(), jButton.getText());
            });
            buttonList.add(jButton);
            this.add(jButton);

        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 300);
    }

}
