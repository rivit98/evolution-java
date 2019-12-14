package agh.po.ewolucja;

import agh.po.ewolucja.Interfaces.IWorldMap;

import java.util.Optional;
import java.util.Random;

public class Animal extends AbstractMapElement {
    public static Double INITIAL_ENERGY = 50.0;
    public static Double COST_PER_MOVE = 1.0;
    private final IWorldMap map;
    private static final Random rand = new Random();

    private final Integer animalID;
    private Integer age = 0;
    private Integer kids = 0;
    private Genotype genotype;
    private MapDirection orientation;

    public Animal(IWorldMap map){
        super(); //is it necessary?
        this.energy = INITIAL_ENERGY;
        this.map = map;
        this.orientation = MapDirection.getRandomDirection();
        this.genotype = new Genotype();
        this.animalID = rand.nextInt(10000);
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this(map);
        this.position = initialPosition;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Double energy){
        this(map, initialPosition);
        this.energy = energy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Double energy, Genotype g){
        this(map, initialPosition, energy);
        this.genotype = g;
    }

    public Integer getAge(){
        return age;
    }

    public void incAge(){
        age++;
    }

    public Integer getKids(){
        return kids;
    }

    public void incKids(){
        kids++;
    }

    public Integer getAnimalID(){
        return animalID;
    }

    public String toString(){
        return this.orientation.toString();
    }
    
    public void chooseOrientation(){
        this.orientation = MapDirection.getDirection(this.genotype.getRandom());
    }

    public Vector2d movePre(){
        Vector2d newPosition = position.add(orientation.toUnitVector());
        return newPosition;
    }

    public void moveTo(Vector2d newPosition){
        Vector2d old = new Vector2d(this.position);
        this.position = newPosition;
        this.notifyObservers(old, this);
        this.addEnergy(-COST_PER_MOVE);
    }

    public void eat(Grass g){
        this.addEnergy(g.getEnergy());
    }

    public Genotype getGenotype(){
        return genotype;
    }

    private static Double getRequiredEnergyToReproduce(){
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

        Double newEnergy = a1.getEnergy()/4;
        a1.addEnergy(-newEnergy);
        Double a2energy = a2.getEnergy()/4;
        newEnergy += a2energy;
        a2.addEnergy(-a2energy);

        a1.incKids();
        a2.incKids();

        Animal newAnimal = new Animal(a1.map, calcPos.get(), newEnergy, a1.genotype.merge(a2.genotype));

        return newAnimal;
    }
}
