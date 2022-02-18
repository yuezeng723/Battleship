package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InBoundsRuleCheckerTest {
    @Test
    public void test_within_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> des = sf.makeDestroyer(new Placement(new Coordinate(1,1),'V'));
        assertNull(board.tryAddShip(des));
        //test rule chain
        Ship<Character> desNew = sf.makeDestroyer(new Placement(new Coordinate(1,2),'V'));
        assertNull(board.tryAddShip(desNew));
    }
    @Test
    public void test_out_bottom_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> des = sf.makeDestroyer(new Placement(new Coordinate(2,1),'V'));
        assertEquals("That placement is invalid: the ship goes off the bottom of the board.",board.tryAddShip(des));
    }

    @Test
    public void test_out_right_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> des = sf.makeDestroyer(new Placement(new Coordinate(2,2),'H'));
        assertEquals("That placement is invalid: the ship goes off the right of the board.",board.tryAddShip(des));
    }

    @Test
    public void test_out_top_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        Ship<Character> testShip = new RectangleShip<Character>(new Coordinate(-1,0), 't', '*');
        assertEquals("That placement is invalid: the ship goes off the top of the board.", board.tryAddShip(testShip));
    }
    @Test
    public void test_out_left_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        Ship<Character> testShip = new RectangleShip<Character>(new Coordinate(0,-1), 't', '*');
        assertEquals("That placement is invalid: the ship goes off the left of the board.", board.tryAddShip(testShip));
    }
    @Test
    void test_within_bound_new_ship_tShip_with_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> tship = sf.makeBattleship(new Placement(new Coordinate(0,0),'u'));
        assertNull(board.tryAddShip(tship));
    }
    @Test
    void test_new_ship_tShip_out_right_bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> tship = sf.makeBattleship(new Placement(new Coordinate(0,1),'u'));
        assertEquals("That placement is invalid: the ship goes off the right of the board.",board.tryAddShip(tship));
    }

    @Test
    void test_new_ship_zShip_out_right_Bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> zship = sf.makeCarrier(new Placement(new Coordinate(0,0),'r'));
        assertEquals("That placement is invalid: the ship goes off the right of the board.",board.tryAddShip(zship));
    }
    @Test
    void test_new_ship_zShip_out_bottom_Bound(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(null), 'X');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> zship = sf.makeCarrier(new Placement(new Coordinate(1,0),'d'));
        assertEquals("That placement is invalid: the ship goes off the bottom of the board.",board.tryAddShip(zship));
    }


}