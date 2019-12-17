package agh.po.ewolucja.Classes;

import agh.po.ewolucja.Interfaces.IMapElement;
import agh.po.ewolucja.Interfaces.IObservable;
import agh.po.ewolucja.Interfaces.IPositionChangeListener;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMapElement implements IMapElement, IObservable {
    protected Vector2d position;
    protected List<IPositionChangeListener> observers = new LinkedList<>();
    protected Double energy = 0.0;

    public Double getEnergy(){
        return this.energy;
    }

    public void addEnergy(Double e){
        this.energy += e;
        if(this.energy < 0){
            this.energy = 0.0;
        }
    }

    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public void setPosition(Vector2d pos){
        this.position = pos;
    }

    @Override
    public abstract Color getColor();

    @Override
    public void addObserver(IPositionChangeListener observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(IPositionChangeListener observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(Vector2d oldPosition, Animal a) {
        this.observers.forEach(v -> v.positionChanged(oldPosition, a));
    }
}
