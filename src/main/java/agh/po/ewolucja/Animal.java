package agh.po.ewolucja;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class Animal {
    public static Integer INITIAL_ENERGY = 50;
    public static Integer COST_PER_MOVE = 1;
    private static Integer animalCnt = 0;
    private final IWorldMap map;

    private Integer age;
    private Integer animalId;
    private Genotype genotype;
    private Integer energy = INITIAL_ENERGY;
    private Vector2d position = new Vector2d(0,0);
    private List<IPositionChangeObserver> observers = new LinkedList<>();
    private MapDirection orientation;

    public Animal(IWorldMap map){
        this.map = map;
        this.orientation = MapDirection.getRandomDirection();
        this.genotype = new Genotype();
        this.age = 0;
        animalId = animalCnt++;
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this(map);
        this.position = initialPosition;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Integer energy){
        this(map, initialPosition);
        this.energy = energy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Integer energy, Genotype g){
        this(map, initialPosition, energy);
        this.genotype = g;
    }

    public String toString(){
        return this.orientation.toString();
    }

    public Integer getAnimalId(){
        return this.animalId;
    }

    public void chooseOrientation(){
        MapDirection dir = MapDirection.getDirection(this.genotype.getRandom());
        this.orientation = dir;
    }

    public Vector2d movePre(){
        Vector2d unit = this.orientation.toUnitVector();
        Vector2d newPosition = this.position.add(unit);

        return newPosition;
    }

    public void moveTo(Vector2d newPosition){
        Vector2d old = new Vector2d(this.position);
        this.position = newPosition;
        this.notifyObservers(old);
        this.addEnergy(-COST_PER_MOVE);
    }

    public void eat(Grass g){
        this.addEnergy(g.getEnergyValue());
        Harness.getInstance().sendMessage(NetOptions.DELGRASS, g);
    }

    public Integer getEnergy(){
        return this.energy;
    }

    public void addEnergy(Integer e){
        this.energy += e;
        if(this.energy < 0){
            this.energy = 0;
        }
    }

    public void addAge(Integer a){
        this.age += a;
    }

    public Integer getAge(){
        return this.age;
    }

    public Vector2d getPosition() {
        return position;
    }

    public Genotype getGenotype(){
        return genotype;
    }

    private static Integer getRequiredEnergyToReproduce(){
        return INITIAL_ENERGY/2;
    }

    public Animal merge(Animal a2){
        Animal a1 = this;

        if(a1.getEnergy() < Animal.getRequiredEnergyToReproduce() || a2.getEnergy() < Animal.getRequiredEnergyToReproduce()){
            return null;
        }

        Optional<Vector2d> calcPos = Optional.ofNullable(a1.map.getFreeSpot(a1.getPosition()));
        if(calcPos.isEmpty()){
            return null;
        }

        Integer newEnergy = a1.getEnergy()/4;
        a1.addEnergy(-newEnergy);
        Integer a2energy = a2.getEnergy()/4;
        newEnergy += a2energy;
        a2.addEnergy(-a2energy);

        Animal newAnimal = new Animal(a1.map, calcPos.get(), newEnergy, a1.genotype.merge(a2.genotype));

        return newAnimal;
    }

    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    private void notifyObservers(Vector2d oldPosition){
        this.observers.forEach(v -> v.positionChanged(oldPosition, this));
    }
}
