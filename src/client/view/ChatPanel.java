package client.view;

import client.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {

    private String sendToNickName;
    private Controller controller;
    private boolean openWindow;
    private JTextArea messageJTextArea;


    public ChatPanel(String sendToNickName, Controller controller) {

        this.sendToNickName = sendToNickName;
        this.controller = controller;

        this.setLayout(null);
        this.setVisible(true);

        JTextField jTextField = new JTextField();
        messageJTextArea = new JTextArea();
        JButton sendMessageButton = new JButton();
        messageJTextArea.setBounds(0, 0, 400, 450);
        messageJTextArea.setEditable(false);
        this.add(messageJTextArea);
        jTextField.setBounds(0, 450, 320, 50);
        this.add(jTextField);
        sendMessageButton.setBounds(320, 450, 80, 50);
        sendMessageButton.setText("Send");
        this.add(sendMessageButton);

        sendMessageButton.addActionListener(e -> {
            String msg = jTextField.getText().trim();
            if (msg.length() > 0) {
                sendMessage(msg);
                jTextField.setText("");
            }
        });

    }

    public void receiveMessage(String messageBody){
        messageJTextArea.append(sendToNickName + ": ");
        messageJTextArea.append(messageBody);
        messageJTextArea.append("\n");
    }

    private void sendMessage(String messageBody) {
        messageJTextArea.append("you: ");
        messageJTextArea.append(messageBody);
        messageJTextArea.append("\n");

        controller.sendMessage(sendToNickName, messageBody);
    }

    public boolean isOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(boolean openWindow) {
        this.openWindow = openWindow;
    }

    public String getSendToNickName() {
        return sendToNickName;
    }

    public void setSendToNickName(String sendToNickName) {
        this.sendToNickName = sendToNickName;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 500);
    }
}
