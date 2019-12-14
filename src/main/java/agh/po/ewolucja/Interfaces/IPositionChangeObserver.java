package agh.po.ewolucja.Interfaces;

import agh.po.ewolucja.Animal;
import agh.po.ewolucja.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Animal a);
}
