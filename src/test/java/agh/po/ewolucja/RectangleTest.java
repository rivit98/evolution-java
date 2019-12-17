package agh.po.ewolucja;
import agh.po.ewolucja.Classes.Rectangle;
import agh.po.ewolucja.Classes.Vector2d;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RectangleTest {
    Rectangle r;
    Rectangle r2;

    @BeforeEach
    public void prepare(){
        r = new Rectangle(new Vector2d(0,0), new Vector2d(4,3));
        r2 = new Rectangle(new Vector2d(0,0), new Vector2d(4,3));
    }

    @Test
    public void isInsideTest(){
        assertTrue(r.isInside(new Vector2d(0,0)));
        assertFalse(r.isInside(new Vector2d(4,3)));
        assertTrue(r.isInside(new Vector2d(1,1)));
        assertFalse(r.isInside(new Vector2d(1,12)));
    }

    @Test
    public void areaTest(){
        assertEquals(12, r.area().intValue());
    }

    @Test
    public void equalsTest(){
        assertEquals(r, r2);
    }
}
