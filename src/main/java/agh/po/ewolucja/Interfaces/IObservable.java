package agh.po.ewolucja.Interfaces;

import agh.po.ewolucja.Classes.Animal;
import agh.po.ewolucja.Classes.Vector2d;

public interface IObservable {
    void addObserver(IPositionChangeListener observer);
    void removeObserver(IPositionChangeListener observer);
    void notifyObservers(Vector2d oldPosition, Animal a);
}
