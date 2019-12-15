package agh.po.ewolucja;

import java.util.*;
import java.util.stream.Collectors;

public class JungleMap extends AbstractWorldMap{
    public static final Integer GRASS_STEP_PERCENT = 5;
    public static final Integer GRASS_JUNGLE_PERCENT = 60;
    private final PointsGenerator pointsGenerator;
    public final HashMap<Vector2d, Grass> grassList = new LinkedHashMap<>();
    private final Rectangle mapSize;
    private final Rectangle jungleSize;
    public Config cfg;

    public JungleMap(Vector2d mpsze, Vector2d jungleSize, Config c){
        if(c == null){
            //this is only for test compatibility
            cfg = new ConfigParser().parseDefault();
        }else{
            cfg = c;
        }
        this.mapSize = new Rectangle(new Vector2d(0,0), mpsze);

        Vector2d jungleLowerLeftCorner = new Vector2d(0,0);
        jungleLowerLeftCorner.x = (mpsze.x-jungleSize.x)/2;
        jungleLowerLeftCorner.y = (mpsze.y-jungleSize.y)/2;

        this.jungleSize = new Rectangle(jungleLowerLeftCorner, jungleLowerLeftCorner.add(jungleSize));

        pointsGenerator = new PointsGenerator(this);
        this.prepareArea(this.jungleSize, null, GRASS_JUNGLE_PERCENT);
        this.prepareArea(this.mapSize, this.jungleSize, GRASS_STEP_PERCENT);
    }

    public JungleMap(Vector2d mpsze, Vector2d jungleSize) {
        this(mpsze, jungleSize, null);
    }

    private void prepareArea(Rectangle area, Rectangle exclude, Integer percent){
        pointsGenerator.getRandomPoints(area, exclude, percent)
                .forEach(v -> {
                    Grass g = new Grass(v, cfg.plantStartEnergy);
                    grassList.put(v, g);
                });
    }

    public void spawnGrassInArea(Rectangle area, Rectangle exclude, Integer howMany){
        List<Vector2d> pool = pointsGenerator.getPool(area, exclude);
        Collections.shuffle(pool);

        while(howMany-- > 0 && pool.size() > 0){
            Vector2d v = pool.get(0);
            Grass g = new Grass(v, cfg.plantStartEnergy);
            grassList.put(v, g);
            pool.remove(0);
        }
    }

    public Vector2d translatePosition(Vector2d po){
        Integer x = po.x;
        Integer y = po.y;
        if(x < 0){
            x = this.mapSize.upperRight.x-1;
        }
        if(y < 0){
            y = this.mapSize.upperRight.y-1;
        }

        return new Vector2d(x % this.mapSize.upperRight.x, y % this.mapSize.upperRight.y);
    }

    public Rectangle getJungleCorners() {
        return this.jungleSize;
    }

    @Override
    public Vector2d getFreeSpot(Vector2d origin) {
        List<Vector2d> free = new ArrayList<>();
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                if(x == 0 && y == 0){ //self skip
                    continue;
                }
                Vector2d newPos = new Vector2d(origin).add(new Vector2d(x, y));
                newPos = this.translatePosition(newPos);
                if(this.objectAt(newPos) == null){
                    free.add(newPos);
                }
            }
        }
        if(!free.isEmpty()){
            Collections.shuffle(free);
            return free.get(0);
        }
        return null;
    }

    @Override
    protected Rectangle getMapCorners() {
        return this.mapSize;
    }

    @Override
    public void run() {
        //clean dead animals
        animalMap.values().removeIf(a -> a.getEnergy() <= 0.0);

        //choose orientation
        animalMap.values().forEach(Animal::chooseOrientation);

        LinkedList<Animal> animals = new LinkedList<>(animalMap.values());
        //move
        //animalMap.values().forEach will cause ConcurrentModificationException because of observers and so on
        animals.forEach(a -> {
            Vector2d newPos = a.movePre();
            newPos = this.translatePosition(newPos);
            a.moveTo(newPos);
        });

        //eat
        animalMap.values().forEach(a -> {
            Vector2d newPos = a.getPosition();
            Grass g = grassAt(newPos);

            if(g == null){
                return;
            }

            LinkedList<Animal> list = new LinkedList<>(animalMap.get(newPos));

            Double strongestEnergy = list.stream().mapToDouble(Animal::getEnergy).max().orElse(a.getEnergy());
            List<Animal> strongestList = list.stream().filter(anim -> anim.getEnergy().equals(strongestEnergy)).collect(Collectors.toList());

            Double energy = g.getEnergy() / strongestList.size();
            for (Animal anim : strongestList) {
                anim.addEnergy(energy);
            }
            removeGrass(g);
        });

        //reproduce
        animals.forEach(a -> {
            Vector2d newPos = a.getPosition();
            LinkedList<Animal> list = new LinkedList<>(animalMap.get(newPos));
            int size = list.size();
            if(size < 2) {
                return;
            }
            Collections.shuffle(list);
            Optional<Animal> bbyAnimal = Optional.ofNullable(list.get(0).merge(list.get(1)));
            bbyAnimal.ifPresent(this::place);
        });

        animalMap.values().forEach(Animal::incAge);
        grassList.values().forEach(Grass::grow);
        spawnGrassInArea(this.mapSize, this.jungleSize, 1);
        spawnGrassInArea(this.jungleSize, null, 1);
        day++;
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object o = super.objectAt(position);
        if(o instanceof Collection<?>){
            LinkedList l = new LinkedList<>((Collection) o);
            if(l.size() > 0) {
                return o;
            }
        }

        return grassAt(position);
    }

    public Grass grassAt(Vector2d position){
        return grassList.get(position);
    }

    public int getGrassNum() {
        return grassList.size();
    }

    public void removeGrass(Grass g){
        grassList.remove(g.getPosition());
    }

    @Override
    public String toString(){
        //return "Ilosc zwierzat: " + animalMap.size();
        return super.toString() + "\nIlosc zwierzat: " + animalMap.size() + "\n\n";
    }
}
