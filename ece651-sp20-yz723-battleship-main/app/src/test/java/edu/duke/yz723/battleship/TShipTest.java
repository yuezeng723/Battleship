package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TShipTest {
    @Test
    void test_makeCoords_up(){
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'u', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate c1 = new Coordinate(0,1);
        Coordinate c2 = new Coordinate(1,0);
        Coordinate c3 = new Coordinate(1,1);
        Coordinate c4 = new Coordinate(1,2);
        expected.add(c1);
        expected.add(c2);
        expected.add(c3);
        expected.add(c4);
        assertEquals(expected, battleship.getCoordinates());
        assertEquals(new Coordinate(0,0), battleship.getUpperLeft());
    }

    @Test
    void test_makeCoords_right(){
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'r', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(1,0);
        Coordinate c3 = new Coordinate(1,1);
        Coordinate c4 = new Coordinate(2,0);
        expected.add(c1);
        expected.add(c2);
        expected.add(c3);
        expected.add(c4);
        assertEquals(expected, battleship.getCoordinates());
    }

    @Test
    void test_makeCoords_down(){
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'d', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(0,1);
        Coordinate c3 = new Coordinate(0,2);
        Coordinate c4 = new Coordinate(1,1);
        expected.add(c1);
        expected.add(c2);
        expected.add(c3);
        expected.add(c4);
        assertEquals(expected, battleship.getCoordinates());
    }

    @Test
    void test_makeCoords_left(){
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'l', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate c1 = new Coordinate(0,1);
        Coordinate c2 = new Coordinate(1,0);
        Coordinate c3 = new Coordinate(1,1);
        Coordinate c4 = new Coordinate(2,1);
        expected.add(c1);
        expected.add(c2);
        expected.add(c3);
        expected.add(c4);
        assertEquals(expected, battleship.getCoordinates());
        assertEquals(new Coordinate(0,0), battleship.getUpperLeft());
    }

    @Test
    void test_getName(){
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'l', 'b', '*');
        assertEquals("Battleship", battleship.getName());
    }

    @Test
    void test_invalid_orientation_input(){
        assertThrows(IllegalArgumentException.class, ()->new TShip<Character>(new Coordinate(0,0),'H', 'b', '*'));
        assertThrows(IllegalArgumentException.class, ()->new TShip<Character>(new Coordinate(0,0),'V', 'b', '*'));
    }
    @Test
    void test_getCenter(){
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'u', 'b', '*');
        assertEquals(new Coordinate(1,1), battleship.getCenter());
        TShip<Character> battleship1 = new TShip<Character>(new Coordinate(0,0),'r', 'b', '*');
        assertEquals(new Coordinate(1,0), battleship1.getCenter());
        TShip<Character> battleship2 = new TShip<Character>(new Coordinate(0,0),'d', 'b', '*');
        assertEquals(new Coordinate(0,1), battleship2.getCenter());
        TShip<Character> battleship3 = new TShip<Character>(new Coordinate(0,0),'l', 'b', '*');
        assertEquals(new Coordinate(1,1), battleship3.getCenter());
    }
    @Test
    void test_getRelatives_Up(){
        Coordinate[] expected = new Coordinate[4];
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'u', 'b', '*');
        expected[0] = new Coordinate(1,1);
        expected[1] = new Coordinate(1,0);
        expected[2] = new Coordinate(0,1);
        expected[3] = new Coordinate(1,2);
        for (int i = 0; i < 4 ;i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }
    @Test
    void test_getRelatives_right(){
        Coordinate[] expected = new Coordinate[4];
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'r', 'b', '*');
        expected[0] = new Coordinate(1,0);
        expected[1] = new Coordinate(0,0);
        expected[2] = new Coordinate(1,1);
        expected[3] = new Coordinate(2,0);
        for (int i = 0; i < 4 ; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }
    @Test
    void test_getRelatives_down(){
        Coordinate[] expected = new Coordinate[4];
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'d', 'b', '*');
        expected[0] = new Coordinate(0,1);
        expected[1] = new Coordinate(0,2);
        expected[2] = new Coordinate(1,1);
        expected[3] = new Coordinate(0,0);
        for (int i = 0; i < 4 ; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }
    @Test
    void test_getRelatives_left(){
        Coordinate[] expected = new Coordinate[4];
        TShip<Character> battleship = new TShip<Character>(new Coordinate(0,0),'l', 'b', '*');
        expected[0] = new Coordinate(1,1);
        expected[1] = new Coordinate(2,1);
        expected[2] = new Coordinate(1,0);
        expected[3] = new Coordinate(0,1);
        for (int i = 0; i < 4 ; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }


}