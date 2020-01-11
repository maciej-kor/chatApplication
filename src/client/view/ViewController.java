package client.view;

import client.Main;
import client.ChatClient;

import java.awt.*;

public class ViewController {

    private ChatClient chatClient;

    private MainFrame jFrame;

    public ViewController(ChatClient chatClient) {
        this.chatClient = chatClient;
        createHelloFrame();
    }

    private void createHelloFrame() {
        jFrame = new MainFrame();
        jFrame.setTitle("Login Panel");

        LoginPanel loginPanel = new LoginPanel(chatClient, this);
// UsersListPanel usersListPanel = new UsersListPanel(chatClient, this);
        jFrame.add(loginPanel);
        jFrame.pack();

        //set login window in center
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameDimension = jFrame.getSize();
        jFrame.setLocation((screenSize.width / 2 - frameDimension.width / 2), (screenSize.height / 2 - frameDimension.height / 2));

    }

    public void createUserListFrame() {
        jFrame.setVisible(false);
        jFrame = new MainFrame();
        jFrame.setTitle("All users");


    }

}
