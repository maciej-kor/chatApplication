package client.view;

import client.Controller;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ViewController {

    private MainFrame jFrame;
    private List<String> openChatList = new ArrayList<>();
    private Controller controller;
    private UsersListPanel usersListPanel;
    private List<ChatPanel> chatPanelList = new ArrayList<>();

    public ViewController(Controller controller) {
        this.controller = controller;
        createHelloFrame();
    }

    public void createHelloFrame() {

        jFrame = new MainFrame();
        jFrame.setTitle("Login Panel");
        LoginPanel loginPanel = new LoginPanel(this, controller);
        jFrame.add(loginPanel);
        jFrame.pack();
        jFrame.centreFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void createUserListFrame() {

        jFrame.setVisible(false);
        jFrame = new MainFrame();
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.wyloguj();
            }
        });
        jFrame.setTitle("All users " + controller.pobierzLoginUzytkownika());
        usersListPanel = new UsersListPanel(this, controller);
        jFrame.add(usersListPanel);
        jFrame.pack();
        jFrame.centreFrame();

    }

    public void createChatWindow(String sendTo){

        if (!openChatList.contains(sendTo)) {
            openChatList.add(sendTo);
            MainFrame chatFrame = new MainFrame();
            chatFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    openChatList.remove(sendTo);
                }
            });

            chatFrame.setTitle("ChatApp - " + sendTo);
            ChatPanel chatPanel = new ChatPanel(sendTo, controller);
            chatPanelList.add(chatPanel);
            chatFrame.add(chatPanel);
            chatFrame.pack();
            chatFrame.centreFrame();
        }
    }


    public UsersListPanel getUsersListPanel() {
        return usersListPanel;
    }

    public void setUsersListPanel(UsersListPanel usersListPanel) {
        this.usersListPanel = usersListPanel;
    }

    public List<ChatPanel> getChatPanelList() {
        return chatPanelList;
    }

    public void setChatPanelList(List<ChatPanel> chatPanelList) {
        this.chatPanelList = chatPanelList;
    }
}
