package agh.po.ewolucja;

import java.util.Arrays;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTest {
    Genotype g1;
    Genotype g2;
    Genotype g3;

    @BeforeEach
    public void prepare(){
        g1 = new Genotype();
        g2 = new Genotype();
        g3 = new Genotype();
    }

    @Test
    public void createTest(){
        assertTrue(g1.verify());
        assertTrue(g2.verify());
        assertTrue(g3.verify());
        Integer[] asdf = new Integer[Genotype.GENOTYPE_SIZE];
        Arrays.fill(asdf, 1);
        Genotype badGeno = new Genotype(asdf);
        assertFalse(badGeno.verify());
        badGeno.fix();
        assertTrue(badGeno.verify());
    }

    @Test
    public void merge(){
        int asdf = 300;
        while(asdf-- > 0){
            Genotype random = new Genotype();
            assertTrue(random.merge(g2).verify());
            assertTrue(random.merge(g1).verify());
            assertTrue(random.merge(g3).verify());
        }
    }
}
