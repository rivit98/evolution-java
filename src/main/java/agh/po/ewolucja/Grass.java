package agh.po.ewolucja;

public class Grass extends AbstractMapElement {
    public static Double DEFAULT_ENERGY_VALUE = 1.0;
    private static final Double MAX_ENERGY_VALUE = 20.0;

    public Grass(Vector2d position){
        super();
        this.position = position;
        this.energy = DEFAULT_ENERGY_VALUE;
    }

    public Grass(Vector2d position, Double energy){
        this(position);
        this.energy = energy;
    }

    public void grow(){
        energy = Math.min(energy+1, MAX_ENERGY_VALUE);
    }

    public String toString(){
        return "*";
    }
}
