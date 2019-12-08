package agh.po.ewolucja;

public class Grass {
    public static Integer DEFAULT_ENERGY_VALUE = 1;
    private static final Integer MAX_ENERGY_VALUE = 20;
    private final Vector2d position;
    private Integer energy;

    public Grass(Vector2d position){
        this.position = position;
        this.energy = DEFAULT_ENERGY_VALUE;
    }

    public Grass(Vector2d position, Integer energy){
        this(position);
        this.energy = energy;
    }

    public void grow(){
        energy = Math.min(energy+1, MAX_ENERGY_VALUE);
    }

    public Integer getEnergyValue(){
        return energy;
    }

    public Vector2d getPosition(){
        return position;
    }

    public String toString(){
        return "*";
    }
}
