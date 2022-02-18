package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ZShipTest {
    @Test
    void test_getCoords_up(){
        ZShip<Character> carrier = new ZShip<Character>(new Coordinate(0,0),'u', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate[] c = new Coordinate[7];
        c[0] =new Coordinate(0,0);
        c[1] =new Coordinate(1,0);
        c[2] =new Coordinate(2,0);
        c[3] =new Coordinate(2,1);
        c[4] =new Coordinate(3,0);
        c[5] =new Coordinate(3,1);
        c[6] =new Coordinate(4,1);
        for (int i = 0; i < c.length; i++){
            expected.add(c[i]);
        }
        assertEquals(expected, carrier.getCoordinates());
        assertEquals(new Coordinate(0,0), carrier.getUpperLeft());
    }
    @Test
    void test_getCoords_right(){
        ZShip<Character> carrier = new ZShip<Character>(new Coordinate(0,0),'r', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate[] c = new Coordinate[7];
        c[0] =new Coordinate(0,1);
        c[1] =new Coordinate(0,2);
        c[2] =new Coordinate(0,3);
        c[3] =new Coordinate(0,4);
        c[4] =new Coordinate(1,0);
        c[5] =new Coordinate(1,1);
        c[6] =new Coordinate(1,2);
        for (int i = 0; i < c.length; i++){
            expected.add(c[i]);
        }
        assertEquals(expected, carrier.getCoordinates());
    }
    @Test
    void test_getCoords_down(){
        ZShip<Character> carrier = new ZShip<Character>(new Coordinate(0,0),'d', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate[] c = new Coordinate[7];
        c[0] =new Coordinate(0,0);
        c[1] =new Coordinate(1,0);
        c[2] =new Coordinate(1,1);
        c[3] =new Coordinate(2,0);
        c[4] =new Coordinate(2,1);
        c[5] =new Coordinate(3,1);
        c[6] =new Coordinate(4,1);
        for (int i = 0; i < c.length; i++){
            expected.add(c[i]);
        }
        assertEquals(expected, carrier.getCoordinates());
    }
    @Test
    void test_getCoords_left(){
        ZShip<Character> carrier = new ZShip<Character>(new Coordinate(0,0),'l', 'b', '*');
        HashSet<Coordinate> expected = new HashSet<>();
        Coordinate[] c = new Coordinate[7];
        c[0] =new Coordinate(0,2);
        c[1] =new Coordinate(0,3);
        c[2] =new Coordinate(0,4);
        c[3] =new Coordinate(1,0);
        c[4] =new Coordinate(1,1);
        c[5] =new Coordinate(1,2);
        c[6] =new Coordinate(1,3);
        for (int i = 0; i < c.length; i++){
            expected.add(c[i]);
        }
        assertEquals(expected, carrier.getCoordinates());
    }
    @Test
    void test_getName(){
        ZShip<Character> carrier = new ZShip<Character>(new Coordinate(0,0),'l', 'c', '*');
        assertEquals("Carrier", carrier.getName());
    }

    @Test
    void test_invalid_orientation_input(){
        assertThrows(IllegalArgumentException.class, ()->new ZShip<Character>(new Coordinate(0,0),'H', 'c', '*'));
        assertThrows(IllegalArgumentException.class, ()->new ZShip<Character>(new Coordinate(0,0),'V', 'c', '*'));
    }
    @Test
    void test_getCenter(){
        ZShip<Character> carrier = new ZShip<Character>(new Coordinate(0,0),'u', 'c', '*');
        assertEquals(new Coordinate(3,0),carrier.getCenter());
        ZShip<Character> carrier1 = new ZShip<Character>(new Coordinate(0,0),'r', 'c', '*');
        assertEquals(new Coordinate(0,1),carrier1.getCenter());
        ZShip<Character> carrier2 = new ZShip<Character>(new Coordinate(0,0),'d', 'c', '*');
        assertEquals(new Coordinate(1,1),carrier2.getCenter());
        ZShip<Character> carrier3 = new ZShip<Character>(new Coordinate(0,0),'l', 'c', '*');
        assertEquals(new Coordinate(1,3),carrier3.getCenter());
    }
    @Test
    void test_getRelative_Up(){
        Coordinate[] expected = new Coordinate[7];
        ZShip<Character> battleship = new ZShip<Character>(new Coordinate(0,0),'u', 'c', '*');
        expected[0] = new Coordinate(3,0);
        expected[1] = new Coordinate(2,0);
        expected[2] = new Coordinate(1,0);
        expected[3] = new Coordinate(0,0);
        expected[4] = new Coordinate(2,1);
        expected[5] = new Coordinate(3,1);
        expected[6] = new Coordinate(4,1);
        for (int i = 0; i < 6; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }
    @Test
    void test_getRelative_Right(){
        Coordinate[] expected = new Coordinate[7];
        ZShip<Character> battleship = new ZShip<Character>(new Coordinate(0,0),'r', 'c', '*');
        expected[0] = new Coordinate(0,1);
        expected[1] = new Coordinate(0,2);
        expected[2] = new Coordinate(0,3);
        expected[3] = new Coordinate(0,4);
        expected[4] = new Coordinate(1,2);
        expected[5] = new Coordinate(1,1);
        expected[6] = new Coordinate(1,0);
        for (int i = 0; i < 6; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }
    @Test
    void test_getRelative_Down(){
        Coordinate[] expected = new Coordinate[7];
        ZShip<Character> battleship = new ZShip<Character>(new Coordinate(0,0),'d', 'c', '*');
        expected[0] = new Coordinate(1,1);
        expected[1] = new Coordinate(2,1);
        expected[2] = new Coordinate(3,1);
        expected[3] = new Coordinate(4,1);
        expected[4] = new Coordinate(2,0);
        expected[5] = new Coordinate(1,0);
        expected[6] = new Coordinate(0,0);
        for (int i = 0; i < 6; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }
    @Test
    void test_getRelative_Left(){
        Coordinate[] expected = new Coordinate[7];
        ZShip<Character> battleship = new ZShip<Character>(new Coordinate(0,0),'l', 'c', '*');
        expected[0] = new Coordinate(1,3);
        expected[1] = new Coordinate(1,2);
        expected[2] = new Coordinate(1,1);
        expected[3] = new Coordinate(1,0);
        expected[4] = new Coordinate(0,2);
        expected[5] = new Coordinate(0,3);
        expected[6] = new Coordinate(0,4);
        for (int i = 0; i < 6; i++){
            assertEquals(expected[i], battleship.getRelative()[i]);
        }
    }

}