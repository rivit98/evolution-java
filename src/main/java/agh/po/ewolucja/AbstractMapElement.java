package agh.po.ewolucja;

import agh.po.ewolucja.Interfaces.IMapElement;
import agh.po.ewolucja.Interfaces.IObservable;
import agh.po.ewolucja.Interfaces.IPositionChangeObserver;

import java.util.LinkedList;
import java.util.List;

public class AbstractMapElement implements IMapElement, IObservable {
    protected Vector2d position;
    protected List<IPositionChangeObserver> observers = new LinkedList<>();
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
    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(Vector2d oldPosition, Animal a) {
        this.observers.forEach(v -> v.positionChanged(oldPosition, a));
    }
}
