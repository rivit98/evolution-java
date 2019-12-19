package agh.po.ewolucja.Gui;

import agh.po.ewolucja.Classes.Animal;
import agh.po.ewolucja.Classes.Genotype;
import agh.po.ewolucja.Classes.Vector2d;
import agh.po.ewolucja.Map.JungleMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class InfoPanel extends JPanel implements ActionListener {
    private MapWindow parent;
    private JungleMap map;
    private GridBagConstraints gc = new GridBagConstraints();
    private HashMap<String, JLabel> labels = new HashMap<>();
    private JProgressBar pb;
    private JButton highlighterBtn = new JButton("Zaznacz dominatorow");
    private JButton exportButton = new JButton("Zapisz statystyki");
    private JButton stoper = new JButton("Start/Stop");
    private JPanel oneAnimalInformations = new JPanel();
    private JPanel highlightedAnimalInformation = new JPanel();

    public InfoPanel(MapWindow parent, JungleMap map){
        this.parent = parent;
        this.map = map;
        setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 0));
        GridBagLayout gl = new GridBagLayout();
        gl.columnWeights = new double[] {0.2, 0.5, 0.3};
        setLayout(gl);

        initComponents();
    }

    private void initComponents() {
        JPanel simulationDays = new JPanel(new FlowLayout());
        pb = new JProgressBar(0, map.cfg.iterations);
        pb.setStringPainted(true);
        simulationDays.add(new JLabel("Postep: "));
        simulationDays.add(pb);
        addPanel(simulationDays, 0, 0);

        JPanel animalsNum = new JPanel(new FlowLayout());
        labels.put("animalsNum", new JLabel("0"));
        animalsNum.add(new JLabel("Liczba zwierząt: "));
        animalsNum.add(labels.get("animalsNum"));
        addPanel(animalsNum, 0, 1);

        JPanel grassNum = new JPanel(new FlowLayout());
        labels.put("grassNum", new JLabel("0"));
        grassNum.add(new JLabel("Liczba roślin: "));
        grassNum.add(labels.get("grassNum"));
        addPanel(grassNum, 0, 2);

        JPanel dominatingGene = new JPanel(new FlowLayout());
        labels.put("dominatingGene", new JLabel("-"));
        dominatingGene.add(new JLabel("Domin. gen: "));
        dominatingGene.add(new JLabel(" "));
        dominatingGene.add(labels.get("dominatingGene"));
        addPanel(dominatingGene, 0, 3);

        JPanel avgEnergy = new JPanel(new FlowLayout());
        labels.put("avgEnergy", new JLabel("0"));
        avgEnergy.add(new JLabel("Średnia energia: "));
        avgEnergy.add(labels.get("avgEnergy"));
        addPanel(avgEnergy, 0, 4);

        JPanel avgLifeTime = new JPanel(new FlowLayout());
        labels.put("avgLifeTime", new JLabel("0"));
        avgLifeTime.add(new JLabel("Średnia długość życia: "));
        avgLifeTime.add(labels.get("avgLifeTime"));
        addPanel(avgLifeTime, 0, 5);

        JPanel avgKids = new JPanel(new FlowLayout());
        labels.put("avgKids", new JLabel("0"));
        avgKids.add(new JLabel("Średnia ilość dzieci: "));
        avgKids.add(labels.get("avgKids"));
        addPanel(avgKids, 0, 6);

        JPanel highlighter = new JPanel(new FlowLayout());
        highlighterBtn.addActionListener(this);
        highlighter.add(highlighterBtn);
        addPanel(highlighter, 0, 7);

        JPanel startStop = new JPanel(new FlowLayout());
        stoper.addActionListener(this);
        startStop.add(stoper);
        addPanel(startStop, 0, 9);

        JPanel btnSave = new JPanel(new FlowLayout());
        exportButton.addActionListener(this);
        btnSave.add(exportButton);
        addPanel(btnSave, 0, 8);

        //second column, info about one animal
        oneAnimalInformations.setLayout(new BoxLayout(oneAnimalInformations, BoxLayout.PAGE_AXIS));
        JLabel labelka = new JLabel("Statystyki zwierzęcia pod kursorem:");
        labelka.setForeground(Color.RED);
        oneAnimalInformations.add(labelka);
        labels.put("oneAnimalGenotype", new JLabel());
        oneAnimalInformations.add(new JLabel("Genotyp:"));
        oneAnimalInformations.add(labels.get("oneAnimalGenotype"));
        oneAnimalInformations.add(new JLabel(" "));

        labels.put("oneAnimalEnergy", new JLabel());
        oneAnimalInformations.add(new JLabel("Energia: "));
        oneAnimalInformations.add(labels.get("oneAnimalEnergy"));
        oneAnimalInformations.add(new JLabel(" "));

        labels.put("oneAnimalKids", new JLabel());
        oneAnimalInformations.add(new JLabel("Liczba dzieci: "));
        oneAnimalInformations.add(labels.get("oneAnimalKids"));
        oneAnimalInformations.add(new JLabel(" "));

        labels.put("oneAnimalDescendant", new JLabel());
        oneAnimalInformations.add(new JLabel("Potomkowie: "));
        oneAnimalInformations.add(labels.get("oneAnimalDescendant"));

        gc.gridheight = 8;
        addPanel(oneAnimalInformations, 1, 0);

        highlightedAnimalInformation.setLayout(new BoxLayout(highlightedAnimalInformation, BoxLayout.PAGE_AXIS));

        JLabel labelka2 = new JLabel("Statystyki wybranego zwierzęcia:");
        labelka2.setForeground(Color.RED);
        highlightedAnimalInformation.add(labelka2);

        labels.put("highlightedAnimalEnergy", new JLabel());
        highlightedAnimalInformation.add(new JLabel("Energia:"));
        highlightedAnimalInformation.add(labels.get("highlightedAnimalEnergy"));
        highlightedAnimalInformation.add(new JLabel(" "));

        labels.put("highlightedAnimalKids", new JLabel());
        highlightedAnimalInformation.add(new JLabel("Dzieci od czasu zaznaczenia:"));
        highlightedAnimalInformation.add(labels.get("highlightedAnimalKids"));
        highlightedAnimalInformation.add(new JLabel(" "));

        labels.put("highlightedAnimalDescendant", new JLabel());
        highlightedAnimalInformation.add(new JLabel("Potomkowie od czasu zaznaczenia:"));
        highlightedAnimalInformation.add(labels.get("highlightedAnimalDescendant"));
        highlightedAnimalInformation.add(new JLabel(" "));

        labels.put("highlightedAnimalDeadDay", new JLabel());
        highlightedAnimalInformation.add(new JLabel("Dzien w ktorym zdechlo:"));
        highlightedAnimalInformation.add(labels.get("highlightedAnimalDeadDay"));
        highlightedAnimalInformation.add(new JLabel(" "));

        gc.gridheight = 8;
        addPanel(highlightedAnimalInformation, 2, 0);
    }

    private void addPanel(JPanel p, int x, int y){
        gc.gridx = x;
        gc.gridy = y;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(p, gc);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setSize(parent.getWidth(), getHeight());

        Genotype dominGene = map.mostPopularGenotype();
        Integer avgE = map.getAvgEnergy();
        Integer avgL = map.getAvgLifeTime();
        Double avgK = map.getAvgKidsNum();
        pb.setValue(map.getDay());
        labels.get("animalsNum").setText("" + map.getAnimalsNum().toString() + " (martwych: " + map.getDeadAnimalsNum().toString() + ")");
        labels.get("grassNum").setText(map.getGrassNum().toString());
        labels.get("dominatingGene").setText(dominGene.toString());
        labels.get("avgEnergy").setText(avgE.toString());
        labels.get("avgLifeTime").setText(avgL.toString());
        labels.get("avgKids").setText(avgK.toString());

        if(parent.statsManager.getLastDay() != map.getDay()){
            parent.statsManager.addNewStat(map.getDay(), map.getAnimalsNum(), map.getGrassNum(), dominGene, avgE, avgL, avgK);
        }
    }

    public void clearOneAnimalStats() {
        labels.get("oneAnimalGenotype").setText("");
        labels.get("oneAnimalEnergy").setText("");
        labels.get("oneAnimalKids").setText("");
        labels.get("oneAnimalDescendant").setText("");
    }

    public void oneAnimalStatsUpdate(Animal a){
        labels.get("highlightedAnimalKids").setText(a.getKids().toString());
        labels.get("highlightedAnimalDescendant").setText(a.getDescendants().toString());
        if(!a.isAlive()){
            labels.get("highlightedAnimalDeadDay").setText(map.getDay().toString());
            labels.get("highlightedAnimalEnergy").setText("");
        }else{
            labels.get("highlightedAnimalDeadDay").setText("");
            labels.get("highlightedAnimalEnergy").setText(String.valueOf(Math.round(a.getEnergy() * 100) / 100.0));
        }
    }

    public void updateStats(Vector2d pos){
        Animal a = map.getStrongestAtPosition(pos);
        if(a != null){
            labels.get("oneAnimalGenotype").setText(a.getGenotype().toString());
            labels.get("oneAnimalEnergy").setText(String.valueOf(Math.round(a.getEnergy())));
            labels.get("oneAnimalKids").setText(a.getKids().toString());
            labels.get("oneAnimalDescendant").setText(a.getDescendants().toString());
//            oneAnimalInformations.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == stoper){
            parent.flipState();
        }else if(src == exportButton){
            parent.stopSimulation();
            parent.saveStats();
        }else{
            parent.markDominators();
        }
    }
}
