package agh.po.ewolucja.Interfaces;

import agh.po.ewolucja.Classes.Vector2d;

import java.awt.*;

public interface IMapElement {
    Vector2d getPosition();
    void setPosition(Vector2d pos);
    Color getColor();
}
