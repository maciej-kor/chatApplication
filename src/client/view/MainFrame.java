package client.view;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(){
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

    }

    public void centreFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameDimension = this.getSize();
        this.setLocation((screenSize.width / 2 - frameDimension.width / 2), (screenSize.height / 2 - frameDimension.height / 2));

    }

}
