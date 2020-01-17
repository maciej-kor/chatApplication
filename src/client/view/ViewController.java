package client.view;

import client.ChatClient;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ViewController {

    private ChatClient chatClient;

    private MainFrame jFrame;

    private List<String> openChatList = new ArrayList<>();

    public ViewController(ChatClient chatClient) {
        this.chatClient = chatClient;
        createHelloFrame();
    }

    private void createHelloFrame() {

        jFrame = new MainFrame();
        jFrame.setTitle("Login Panel");
        LoginPanel loginPanel = new LoginPanel(chatClient, this);
        jFrame.add(loginPanel);
        jFrame.pack();
        jFrame.centreFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void createUserListFrame() {

        jFrame.setVisible(false);
        jFrame = new MainFrame();
        jFrame.setTitle("All users " + chatClient.getUserName());
        UsersListPanel usersListPanel = new UsersListPanel(chatClient, this);
        jFrame.add(usersListPanel);
        jFrame.pack();
        jFrame.centreFrame();

    }

    public void createChatWindow(String sender, String receiver){

        if (!openChatList.contains(receiver)) {
            openChatList.add(receiver);
            MainFrame chatFrame = new MainFrame();
            chatFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    openChatList.remove(receiver);
                }
            });

            chatFrame.setTitle("ChatApp - " + receiver);
            ChatPanel chatPanel = new ChatPanel(sender, receiver, chatClient, this);
            chatFrame.add(chatPanel);
            chatFrame.pack();
            chatFrame.centreFrame();
        }
    }


}
