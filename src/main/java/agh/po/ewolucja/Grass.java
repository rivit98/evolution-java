package agh.po.ewolucja;

import java.awt.*;

public class Grass extends AbstractMapElement {
    private static final Double MAX_ENERGY_VALUE = 15.0;

    public Grass(Vector2d position){
        this.position = position;
    }

    public Grass(Vector2d position, Double e){
        this(position);
        energy = e;
    }

    public void grow(){
        energy = Math.min(energy+1, MAX_ENERGY_VALUE);
    }

    public String toString(){
        return "*";
    }

    @Override
    public Color getColor() {
        return new Color(0, (int) Math.max(100, 255-(energy*9)),0);
    }
}
