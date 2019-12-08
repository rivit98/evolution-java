package agh.po.ewolucja;

import java.util.*;
import java.util.stream.Collectors;

public class JungleMap extends AbstractWorldMap{
    public static final Integer GRASS_STEP_PERCENT = 5;
    public static final Integer GRASS_JUNGLE_PERCENT = 60;
    private final PointsGenerator pointsGenerator;
    private final HashMap<Vector2d, Grass> grassList = new LinkedHashMap<>();
    private final Rectangle mapSize;
    private final Rectangle jungleSize;

    public JungleMap(Vector2d mpsze, Vector2d jungleSize){
        this.mapSize = new Rectangle(new Vector2d(0,0), mpsze);

        Vector2d jungleLowerLeftCorner = new Vector2d(0,0);
        jungleLowerLeftCorner.x = (mpsze.x-jungleSize.x)/2;
        jungleLowerLeftCorner.y = (mpsze.y-jungleSize.y)/2;

        this.jungleSize = new Rectangle(jungleLowerLeftCorner, jungleLowerLeftCorner.add(jungleSize));
        Harness.getInstance().sendMessage(NetOptions.CREATEWORLD, this);

        pointsGenerator = new PointsGenerator(this);
        this.prepareArea(this.jungleSize, null, GRASS_JUNGLE_PERCENT);
        this.prepareArea(this.mapSize, this.jungleSize, GRASS_STEP_PERCENT);
    }

    private void prepareArea(Rectangle area, Rectangle exclude, Integer percent){
        pointsGenerator.getRandomPoints(area, exclude, percent)
                .forEach(v -> {
                    Grass g = new Grass(v);
                    grassList.put(v, g);
                    Harness.getInstance().sendMessage(NetOptions.PUTGRASS, g);
                });
    }

    public void spawnGrassInArea(Rectangle area, Rectangle exclude, Integer howMany){
        List<Vector2d> pool = pointsGenerator.getPool(area, exclude);
        Collections.shuffle(pool);

        while(howMany-- > 0 && pool.size() > 0){
            Vector2d v = pool.get(0);
            Grass g = new Grass(v);
            grassList.put(v, g);
            Harness.getInstance().sendMessage(NetOptions.PUTGRASS, g);
            pool.remove(0);
        }
    }

    public int getGrassNum() {
        return grassList.size();
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
        animalMap.values().stream().filter(a -> a.getEnergy() <= 0).forEach(a -> {
            Harness.getInstance().sendMessage(NetOptions.DELANIMAL, a);
        });
        animalMap.values().removeIf(a -> a.getEnergy() <= 0);
//        animals.removeIf(a -> a.getEnergy() <= 0); //funny bug was here

        LinkedList<Animal> animals = new LinkedList<>(animalMap.values());
        for(Animal a : animals){
            a.chooseOrientation();

            Vector2d newPos = a.movePre();
            newPos = this.translatePosition(newPos);
            a.moveTo(newPos);

            LinkedList<Animal> list = new LinkedList<>(animalMap.get(newPos));
            Grass g = grassAt(newPos);
            if(g != null){
                if(list.size() > 1){
                    Animal strongest = list.stream().max(Comparator.comparing(Animal::getEnergy)).orElseThrow(NoSuchElementException::new);
                    List<Animal> strongestList = list.stream().filter(anim -> anim.getEnergy().equals(strongest.getEnergy())).collect(Collectors.toList());

                    if(strongestList.size() > 1){
                        int toEat = (int) Math.ceil(g.getEnergyValue()/(double)strongestList.size());
                        for(Animal anim : strongestList){
                            anim.eat(new Grass(new Vector2d(0,0), toEat));
                        }
                    }else{
                        a.eat(g);
                    }
                }else{
                    a.eat(g);
                }
                removeGrass(g);
            }

            if(list.size() > 1){
                list.sort(Comparator.comparingInt(Animal::getEnergy));
                Animal a1 = list.get(0);
                Animal a2 = list.get(1);

                Optional<Animal> a3 = Optional.ofNullable(a1.merge(a2));
                a3.ifPresent(this::place);
            }
        }

        grassList.values().forEach(Grass::grow);
        spawnGrassInArea(this.mapSize, this.jungleSize, 2);
        spawnGrassInArea(this.jungleSize, null, 1);
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

    public void removeGrass(Grass g){
        grassList.remove(g.getPosition());
    }

    @Override
    public String toString(){
        return "Ilosc zwierzat: " + animalMap.size();
        //return super.toString() + "\nIlosc zwierzat: " + animalMap.size() + "\n\n";
    }
}
