package agh.po.ewolucja;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Animal a);
}
