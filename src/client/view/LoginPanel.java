package client.view;

import client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginPanel extends JPanel {

    private ChatClient chatClient;
    private ViewController viewController;

    private JLabel jLabelUsername, jLabelPassword;
    private JTextField jTextFieldUsername;
    private JPasswordField jPasswordField;
    private JButton jButtonLogin;

    public LoginPanel(ChatClient chatClient, ViewController viewController) {

        this.chatClient = chatClient;
        this.viewController = viewController;

        this.setVisible(true);
        this.setLayout(null);

        jLabelUsername = new JLabel();
        jLabelUsername.setText("Username:");
        jLabelUsername.setBounds(40, 30, 100, 25);
        add(jLabelUsername);

        jLabelPassword = new JLabel();
        jLabelPassword.setText("Password:");
        jLabelPassword.setBounds(40, 90, 100, 25);
        add(jLabelPassword);

        jTextFieldUsername = new JTextField();
        jTextFieldUsername.setBounds(150, 30, 150, 25);
        add(jTextFieldUsername);

        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(150, 90, 150, 25);
        add(jPasswordField);

        jButtonLogin = new JButton();
        jButtonLogin.setText("Login");
        jButtonLogin.setBounds(125, 150, 100, 40);
        add(jButtonLogin);

        jButtonLogin.addActionListener(e -> {
            try {
                performLogin(jTextFieldUsername.getText(), new String(jPasswordField.getPassword()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    private boolean performLogin(String username, String password) throws IOException {

        if (chatClient.logowanie(username, password)) {
            System.out.println("zalogowano");
            return true;
        } else {
            System.out.println("nie zalogowano");
            JOptionPane.showMessageDialog(this, "Login error");
            jTextFieldUsername.setText("");
            jPasswordField.setText("");
        }
        return false;
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 220);
    }
}
