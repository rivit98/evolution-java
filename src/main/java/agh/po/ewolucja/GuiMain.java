package agh.po.ewolucja;

public class GuiMain {
    public static void main(String args[]){
        Config c;
        if(args.length == 3){
            c = new ConfigParser().parse(args[2]);
            c.iterations = Integer.parseInt(args[0]);
            c.startAnimals = Integer.parseInt(args[1]);
        }else{
            c = new ConfigParser().parseDefault();
        }
        //c.applyConfig(); //TODO: move before world runs

        try{
            SettingsPage settingsPage = new SettingsPage(c);

        }catch (Exception ex){
            //TODO: change this
            ex.printStackTrace();
        }

    }
}
