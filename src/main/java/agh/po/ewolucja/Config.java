package agh.po.ewolucja;

public class Config {
    public Integer mapWidth;
    public Integer mapHeight;
    public Integer startEnergy;
    public Integer moveEnergy;
    public Integer plantStartEnergy;
    public Integer jungleWidth;
    public Integer jungleHeight;

    public Integer iterations = 10000;
    public Integer startAnimals = 100;
    public Integer stepTime = 100; //msec
    public Vector2d jungleSize = new Vector2d(0,0);
    public Vector2d mapSize = new Vector2d(0,0);

    public void applyConfig(){
        Animal.COST_PER_MOVE = moveEnergy.doubleValue();
        Animal.INITIAL_ENERGY = startEnergy.doubleValue();
        Grass.DEFAULT_ENERGY_VALUE = plantStartEnergy.doubleValue();
        jungleSize = new Vector2d(jungleWidth, jungleHeight);
        mapSize = new Vector2d(mapWidth, mapHeight);
    }
}
