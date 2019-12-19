package agh.po.ewolucja;

import agh.po.ewolucja.Classes.Animal;
import agh.po.ewolucja.Classes.Grass;
import agh.po.ewolucja.Classes.Vector2d;
import agh.po.ewolucja.Map.JungleMap;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    private JungleMap map;
    private Animal a1;
    private Animal a2;

    @BeforeEach
    public void prepare(){
        map = new JungleMap(new Vector2d(20,20), new Vector2d(3,3));
        a1 = new Animal(map, new Vector2d(10, 10), 100.0);
        a2 = new Animal(map, new Vector2d(0,0), 100.0);
        map.place(a1);
        map.place(a2);
    }

    @Test
    public void moveToTest(){
        Double en = a1.getEnergy();
        Vector2d free = map.getFreeSpot(a1.getPosition());
        a1.moveTo(free);
        assertEquals(a1.getPosition(), free);
        assertEquals(a1.getEnergy().intValue(), en-1);
    }

    @Test
    public void eatTest(){
        Double e = a1.getEnergy();
        Vector2d free = map.getFreeSpot(a1.getPosition());
        Grass g = new Grass(free, 100.0);
        a1.eat(g);
        assertEquals(a1.getEnergy().intValue(), e+100);
    }

    @Test
    public void addEnergyTest(){
        Double e = a1.getEnergy();
        a1.addEnergy(123.0);
        assertEquals(a1.getEnergy().intValue(), e + 123);
    }

    @Test
    public void mergeTest(){
        Animal a3 = a1.merge(a2);
        assertEquals(a1.getEnergy().intValue(), 75);
        assertEquals(a2.getEnergy().intValue(), 75);
        assertEquals(a3.getEnergy().intValue(), 50);
        assertTrue(a3.getGenotype().verify());
    }

    @Test
    public void descendantCountTest(){
        Animal a3 = new Animal(map, new Vector2d(1,1));
        Animal a4 = new Animal(map, new Vector2d(1,3));
        a1.addEnergy(1000.0);
        a2.addEnergy(1000.0);
        a3.addEnergy(1000.0);
        a4.addEnergy(1000.0);

        Animal a12 = a1.merge(a2);
        assertEquals(1, a1.getDescendants());
        assertEquals(1, a2.getDescendants());
        assertEquals(0, a3.getDescendants());
        assertEquals(0, a12.getDescendants());

        Animal a12_3 = a12.merge(a3);
        assertEquals(2, a1.getDescendants());
        assertEquals(2, a2.getDescendants());
        assertEquals(1, a3.getDescendants());
        assertEquals(1, a12.getDescendants());
        assertEquals(0, a12_3.getDescendants());

        Animal finalA = a12_3.merge(a4);
        assertEquals(0, finalA.getDescendants());
        assertEquals(1, a12_3.getDescendants());
        assertEquals(3, a1.getDescendants());

    }
}
