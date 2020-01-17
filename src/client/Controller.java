package client;

import client.view.ChatPanel;
import client.view.ViewController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Controller {

    private ChatClient chatClient;
    private ViewController viewController;

    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 9999;

    public Controller() {
        chatClient = new ChatClient(SERVER_NAME, SERVER_PORT, this);
        viewController = new ViewController(this);
    }

    public void wyslijWiadomosc(String sendTo, String messageBody) {

        chatClient.msg(sendTo, messageBody);

    }

    public void odbierzWiadomosc(String messageFrom, String messageBody) {

        List<ChatPanel> chatPanels = viewController.getChatPanelList();
        for (ChatPanel chatPanel : chatPanels){
            if (chatPanel.getSendToNickName().equals(messageFrom)){
                chatPanel.odbierzWiadomosc(messageBody);
            }
        }

    }

    public Map<String, Boolean> pobierzListeUzytkownikow() {

        return chatClient.getUserListAndOnlineStatus();

    }

    public boolean sprawdzDaneLogowania(String nickName, String password) {
        try {
            return chatClient.login(nickName, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void wyloguj(){
        try {
            chatClient.logOut();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String pobierzLoginUzytkownika() {
        return chatClient.getNickName();
    }

    public void aktualizujStatusLogowania() {
        viewController.getUsersListPanel().repaint();
    }


}
