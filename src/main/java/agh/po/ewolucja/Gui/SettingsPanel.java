package agh.po.ewolucja.Gui;

import agh.po.ewolucja.Config.Config;
import agh.po.ewolucja.Gui.MapWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SettingsPanel extends JPanel implements ActionListener {
    private static final int INPUT_SIZE = 5;

    private HashMap<String, JLabel> labels = new HashMap<>();
    private HashMap<String, JTextField> textFields = new HashMap<>();
    private HashMap<String, JPanel> panels = new HashMap<>();
    private Config defaultConfig;

    private JButton startButton;


    public SettingsPanel(Config c){
        defaultConfig = c;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initLabels();
        initTextFields();
        setupLabels();
        preparePanels();
        createLayout();
    }

    private void createLayout() {
        JPanel mapGroup = new JPanel();
        mapGroup.setLayout(new BoxLayout(mapGroup, BoxLayout.Y_AXIS));
        mapGroup.setBorder(BorderFactory.createTitledBorder("Map"));
        mapGroup.add(panels.get("mapWidth"));
        mapGroup.add(panels.get("mapHeight"));
        mapGroup.add(panels.get("jungleWidth"));
        mapGroup.add(panels.get("jungleHeight"));
        add(mapGroup);

        JPanel energyGroup = new JPanel();
        energyGroup.setLayout(new BoxLayout(energyGroup, BoxLayout.Y_AXIS));
        energyGroup.setBorder(BorderFactory.createTitledBorder("Energy"));
        energyGroup.add(panels.get("startEnergy"));
        energyGroup.add(panels.get("moveEnergy"));
        energyGroup.add(panels.get("plantStartEnergy"));
        add(energyGroup);

        JPanel startParamGroup = new JPanel();
        startParamGroup.setLayout(new BoxLayout(startParamGroup, BoxLayout.Y_AXIS));
        startParamGroup.setBorder(BorderFactory.createTitledBorder("Start parameters"));
        startParamGroup.add(panels.get("startAnimals"));
        startParamGroup.add(panels.get("iterations"));
        startParamGroup.add(panels.get("stepTime"));
        add(startParamGroup);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        startButton = new JButton("Zacznij symulacje");
        startButton.addActionListener(this);
        btnPanel.add(startButton, Component.CENTER_ALIGNMENT);
        add(btnPanel);
    }

    private void preparePanels() {
        for(String key : labels.keySet()){
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.add(labels.get(key));
            panel.add(textFields.get(key));
            panel.setMinimumSize(new Dimension(1,1));
            panels.put(key, panel);
        }
    }

    private void setupLabels() {
        for(String key : labels.keySet()){
            labels.get(key).setLabelFor(textFields.get(key));
        }
    }

    private void initTextFields() {
        JTextField f1 = new JTextField(INPUT_SIZE);
        JTextField f2 = new JTextField(INPUT_SIZE);
        JTextField f3 = new JTextField(INPUT_SIZE);
        JTextField f4 = new JTextField(INPUT_SIZE);
        JTextField f5 = new JTextField(INPUT_SIZE);
        JTextField f6 = new JTextField(INPUT_SIZE);
        JTextField f7 = new JTextField(INPUT_SIZE);
        JTextField f8 = new JTextField(INPUT_SIZE);
        JTextField f9 = new JTextField(INPUT_SIZE);
        JTextField f10 = new JTextField(INPUT_SIZE);

        f1.setText(defaultConfig.mapWidth.toString());
        textFields.put("mapWidth", f1);

        f2.setText(defaultConfig.mapHeight.toString());
        textFields.put("mapHeight", f2);

        f3.setText(defaultConfig.startEnergy.toString());
        textFields.put("startEnergy", f3);

        f4.setText(defaultConfig.moveEnergy.toString());
        textFields.put("moveEnergy", f4);

        f5.setText(defaultConfig.plantStartEnergy.toString());
        textFields.put("plantStartEnergy", f5);

        f6.setText(defaultConfig.jungleWidth.toString());
        textFields.put("jungleWidth", f6);

        f7.setText(defaultConfig.jungleHeight.toString());
        textFields.put("jungleHeight", f7);

        f8.setText(defaultConfig.iterations.toString());
        textFields.put("iterations", f8);

        f9.setText(defaultConfig.stepTime.toString());
        textFields.put("stepTime", f9);

        f10.setText(defaultConfig.startAnimals.toString());
        textFields.put("startAnimals", f10);
    }

    private void initLabels() {
        labels.put("mapWidth", new JLabel("world x size: "));
        labels.put("mapHeight", new JLabel("world y size: "));
        labels.put("jungleWidth", new JLabel("jungle x size: "));
        labels.put("jungleHeight", new JLabel("jungle y size: "));
        labels.put("startEnergy", new JLabel("start energy: "));
        labels.put("moveEnergy", new JLabel("move cost: "));
        labels.put("plantStartEnergy", new JLabel("plant start energy: "));
        labels.put("iterations", new JLabel("iterations: "));
        labels.put("stepTime", new JLabel("step time (ms): "));
        labels.put("startAnimals", new JLabel("start animals: "));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == startButton){
            Config c = getSettings();

            spawnMaps(c);
        }
    }

    private void spawnMaps(Config c) {
        MapWindow m1 = new MapWindow(c);
        MapWindow m2 = new MapWindow(c);
        m1.setLocation(0,0);
        m1.start();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        m2.setLocation((screenSize.width/2)-250,0);
        m2.start();
    }

    private Config getSettings() {
        Config c = new Config();
        c.iterations = c.stringToInt(textFields.get("iterations").getText());
        c.mapWidth = c.stringToInt(textFields.get("mapWidth").getText());
        c.mapHeight = c.stringToInt(textFields.get("mapHeight").getText());
        c.jungleWidth = c.stringToInt(textFields.get("jungleWidth").getText());
        c.jungleHeight = c.stringToInt(textFields.get("jungleHeight").getText());
        c.startEnergy = c.stringToInt(textFields.get("startEnergy").getText()).doubleValue();
        c.moveEnergy = c.stringToInt(textFields.get("moveEnergy").getText()).doubleValue();
        c.plantStartEnergy = c.stringToInt(textFields.get("plantStartEnergy").getText()).doubleValue();
        c.stepTime = c.stringToInt(textFields.get("stepTime").getText());
        c.startAnimals = c.stringToInt(textFields.get("startAnimals").getText());
        c.applyConfig();

        return c;
    }
}
