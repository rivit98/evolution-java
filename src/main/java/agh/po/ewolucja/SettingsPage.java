package agh.po.ewolucja;

import javax.swing.*;
import java.awt.*;

public class SettingsPage extends JFrame {
    private final Integer WIDTH = 500;
    private final Integer HEIGHT = 600;


    public SettingsPage(Config c){
        super();
        //setExtendedState(Frame.MAXIMIZED_BOTH);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); //center
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel settingsContent = new SettingsPanel(c);
        add(settingsContent);

        setVisible(true);
    }
}
