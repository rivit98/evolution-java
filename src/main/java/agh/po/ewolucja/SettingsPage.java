package agh.po.ewolucja;

import javax.swing.*;

public class SettingsPage extends JFrame {
    private final Integer WIDTH = 300;
    private final Integer HEIGHT = 400;


    public SettingsPage(Config c){
        super("Setup");

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); //center
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel settingsContent = new SettingsPanel(c);
        add(settingsContent);

        setVisible(true);
    }
}
