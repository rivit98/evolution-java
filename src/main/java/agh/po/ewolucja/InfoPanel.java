package agh.po.ewolucja;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    JFrame parent;
    JungleMap map;

    public InfoPanel(JFrame parent, JungleMap map){
        this.parent = parent;
        this.map = map;
        setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 0));


    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setSize(parent.getWidth(), getHeight());

        //translate coords system to left bottom cornerd
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(1.0, -1.0);
        g2.translate(0, -getHeight());

//        g.setColor(new Color(255, 86, 88));
//        g.fillRect(0,0, getWidth(), getHeight());

    }
}
