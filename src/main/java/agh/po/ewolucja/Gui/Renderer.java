package agh.po.ewolucja.Gui;

import agh.po.ewolucja.Classes.Animal;
import agh.po.ewolucja.Classes.Vector2d;
import agh.po.ewolucja.Map.JungleMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Renderer extends JPanel implements MouseMotionListener, MouseListener {
    private MapWindow parent; //rather bad inteface between renderer and infopanel :/

    private JungleMap map;
    private int xTile;
    private int yTile;
    private boolean markingDominators = false;
    private Animal markedAnimal = null;

    public Renderer(MapWindow parent, JungleMap map){
        this.parent = parent;
        this.map = map;
        setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 0));
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        xTile = parent.getWidth() / map.getMapCorners().upperRight.x;
        yTile = getHeight() / map.getMapCorners().upperRight.y;

        setSize(xTile * map.getMapCorners().upperRight.x, yTile * map.getMapCorners().upperRight.y);

        //translate coords system to left bottom corner
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(1.0, -1.0);
        g2.translate(0, -getHeight());

        //background
        g.setColor(new Color(193, 243, 104));
        g.fillRect(0,0, getWidth(), getHeight());

        map.animalMap.values().forEach(a -> {
            g.setColor(a.getColor());
            g.fillOval(a.getPosition().x * xTile, a.getPosition().y * yTile, xTile, yTile);
        });

        //grass
        map.grassList.values().forEach(grass -> {
            g.setColor(grass.getColor());
            g.fillRect(grass.getPosition().x * xTile, grass.getPosition().y * yTile, xTile, yTile);
        });

        if(markingDominators){
            map.getDominators().forEach(a -> {
                g.setColor(Color.BLUE);
                g.fillOval(a.getPosition().x * xTile, a.getPosition().y * yTile, xTile, yTile);
            });
        }

        parent.updateInfo();

        if(markedAnimal != null){
            parent.oneAnimalStatsUpdate(markedAnimal);
            if(markedAnimal.isAlive()){
                g.setColor(new Color(217, 0, 212));
                g.fillOval(markedAnimal.getPosition().x * xTile, markedAnimal.getPosition().y * yTile, xTile, yTile);
            }else{
                markedAnimal = null;
                parent.stopSimulation();
            }
        }
    }

    private Vector2d getNormalizedCoords(MouseEvent e){
        int x = e.getX();
        int y = getHeight() - e.getY();
        return new Vector2d(x/xTile, y/yTile);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(parent.isSimulationRunning()){
            return;
        }

        parent.updateStats(getNormalizedCoords(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(parent.isSimulationRunning()){
            return;
        }
        markedAnimal = map.getStrongestAtPosition(getNormalizedCoords(e));
        if(markedAnimal != null){
            this.repaint();
        }
    }

    public void markingDominators(){
        markingDominators = !markingDominators;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
