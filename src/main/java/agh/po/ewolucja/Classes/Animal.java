package agh.po.ewolucja.Classes;

import agh.po.ewolucja.Map.JungleMap;
import agh.po.ewolucja.Map.MapDirection;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Animal extends AbstractMapElement {
    private final JungleMap map;

    private Integer age = 0;
    private Integer kids = 0;
    private Genotype genotype;
    private MapDirection orientation;
    private List<Animal> kidsList = new LinkedList<>();

    public Animal(JungleMap map){
        this.map = map;
        this.orientation = MapDirection.getRandomDirection();
        this.genotype = new Genotype();
    }

    public Animal(JungleMap map, Vector2d initialPosition){
        this(map);
        setPosition(initialPosition);
    }

    public Animal(JungleMap map, Vector2d initialPosition, Double energy){
        this(map, initialPosition);
        this.energy = energy;
    }

    public Animal(JungleMap map, Vector2d initialPosition, Double energy, Genotype g){
        this(map, initialPosition, energy);
        this.genotype = g;
    }

    @Override
    public Color getColor(){
        return new Color((int) Math.min(255, Math.max(0, 255-energy)),0,0);
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

    public void incKids(Animal a){
        kidsList.add(a);
        kids++;
    }

    public Integer getDescendant(){
        return kidsList.size(); //for now  TODO:change this
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
        setPosition(newPosition);
        this.notifyObservers(old, this);
        this.addEnergy(-map.cfg.moveEnergy);
    }

    public void eat(Grass g){
        this.addEnergy(g.getEnergy());
    }

    public Genotype getGenotype(){
        return genotype;
    }

    public boolean isAlive(){
        return getEnergy() > 0;
    }

    private Double getRequiredEnergyToReproduce(){
        return map.cfg.startEnergy/2;
    }

    public Animal merge(Animal a2) {
        Animal a1 = this;

        if (a1.getEnergy() < a1.getRequiredEnergyToReproduce() || a2.getEnergy() < a2.getRequiredEnergyToReproduce()) {
            return null;
        }

        Optional<Vector2d> calcPos = Optional.ofNullable(a1.map.getFreeSpot(a1.getPosition()));
        if (calcPos.isEmpty()) {
            return null;
        }

        Double newEnergy = a1.getEnergy() / 4;
        a1.addEnergy(-newEnergy);
        Double a2energy = a2.getEnergy() / 4;
        newEnergy += a2energy;
        a2.addEnergy(-a2energy);


        Animal newAnimal = new Animal(a1.map, calcPos.get(), newEnergy, a1.genotype.merge(a2.genotype));
        a1.incKids(newAnimal);
        a2.incKids(newAnimal);

        return newAnimal;
    }
}
