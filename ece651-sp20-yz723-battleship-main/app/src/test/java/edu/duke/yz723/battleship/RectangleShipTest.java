package edu.duke.yz723.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static edu.duke.yz723.battleship.RectangleShip.makeCoords;
import static org.junit.jupiter.api.Assertions.*;

class RectangleShipTest {

    @Test
    public void test_makeCoords() {
        Coordinate upperLeft = new Coordinate(3, 4);
        int width = 2;
        int height = 3;
        HashSet<Coordinate> expected = new HashSet<Coordinate>();
        expected.add(new Coordinate(3, 4));
        expected.add(new Coordinate(3, 5));
        expected.add(new Coordinate(4, 4));
        expected.add(new Coordinate(4, 5));
        expected.add(new Coordinate(5, 4));
        expected.add(new Coordinate(5, 5));
        HashSet<Coordinate> ans = makeCoords(new Coordinate(3,4), 2, 3);
        assertEquals(expected, ans);
    }
    @Test
    public void test_makeRectangleShip(){
        Coordinate upperLeft = new Coordinate(3, 4);
        RectangleShip<Character> recShip = new RectangleShip<Character>("testship",upperLeft,2,3,'s', '*');
        //test getName()
        assertEquals("testship", recShip.getName());
        HashMap<Coordinate, Boolean> expected = new HashMap<Coordinate, Boolean>();
        expected.put(new Coordinate(3, 4), false);
        expected.put(new Coordinate(3, 5), false);
        expected.put(new Coordinate(4, 4), false);
        expected.put(new Coordinate(4, 5), false);
        expected.put(new Coordinate(5, 4), false);
        expected.put(new Coordinate(5, 5), false);
        assertEquals(expected, recShip.myPieces);
    }
    @Test
    public void test_wasHitAt_and_recordHit(){
        RectangleShip<Character> ship1 = new RectangleShip<Character>(new Coordinate(1,2),'c','*');
        assertThrows(IllegalArgumentException.class, ()->ship1.checkCoordinateInThisShip(new Coordinate(2,2)));
        assertFalse(ship1.wasHitAt(new Coordinate(1,2)));
        ship1.recordHitAt(new Coordinate(1,2));
        assertTrue(ship1.wasHitAt(new Coordinate(1,2)));
    }
    @Test
    public void test_isSunk(){
        RectangleShip<Character> ship1 = new RectangleShip<Character>("testship",new Coordinate(1,1),2,1,'c','*');
        assertFalse(ship1.isSunk());
        ship1.recordHitAt(new Coordinate(1,1));
        assertFalse(ship1.isSunk());
        ship1.recordHitAt(new Coordinate(1,2));
        assertTrue(ship1.isSunk());
    }
    @Test
    public void test_getDisplayInfoAt_selfShip(){
        RectangleShip<Character> ship1 = new RectangleShip<Character>("testship", new Coordinate(1,1),2,1,'c','*');
        assertEquals('c',ship1.getDisplayInfoAt(new Coordinate(1,1), true));
        assertEquals('c',ship1.getDisplayInfoAt(new Coordinate(1,2), true));
        ship1.recordHitAt(new Coordinate(1,2));
        assertEquals('*',ship1.getDisplayInfoAt(new Coordinate(1,2),true));
    }

    @Test
    public void test_get_allCoordinates(){
        V1ShipFactory shipFactory = new V1ShipFactory();
        Ship<Character> destroyer = shipFactory.makeDestroyer(new Placement(new Coordinate(1,1), 'V'));
        Set<Coordinate> expected = new HashSet<Coordinate>();
        expected.add(new Coordinate(1,1));
        expected.add(new Coordinate(2,1));
        expected.add(new Coordinate(3,1));
        assertEquals(expected, destroyer.getCoordinates());
    }

    @Test
    public void test_getDisplayInfoAt_enemyShip(){
        SimpleShipDisplayInfo<Character> myDisplayInfo = new SimpleShipDisplayInfo<Character>('c','*');
        SimpleShipDisplayInfo<Character> enemyDisplayInfo = new SimpleShipDisplayInfo<Character>(' ','c');
        RectangleShip<Character> ship1 = new RectangleShip<Character>("testship", new Coordinate(1,1),2,1,myDisplayInfo,enemyDisplayInfo);
        assertEquals(' ',ship1.getDisplayInfoAt(new Coordinate(1,1), false));
        assertEquals(' ',ship1.getDisplayInfoAt(new Coordinate(1,2), false));
        ship1.recordHitAt(new Coordinate(1,2));
        assertEquals('c',ship1.getDisplayInfoAt(new Coordinate(1,2),false));
    }
    @Test
    void test_getUpperLeft(){
        SimpleShipDisplayInfo<Character> myDisplayInfo = new SimpleShipDisplayInfo<Character>('c','*');
        SimpleShipDisplayInfo<Character> enemyDisplayInfo = new SimpleShipDisplayInfo<Character>(' ','c');
        RectangleShip<Character> ship = new RectangleShip<Character>("testship", new Coordinate(1,1),2,1,myDisplayInfo,enemyDisplayInfo);
        assertEquals(new Coordinate(1,1), ship.getUpperLeft());
    }
    @Test
    void test_getAllHitCoords(){
        SimpleShipDisplayInfo<Character> myDisplayInfo = new SimpleShipDisplayInfo<Character>('c','*');
        SimpleShipDisplayInfo<Character> enemyDisplayInfo = new SimpleShipDisplayInfo<Character>(' ','c');
        RectangleShip<Character> ship = new RectangleShip<Character>("testship", new Coordinate(1,1),2,1,myDisplayInfo,enemyDisplayInfo);
        HashSet<Coordinate> expected = new HashSet<>();
        assertEquals(expected, ship.getAllHitCoords());
        ship.recordHitAt(new Coordinate(1,2));
        expected.add(new Coordinate(1,2));
        assertEquals(expected, ship.getAllHitCoords());
        ship.recordHitAt(new Coordinate(1,1));
        expected.add(new Coordinate(1,1));
        assertEquals(expected, ship.getAllHitCoords());
    }
    @Test
    void test_getCenter(){
        SimpleShipDisplayInfo<Character> myDisplayInfo = new SimpleShipDisplayInfo<Character>('c','*');
        SimpleShipDisplayInfo<Character> enemyDisplayInfo = new SimpleShipDisplayInfo<Character>(' ','c');
        RectangleShip<Character> ship = new RectangleShip<Character>("testship", new Coordinate(1,1),2,1,myDisplayInfo,enemyDisplayInfo);
        assertEquals(ship.getCenter(),ship.getUpperLeft());
    }
    @Test
    void test_relativeCoords(){
        RectangleShip<Character> ship = new RectangleShip<Character>("testship", new Coordinate(2,1),2,3,'c','*');
        Coordinate[] expected = new Coordinate[6];
        expected[0] = new Coordinate(2,1);
        expected[1] = new Coordinate(2,2);
        expected[2] = new Coordinate(3,1);
        expected[3] = new Coordinate(3,2);
        expected[4] = new Coordinate(4,1);
        expected[5] = new Coordinate(4,2);
        Coordinate[] actual = ship.getRelative();
        for (int i = 0; i < 6;i++){
            assertEquals(expected[i], actual[i]);
        }
    }
    @Test
    void test_relative_coords_submarine(){
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> sub1 = sf.makeSubmarine(new Placement("a0v"));
        Coordinate[] expected = new Coordinate[2];
        expected[0] = new Coordinate(0,0);
        expected[1] = new Coordinate(1,0);
        assertEquals(expected[0], sub1.getRelative()[0]);
        assertEquals(expected[1], sub1.getRelative()[1]);
    }

    @Test
    void check_checkShipHit(){
        SimpleShipDisplayInfo<Character> myDisplayInfo = new SimpleShipDisplayInfo<Character>('c','*');
        SimpleShipDisplayInfo<Character> enemyDisplayInfo = new SimpleShipDisplayInfo<Character>(' ','c');
        RectangleShip<Character> ship = new RectangleShip<Character>("testship", new Coordinate(1,1),2,1,myDisplayInfo,enemyDisplayInfo);
        assertFalse(ship.checkShipHit());
        ship.recordHitAt(new Coordinate(1,2));
        assertTrue(ship.checkShipHit());
    }

}