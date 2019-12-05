package agh.po.ewolucja;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Multimap<Vector2d, Animal> animalMap = LinkedListMultimap.create();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);

    @Override
    public boolean place(Animal a) {
        if (this.objectAt(a.getPosition()) != null) {
//            throw new IllegalArgumentException("This field is occupied: " + a.getPosition());
            return false;
        }

        //TODO: send to Harness
        animalMap.put(a.getPosition(), a);
        a.addObserver(this);
        return true;
    }

    @Override
    public Vector2d getFreeSpot(Vector2d origin) {
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                if(x == 0 && y == 0){
                    continue;
                }
                Vector2d newPos = new Vector2d(0,0).add(origin);
                if(this.objectAt(newPos.add(new Vector2d(x, y))) != null){
                    return newPos;
                }
            }
        }
        return null;
    }

    @Override
    public void run() {
        Integer size = this.animalMap.size();
        if (size.equals(0)) {
            return;
        }

        List<Animal> animalListConverted = new LinkedList<>(this.animalMap.values());
        for (Animal animal : animalListConverted) {
            animal.moveTo(animal.movePre());
        }
    }

    @Override
    public boolean canMoveTo(Vector2d newPosition) {
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.animalMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return this.animalMap.get(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Animal a){
        //TODO: send to Harness
        this.animalMap.remove(oldPosition, a);
        this.animalMap.put(a.getPosition(), a);
    }

    protected abstract Rectangle getMapCorners();

    public String toString(){
        Rectangle result = this.getMapCorners();
        return this.mapVisualizer.draw(result.lowerLeft, result.upperRight);
    }
}