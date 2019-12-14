package agh.po.ewolucja.Interfaces;

import agh.po.ewolucja.Animal;
import agh.po.ewolucja.Vector2d;

public interface IObservable {
    void addObserver(IPositionChangeObserver observer);
    void removeObserver(IPositionChangeObserver observer);
    void notifyObservers(Vector2d oldPosition, Animal a);
}
