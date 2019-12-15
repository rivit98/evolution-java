package agh.po.ewolucja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class MapWindow extends JFrame implements ActionListener {
    private JungleMap map;
    private Timer timer;
    private Config cfg;
    private Renderer renderer;
    private InfoPanel infoPanel;
    private GridBagConstraints gc = new GridBagConstraints();
    private final int WIDTH = 1000;
    private final int HEIGHT = 700;

    public MapWindow(Config c){
        super("MapVisualizer");

        timer = new Timer(c.stepTime, this);
        cfg = c;
        map = new JungleMap(c.mapSize, c.jungleSize, cfg);

        infoPanel = new InfoPanel(this, map);
        renderer = new Renderer(this, map);

        int xTile = WIDTH / map.getMapCorners().upperRight.x;
        int yTile = HEIGHT / map.getMapCorners().upperRight.y;

        //setSize(xTile * map.getMapCorners().upperRight.x, yTile * map.getMapCorners().upperRight.y);

        getContentPane().setPreferredSize(new Dimension(xTile * map.getMapCorners().upperRight.x, yTile * map.getMapCorners().upperRight.y));
//        setResizable(false);

        //setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        GridBagLayout gl = new GridBagLayout();
        setLayout(gl);
        gl.rowWeights = new double[] {0.8, 0.2};
        addPanel(renderer, 0, 8);
        addPanel(infoPanel, 1, 2);
        pack();
    }

    public void addPanel (JPanel panel, int gridy, int gridwidth) {
        gc.gridx = 0;
        gc.gridy = gridy;
        gc.gridwidth = gridwidth;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = GridBagConstraints.BOTH;
        add(panel, gc);
    }

    public void start() {
        List<Vector2d> points = new PointsGenerator(map).getPool(new Rectangle(new Vector2d(0,0), cfg.mapSize), null);
        Collections.shuffle(points);
        points.subList(0, Math.min(cfg.startAnimals, points.size())).forEach(v -> map.place(new Animal(map, v, cfg.startEnergy)));

        setVisible(true);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == timer){
            renderer.repaint();
            infoPanel.repaint();
            if(map.getDay().equals(cfg.iterations)){
                timer.stop();
                return;
            }

            map.run();
        }
    }
}
