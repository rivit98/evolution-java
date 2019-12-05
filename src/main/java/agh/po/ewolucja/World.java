package agh.po.ewolucja;

import java.util.Collections;
import java.util.List;

public class World {
    public static void main(String[] args){
        Config c;
        Integer howManyAnimals, iterLimit;

        if(args.length == 3){
            howManyAnimals = Integer.parseInt(args[1]);
            iterLimit = Integer.parseInt(args[0]);
            c = new Config(args[2]);
        }else{
            c = new Config();
            howManyAnimals = 300;
            iterLimit = 10000;
        }
        //TODO: move this to Config or sth like this
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
                if((cnt % 3) == 0){
                    Harness.getInstance().startSending();
                }
                map.run();
                Harness.getInstance().stopSending();

                //cnt++;
            }
        }catch (IllegalArgumentException | IllegalStateException ex){
            System.out.println(ex.getMessage());
            System.exit(666);
        }
    }
}

