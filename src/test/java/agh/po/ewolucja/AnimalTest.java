package agh.po.ewolucja;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    private JungleMap map;
    private Animal a;
    private Animal a2;

    @BeforeEach
    public void prepare(){
        map = new JungleMap(new Vector2d(20,20), new Vector2d(3,3));
        a = new Animal(map, new Vector2d(10, 10), 100.0);
        a2 = new Animal(map, new Vector2d(0,0), 100.0);
        map.place(a);
        map.place(a2);
    }

    @Test
    public void moveToTest(){
        Double en = a.getEnergy();
        Vector2d free = map.getFreeSpot(a.getPosition());
        a.moveTo(free);
        assertEquals(a.getPosition(), free);
        assertEquals(a.getEnergy().intValue(), en-1);
    }

    @Test
    public void eatTest(){
        Double e = a.getEnergy();
        Vector2d free = map.getFreeSpot(a.getPosition());
        Grass g = new Grass(free, 100.0);
        a.eat(g);
        assertEquals(a.getEnergy().intValue(), e+100);
    }

    @Test
    public void addEnergyTest(){
        Double e = a.getEnergy();
        a.addEnergy(123.0);
        assertEquals(a.getEnergy().intValue(), e + 123);
    }

    @Test
    public void mergeTest(){
        Animal a3 = a.merge(a2);
        assertEquals(a.getEnergy().intValue(), 75);
        assertEquals(a2.getEnergy().intValue(), 75);
        assertEquals(a3.getEnergy().intValue(), 50);
        assertTrue(a3.getGenotype().verify());
    }
}
