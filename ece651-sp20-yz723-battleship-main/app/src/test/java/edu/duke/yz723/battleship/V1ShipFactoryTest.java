package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class V1ShipFactoryTest {
    private void checkShip(Ship<Character> testShip, String expectedName,
                           char expectedLetter, Coordinate... expectedLocs){
        //check the name
        assertEquals(expectedName, testShip.getName());
        for (Coordinate it : expectedLocs){
            assertEquals(expectedLetter, testShip.getDisplayInfoAt(it,true));
        }
    }

    @Test
    public void test_makeSubmarine() {
        Placement p1 = new Placement(new Coordinate(1, 3), 'V');
        Placement p2 = new Placement(new Coordinate(1, 3), 'H');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> sub1 = sf.makeSubmarine(p1);
        Ship<Character> sub2 = sf.makeSubmarine(p2);
        checkShip(sub1, "Submarine", 's', new Coordinate(1,3), new Coordinate(2,3));
        checkShip(sub2, "Submarine", 's', new Coordinate(1,3), new Coordinate(1,4));
    }

    @Test
    public void test_makeBattleship() {
        Placement p1 = new Placement(new Coordinate(1, 3), 'V');
        Placement p2 = new Placement(new Coordinate(1, 3), 'H');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> bt1 = sf.makeBattleship(p1);
        Ship<Character> bt2 = sf.makeBattleship(p2);
        checkShip(bt1, "Battleship", 'b', new Coordinate(1,3), new Coordinate(2,3), new Coordinate(3,3), new Coordinate(4,3));
        checkShip(bt2, "Battleship", 'b', new Coordinate(1,3), new Coordinate(1,4), new Coordinate(1,5), new Coordinate(1,6));
    }

    @Test
    public void test_makeCarrier() {
        Placement p1 = new Placement(new Coordinate(1, 3), 'V');
        Placement p2 = new Placement(new Coordinate(1, 3), 'H');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> ca1 = sf.makeCarrier(p1);
        Ship<Character> ca2 = sf.makeCarrier(p2);
        checkShip(ca1, "Carrier", 'c', new Coordinate(1,3), new Coordinate(2,3),new Coordinate(3,3),new Coordinate(4,3),new Coordinate(5,3),new Coordinate(6,3));
        checkShip(ca2, "Carrier", 'c', new Coordinate(1,3), new Coordinate(1,4),new Coordinate(1,5),new Coordinate(1,6),new Coordinate(1,7),new Coordinate(1,8));
    }

    @Test
    public void test_makeDestroyer() {
        Placement p1 = new Placement(new Coordinate(1, 3), 'V');
        Placement p2 = new Placement(new Coordinate(1, 3), 'H');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> dis1 = sf.makeDestroyer(p1);
        Ship<Character> dis2 = sf.makeDestroyer(p2);
        checkShip(dis1, "Destroyer", 'd', new Coordinate(1,3), new Coordinate(2,3),new Coordinate(3,3));
        checkShip(dis2, "Destroyer", 'd', new Coordinate(1,3), new Coordinate(1,4),new Coordinate(1,5));
    }

}