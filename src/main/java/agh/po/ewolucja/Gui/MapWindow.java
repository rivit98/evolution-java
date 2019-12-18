package agh.po.ewolucja.Gui;

import agh.po.ewolucja.Classes.Animal;
import agh.po.ewolucja.Classes.Vector2d;
import agh.po.ewolucja.Config.Config;
import agh.po.ewolucja.Classes.Rectangle;
import agh.po.ewolucja.Map.JungleMap;
import agh.po.ewolucja.Map.PointsGenerator;

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
    private agh.po.ewolucja.Gui.Renderer renderer;
    private InfoPanel infoPanel;
    private GridBagConstraints gc = new GridBagConstraints();
    private final int WIDTH = 600;
    private final int HEIGHT = 700;

    public MapWindow(Config c) {
        super("MapVisualizer");

        timer = new Timer(c.stepTime, this);
        cfg = c;
        map = new JungleMap(c.mapSize, c.jungleSize, cfg);

        infoPanel = new InfoPanel(this, map);
        renderer = new Renderer(this, map);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 2;
        double height = screenSize.getHeight() - 70;

        int xTile = (int) (width / map.getMapCorners().upperRight.x);
        int yTile = (int) (height / map.getMapCorners().upperRight.y);

        getContentPane().setPreferredSize(new Dimension(xTile * map.getMapCorners().upperRight.x, yTile * map.getMapCorners().upperRight.y));
//        setResizable(false);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        GridBagLayout gl = new GridBagLayout();
        setLayout(gl);

        gl.rowWeights = new double[]{0.8, 0.2};
        addPanel(renderer, 0, 2);
        addPanel(infoPanel, 3, 1);
        pack();
    }

    public void addPanel(JPanel panel, int gridy, int gridwidth) {
        gc.gridx = 0;
        gc.gridy = gridy;
        gc.gridheight = gridwidth;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = GridBagConstraints.BOTH;
        add(panel, gc);
    }

    public void updateInfo() {
        infoPanel.repaint();
    }

    public void start() {
        List<Vector2d> points = new PointsGenerator(map).getPool(new Rectangle(new Vector2d(0, 0), cfg.mapSize), null);
        Collections.shuffle(points);
        points.subList(0, Math.min(cfg.startAnimals, points.size())).forEach(v -> map.place(new Animal(map, v, cfg.startEnergy)));

        setVisible(true);
        startSimulation();
    }

    public void flipState() {
        if (timer.isRunning()) {
            stopSimulation();
        } else {
            startSimulation();
        }
    }

    public void oneAnimalStatsUpdate(Animal a) {
        infoPanel.oneAnimalStatsUpdate(a);
    }

    public void startSimulation() {
        timer.start();
        infoPanel.clearOneAnimalStats();
    }

    public void stopSimulation() {
        timer.stop();
    }

    public boolean isSimulationRunning() {
        return timer.isRunning();
    }

    public void updateStats(Vector2d pos) {
        infoPanel.updateStats(pos);
    }

    public void markDominators() {
        renderer.markingDominators();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == timer) {
            renderer.repaint();
            if (map.getDay().equals(cfg.iterations)) {
                stopSimulation();
                return;
            }

            map.run();
        }
    }
}
