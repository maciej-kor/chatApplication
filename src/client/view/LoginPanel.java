package client.view;

import client.model.ChatClient;
import client.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginPanel extends JPanel {


    private ViewController viewController;
    private Controller controller;

    private JLabel jLabelUsername, jLabelPassword;
    private JTextField jTextFieldUsername;
    private JPasswordField jPasswordField;
    private JButton jButtonLogin;

    public LoginPanel(ViewController viewController, Controller controller) {

        this.viewController = viewController;
        this.controller = controller;

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
                boolean isLogged = performLogin(jTextFieldUsername.getText(), new String(jPasswordField.getPassword()));
                if (isLogged) {
                    viewController.createUserListFrame();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    private boolean performLogin(String nickName, String password) throws IOException {

        if (controller.checkLoginData(nickName, password)) {
            return true;
        } else {
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
