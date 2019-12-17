package agh.po.ewolucja.Interfaces;

import agh.po.ewolucja.Classes.Animal;
import agh.po.ewolucja.Classes.Vector2d;

public interface IPositionChangeListener {
    void positionChanged(Vector2d oldPosition, Animal a);
}
