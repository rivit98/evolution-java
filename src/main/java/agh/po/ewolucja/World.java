package agh.po.ewolucja;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class World {
    public static void main(String[] args){
        Config c;
        Integer howManyAnimals, iterLimit;
        Integer pauseTime = 200;

        Harness.getInstance().enable();

        if(args.length == 3){
            howManyAnimals = Integer.parseInt(args[1]);
            iterLimit = Integer.parseInt(args[0]);
            c = new ConfigParser().parse(args[2]);
        }else{
            c = new ConfigParser().parseDefault();
            howManyAnimals = 100;
            iterLimit = 1000;
        }

        Animal.COST_PER_MOVE = c.moveEnergy;
        Animal.INITIAL_ENERGY = c.startEnergy;
        Grass.DEFAULT_ENERGY_VALUE = c.plantStartEnergy;
        Vector2d jungleSize = new Vector2d(c.jungleWidth, c.jungleHeight);
        Vector2d mapsize = new Vector2d(c.mapWidth, c.mapHeight);

        try{
            IWorldMap map = new JungleMap(mapsize, jungleSize);
            int cnt = 0;

            List<Vector2d> points = new PointsGenerator(map).getPool(new Rectangle(new Vector2d(0,0), mapsize), null);
            Collections.shuffle(points);
            points.subList(0, howManyAnimals).forEach(v -> map.place(new Animal(map, v)));

            int iterations = 0;
            while(iterations++ < iterLimit){
                System.out.println(map);
                if((cnt % 100) == 0){
                }
                map.run();

                cnt++;
                Thread.sleep(pauseTime);
            }

            Harness.getInstance().stopConnection();
        }catch (IllegalArgumentException | IllegalStateException | InterruptedException | IOException ex){
            System.out.println(ex.getMessage());
            System.exit(666);
        }

    }
}

