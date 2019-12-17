package agh.po.ewolucja.Config;

import agh.po.ewolucja.Classes.Vector2d;

public class Config {
    public Integer mapWidth;
    public Integer mapHeight;
    public Double startEnergy;
    public Double moveEnergy;
    public Double plantStartEnergy;
    public Integer jungleWidth;
    public Integer jungleHeight;

    public Integer iterations = 10000;
    public Integer startAnimals = 40;
    public Integer stepTime = 50; //msec
    public Vector2d jungleSize = new Vector2d(0,0);
    public Vector2d mapSize = new Vector2d(0,0);

    public void applyConfig(){
        jungleSize = new Vector2d(jungleWidth, jungleHeight);
        mapSize = new Vector2d(mapWidth, mapHeight);
    }
    public Integer stringToInt(String s){
        return Double.valueOf(s).intValue();
    }
}
