package agh.po.ewolucja;

import agh.po.ewolucja.Config.Config;
import agh.po.ewolucja.Config.ConfigParser;
import agh.po.ewolucja.Gui.SettingsPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        Config c;
        if(args.length == 3){
            c = new ConfigParser().parse(args[2]);
            c.iterations = Integer.parseInt(args[0]);
            c.startAnimals = Integer.parseInt(args[1]);
        }else{
            c = new ConfigParser().parseDefault();
        }

        try{
            SwingUtilities.invokeLater(() -> {
                new SettingsPage(c);
            });

        }catch (Exception ex){
            //TODO: change this
            ex.printStackTrace();
        }
    }
}
