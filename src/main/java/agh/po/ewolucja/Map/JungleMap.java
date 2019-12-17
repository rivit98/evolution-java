package agh.po.ewolucja.Map;

import agh.po.ewolucja.Classes.*;
import agh.po.ewolucja.Config.Config;
import agh.po.ewolucja.Config.ConfigParser;
import agh.po.ewolucja.Interfaces.IPositionChangeListener;
import agh.po.ewolucja.Interfaces.IWorldMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class JungleMap implements IWorldMap, IPositionChangeListener {
    public static final Integer GRASS_STEP_PERCENT = 10;
    public static final Integer GRASS_JUNGLE_PERCENT = 60;
    public final HashMap<Vector2d, Grass> grassList = new LinkedHashMap<>();
    private final Rectangle mapSize;
    private final Rectangle jungleSize;
    private final PointsGenerator pointsGenerator;
    private final List<Animal> deadAnimals = new ArrayList<>();
    public final Multimap<Vector2d, Animal> animalMap = LinkedListMultimap.create();
    private final HashMap<Genotype, Integer> animalGenes = new HashMap<>();

    protected Integer day = 0;
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

    @Override
    public boolean place(Animal a) {
        if (this.isOccupied(a.getPosition())) {
            return false;
        }

        animalMap.put(a.getPosition(), a);
        addGenotypeToStats(a);
        a.addObserver(this);
        return true;
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

    public Rectangle getMapCorners() {
        return this.mapSize;
    }

    private void cleanDeadAnimals(){
        animalMap.values().stream().filter(a -> a.getEnergy() <= 0.0).forEach(a -> {
            removeGenotypeFromStats(a);
            deadAnimals.add(a);
        });
        animalMap.values().removeIf(a -> a.getEnergy() <= 0.0);
    }

    private void moveAnimals(List<Animal> animals){
        animals.forEach(a -> {
            Vector2d newPos = a.movePre();
            newPos = this.translatePosition(newPos);
            a.moveTo(newPos);
        });
    }

    private void eatGrass(List<Animal> animals){
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
    }

    private void reproducingAnimals(List<Animal> animals){
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
    }

    @Override
    public void run() {
        //clean dead animals
        cleanDeadAnimals();

        //choose orientation
        animalMap.values().forEach(Animal::chooseOrientation);

        LinkedList<Animal> animals = new LinkedList<>(animalMap.values());

        //move
        moveAnimals(animals);

        //eat
        eatGrass(animals);

        //reproduce
        reproducingAnimals(animals);

        animalMap.values().forEach(Animal::incAge);
        grassList.values().forEach(Grass::grow);
        spawnGrassInArea(this.mapSize, this.jungleSize, 2);
        spawnGrassInArea(this.jungleSize, null, 1);
        day++;
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object o = animalMap.get(position);
        if(o != null){
            LinkedList l = new LinkedList<>((Collection) o);
            if(l.size() > 0) {
                return l;
            }
        }

        return grassAt(position);
    }

    public Animal getStrongestAtPosition(Vector2d pos){
        Object o = objectAt(pos);
        if(o == null){
            return null;
        }

        if(o instanceof LinkedList<?>){
            List<Animal> anims = new LinkedList<Animal>((Collection) o);
            return anims.stream().max(Comparator.comparing(AbstractMapElement::getEnergy)).get();
        }

        return null;
    }

    public Grass grassAt(Vector2d position){
        return grassList.get(position);
    }

    public Integer getGrassNum() {
        return grassList.size();
    }

    public void removeGrass(Grass g){
        grassList.remove(g.getPosition());
    }

    public Integer getDay(){
        return day;
    }

    public Integer getAvgEnergy(){
        return (int) Math.round(animalMap.values().stream().mapToDouble(AbstractMapElement::getEnergy).average().orElse(0.0));
    }

    public Integer getAvgLifeTime(){
        return (int) deadAnimals.stream().mapToDouble(Animal::getAge).average().orElse(0.0);
    }

    public Double getAvgKidsNum(){
        return Math.round(animalMap.values().stream().mapToInt(Animal::getKids).average().orElse(0.0) * 100) / 100.0;
    }

    public Integer getAnimalsNum(){
        return animalMap.size();
    }

    public Integer getDeadAnimalsNum(){
        return deadAnimals.size();
    }

    public void addGenotypeToStats(Animal a){
        animalGenes.put(a.getGenotype(), animalGenes.getOrDefault(a.getGenotype(), 0) + 1);
    }

    public void removeGenotypeFromStats(Animal a){
        animalGenes.put(a.getGenotype(), animalGenes.getOrDefault(a.getGenotype(), 1) - 1);
    }

    public Genotype mostPopularGenotype(){
        if(animalGenes.isEmpty()){
            return new Genotype().defaultGenotype();
        }

        Map.Entry<Genotype, Integer> maxEntry = null;
        for (Map.Entry<Genotype, Integer> entry : animalGenes.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                maxEntry = entry;
        }

        return maxEntry.getKey();
    }

    public List<Animal> getDominators(){
        Genotype best = mostPopularGenotype();
        return animalMap.values().stream()
                .filter(a -> a.getGenotype().equals(best))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Animal a){
        this.animalMap.remove(oldPosition, a);
        this.animalMap.put(a.getPosition(), a);
    }
}
