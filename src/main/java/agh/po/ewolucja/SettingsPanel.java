package agh.po.ewolucja;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SettingsPanel extends JPanel {
    private HashMap<String, JLabel> labels = new HashMap<>();
    private HashMap<String, JTextField> textFields = new HashMap<>();
    private Config cfg;


    public SettingsPanel(Config c){
        cfg = c;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(500, 600));
        initLabels();
        initTextFields();
        setupLabels();

        add(new JPanel());
//
//        add(labels.get("mapWidth"));

    }

    private void setupLabels() {
        for(String key : labels.keySet()){
            labels.get(key).setLabelFor(textFields.get(key));
        }
    }

    private void initTextFields() {
        JTextField f1 = new JTextField();
        JTextField f2 = new JTextField();
        JTextField f3 = new JTextField();
        JTextField f4 = new JTextField();
        JTextField f5 = new JTextField();
        JTextField f6 = new JTextField();
        JTextField f7 = new JTextField();
        JTextField f8 = new JTextField();
        JTextField f9 = new JTextField();
        JTextField f10 = new JTextField();

        f1.setText(cfg.mapWidth.toString());
        textFields.put("mapWidth", f1);

        f2.setText(cfg.mapHeight.toString());
        textFields.put("mapHeight", f2);

        f3.setText(cfg.startEnergy.toString());
        textFields.put("startEnergy", f3);

        f4.setText(cfg.moveEnergy.toString());
        textFields.put("moveEnergy", f4);

        f5.setText(cfg.plantStartEnergy.toString());
        textFields.put("plantStartEnergy", f5);

        f6.setText(cfg.jungleWidth.toString());
        textFields.put("jungleWidth", f6);

        f7.setText(cfg.jungleHeight.toString());
        textFields.put("heightWidth", f7);

        f8.setText(cfg.iterations.toString());
        textFields.put("iterations", f8);

        f9.setText(cfg.stepTime.toString());
        textFields.put("stepTime", f9);

        f10.setText(cfg.startAnimals.toString());
        textFields.put("startAnimals", f10);
    }

    private void initLabels() {
        labels.put("mapWidth", new JLabel("word x size: "));
        labels.put("mapHeight", new JLabel("word y size: "));
        labels.put("startEnergy", new JLabel("start energy: "));
        labels.put("moveEnergy", new JLabel("move cost: "));
        labels.put("plantStartEnergy", new JLabel("plant start energy: "));
        labels.put("jungleWidth", new JLabel("jungle x size: "));
        labels.put("jungleHeight", new JLabel("jungle y size: "));
        labels.put("iterations", new JLabel("iterations: "));
        labels.put("stepTime", new JLabel("step time: "));
        labels.put("startAnimals", new JLabel("start animals: "));
    }
}
