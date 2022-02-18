package edu.duke.yz723.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class PlacementTest {

    @Test
    void test_caseInsensitivity(){
        Coordinate c1 = new Coordinate(3,2);
        Coordinate c2 = new Coordinate(3,2);
        Placement p1 = new Placement(c1, 'v');
        Placement p2 = new Placement(c2, 'V');
        assertEquals(p1, p2);
    }

    @Test
    void test_getCoordinate() {
        Coordinate c1 = new Coordinate(3,2);
        Coordinate c2 = new Coordinate(3,2);
        Placement p1 = new Placement(c1, 'V');
        Placement p2 = new Placement(c2, 'h');
        assertEquals(p1.getCoordinate(), p2.getCoordinate());
    }

    @Test
    void test_getOrientation() {
        Coordinate c1 = new Coordinate(3,2);
        Coordinate c2 = new Coordinate(1,4);
        Placement p1 = new Placement(c1, 'V');
        Placement p2 = new Placement(c2, 'V');
        Placement p3 = new Placement(c2, 'v');
        assertEquals(p1.getOrientation(), p2.getOrientation());
        assertEquals(p1.getOrientation(), p3.getOrientation());
    }

    @Test
    void test_ToString() {
        Coordinate c1 = new Coordinate(3,2);
        Placement p1 = new Placement(c1, 'h');
        Placement p2 = new Placement("E7v");
        assertEquals("(3, 2)H", p1.toString());
        assertEquals("(4, 7)V", p2.toString());
    }

    @Test
    void test_Equals() {
        Placement p1 = new Placement("B7v");
        Placement p2 = new Placement("B7v");
        Placement p3 = new Placement("B7h");
        Placement p4 = new Placement("a7V");
        Coordinate c1 = new Coordinate(2, 3);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
        assertTrue(p1.equals(p2));
        assertFalse(p3.equals(p2));
        assertFalse(p4.equals(p2));
        assertFalse(p1.equals(c1));
    }

    @Test
    void test_HashCode() {
        Placement p1 = new Placement("B7v");
        Placement p2 = new Placement("B7v");
        Placement p3 = new Placement("B7h");
        Placement p4 = new Placement("a7V");
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
        assertNotEquals(p1.hashCode(), p4.hashCode());
    }
    @Test
    void test_Placement_construct_invalid_input(){
        assertThrows(IllegalArgumentException.class, ()->new Placement("A0VV"));
        assertThrows(IllegalArgumentException.class, ()->new Placement("A0Z"));
    }


}