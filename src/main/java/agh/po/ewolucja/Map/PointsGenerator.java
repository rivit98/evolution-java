package agh.po.ewolucja.Map;

import agh.po.ewolucja.Classes.Rectangle;
import agh.po.ewolucja.Classes.Vector2d;
import agh.po.ewolucja.Interfaces.IWorldMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//reimplement with set
public class PointsGenerator {
    private final IWorldMap map;
    private final Random rand = new Random();

    public PointsGenerator(IWorldMap map){
        this.map = map;
    }

    public List<Vector2d> getPool(Rectangle area, Rectangle exclude){
        List<Vector2d> pool = new LinkedList<>();
        Vector2d ll = area.lowerLeft;
        Vector2d ur = area.upperRight;

        for(int i = ll.x; i < ur.x; i++){
            for(int j = ll.y; j < ur.y; j++) {
                Vector2d v = new Vector2d(i, j);

                if(exclude != null){
                    if(exclude.isInside(v)){
                        continue;
                    }
                }

                if (map.objectAt(v) == null) {
                    pool.add(v);
                }
            }
        }

        return pool;
    }

    public List<Vector2d> getRandomPoints(Rectangle area, Rectangle exclude, Integer PERCENT){

        if(PERCENT > 100){
            throw new IllegalArgumentException("PERCENT must be <= 100");
        }

        List<Vector2d> res = new ArrayList<>();
        List<Vector2d> pool = this.getPool(area, exclude);

        Integer howManyGrass = (PERCENT * pool.size()) / 100;
        while(!(howManyGrass--).equals(0) && pool.size() > 0){
            int randomIdx = rand.nextInt(pool.size());
            res.add(pool.get(randomIdx));
            pool.remove(randomIdx);
        }

        return res;
    }
}
