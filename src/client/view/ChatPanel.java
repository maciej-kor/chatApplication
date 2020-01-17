package client.view;

import client.ChatClient;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {

    private String sender;
    private String receiver;
    private ChatClient chatClient;
    private ViewController viewController;
    private boolean openWindow;
    static JTextArea messageJTextArea;

    public ChatPanel(String sender, String receiver, ChatClient chatClient, ViewController viewController) {

        this.sender = sender;
        this.receiver = receiver;
        this.chatClient = chatClient;
        this.viewController = viewController;

        this.setLayout(null);
        this.setVisible(true);

        JTextField jTextField = new JTextField();
        messageJTextArea = new JTextArea();
        JButton sendMessageButton = new JButton();
        messageJTextArea.setBounds(0,0,400, 450);
        messageJTextArea.setEditable(false);
        this.add(messageJTextArea);
        jTextField.setBounds(0,450,320,50);
        this.add(jTextField);
        sendMessageButton.setBounds(320,450,80,50);
        sendMessageButton.setText("Send");
        this.add(sendMessageButton);

        sendMessageButton.addActionListener(e -> {
            String msg = jTextField.getText().trim();
            if (msg.length() > 0){
                sendMessage(msg);
                jTextField.setText("");
            }
        });

    }

    public static void receiveMessage(String msgBody){
        messageJTextArea.append(msgBody);
        messageJTextArea.append("\n");
    }

    private void sendMessage(String msgBody){
        messageJTextArea.append(msgBody);
        messageJTextArea.append("\n");

        chatClient.msg(receiver, msgBody);
    }

    public boolean isOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(boolean openWindow) {
        this.openWindow = openWindow;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 500);
    }
}
