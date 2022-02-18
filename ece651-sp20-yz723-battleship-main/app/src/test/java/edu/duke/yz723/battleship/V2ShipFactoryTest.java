package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class V2ShipFactoryTest {
    private void checkShip(Ship<Character> testShip, String expectedName,
                           char expectedLetter, Coordinate... expectedLocs){
        //check the name
        assertEquals(expectedName, testShip.getName());
        for (Coordinate it : expectedLocs){
            assertEquals(expectedLetter, testShip.getDisplayInfoAt(it,true));
        }
    }
    @Test
    void test_make_submarine(){
        V2ShipFactory sf = new V2ShipFactory();
        Placement p1 = new Placement(new Coordinate(1, 3), 'V');
        Placement p2 = new Placement(new Coordinate(1, 3), 'H');
        Ship<Character> sub1 = sf.makeSubmarine(p1);
        Ship<Character> sub2 = sf.makeSubmarine(p2);
        checkShip(sub1, "Submarine", 's', new Coordinate(1,3), new Coordinate(2,3));
        checkShip(sub2, "Submarine", 's', new Coordinate(1,3), new Coordinate(1,4));
    }
    @Test
    void test_make_destroyer(){
        Placement p1 = new Placement(new Coordinate(1, 3), 'V');
        Placement p2 = new Placement(new Coordinate(1, 3), 'H');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> dis1 = sf.makeDestroyer(p1);
        Ship<Character> dis2 = sf.makeDestroyer(p2);
        checkShip(dis1, "Destroyer", 'd', new Coordinate(1,3), new Coordinate(2,3),new Coordinate(3,3));
        checkShip(dis2, "Destroyer", 'd', new Coordinate(1,3), new Coordinate(1,4),new Coordinate(1,5));
    }
    @Test
    void test_make_battleship(){
        V2ShipFactory sf = new V2ShipFactory();
        Placement p2 = new Placement(new Coordinate(0, 0), 'r');
        Ship<Character> bs2 = sf.makeBattleship(p2);
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(1,0);
        Coordinate c3 = new Coordinate(1,1);
        Coordinate c4 = new Coordinate(2,0);
        checkShip(bs2, "Battleship", 'b',c1,c2,c3,c4);

    }
    @Test
    void test_make_carrier(){
        V2ShipFactory sf = new V2ShipFactory();
        Placement p2 = new Placement(new Coordinate(0, 0), 'd');
        Ship<Character> carrier = sf.makeCarrier(p2);
        Coordinate[] c = new Coordinate[7];
        c[0] =new Coordinate(0,0);
        c[1] =new Coordinate(1,0);
        c[2] =new Coordinate(1,1);
        c[3] =new Coordinate(2,0);
        c[4] =new Coordinate(2,1);
        c[5] =new Coordinate(3,1);
        c[6] =new Coordinate(4,1);
        checkShip(carrier, "Carrier", 'c',c[0], c[1],c[2],c[3],c[4],c[5],c[6]);
    }
    @Test
    void test_make_Tship_with_invalid_input(){
        V2ShipFactory sf = new V2ShipFactory();
        Placement p2 = new Placement(new Coordinate(0, 0), 'H');
        assertThrows(IllegalArgumentException.class, ()->sf.makeBattleship(p2));
    }
    @Test
    void test_make_rectangle_ship_with_invalid_input(){
        V2ShipFactory sf = new V2ShipFactory();
        Placement p1 = new Placement("a0u");
        Placement p2 = new Placement("a0l");
        assertThrows(IllegalArgumentException.class, ()->sf.makeSubmarine(p1));
        assertThrows(IllegalArgumentException.class, ()->sf.makeDestroyer(p2));
    }

}