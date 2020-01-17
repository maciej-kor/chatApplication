package client.view;

import client.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class UsersListPanel extends JPanel {

    private ViewController viewController;
    private Controller controller;
    private List<JButton> buttonList;

    private Map<String, Boolean> onlineStatusMap;

    public UsersListPanel(ViewController viewController, Controller controller) {
        this.viewController = viewController;
        this.controller = controller;

        this.setVisible(true);
        this.setLayout(null);

        setOnlineStatusMap(controller.pobierzListeUzytkownikow());

        this.addButtons();
    }

    private void addButtons() {

        buttonList = new ArrayList<>();
        int dx = 0, dy = 50;
        int i = 0;

        for (String s : onlineStatusMap.keySet()) {
            JButton jButton = new JButton();
            jButton.setText(s);
            jButton.setBounds(dx, dy * i, 250, 50);
            jButton.addActionListener(e ->{
                viewController.createChatWindow(jButton.getText());
            });
            buttonList.add(jButton);
            this.add(jButton);
            i++;
        }

    }

    public Map<String, Boolean> getOnlineStatusMap() {
        return onlineStatusMap;
    }

    public void setOnlineStatusMap(Map<String, Boolean> onlineStatusMap) {
        this.onlineStatusMap = onlineStatusMap;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        int dy = 50;
        int i = 0;

        for (String s : onlineStatusMap.keySet()) {

            if (onlineStatusMap.get(s)) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }

            g.fillRect(250, dy*i,50, 50);
            g.setColor(Color.black);
            g.drawRect(250, dy*i,50, 50);
            i++;

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

}
