package agh.po.ewolucja;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    JFrame parent;
    JungleMap map;

    public Renderer(JFrame parent, JungleMap map){
        this.parent = parent;
        this.map = map;
        setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 0));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        int xTile = parent.getWidth() / map.getMapCorners().upperRight.x;
        int yTile = getHeight() / map.getMapCorners().upperRight.y;

        setSize(xTile * map.getMapCorners().upperRight.x, yTile * map.getMapCorners().upperRight.y);

        //translate coords system to left bottom corner
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(1.0, -1.0);
        g2.translate(0, -getHeight());

        //background
        g.setColor(new Color(193, 243, 104));
        g.fillRect(0,0, parent.getWidth(), getHeight());

        map.animalMap.values().forEach(a -> {
            g.setColor(a.getColor());
            g.fillRect(a.getPosition().x * xTile, a.getPosition().y * yTile, xTile, yTile);
//            g.fillOval(a.getPosition().x * xTile, a.getPosition().y * yTile, xTile, yTile);
        });

        map.grassList.values().forEach(grass -> {
            g.setColor(grass.getColor());
            g.fillRect(grass.getPosition().x * xTile, grass.getPosition().y * yTile, xTile, yTile);
        });

    }
}
