package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoCollisionRuleCheckerTest {
    @Test
    public void test_no_collision(){
        Board<Character> board = new BattleShipBoard<>(3, 4, new InBoundsRuleChecker<>(null), 'X');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> des = sf.makeDestroyer(new Placement(new Coordinate(0,0),'V'));
        //check empty board
        assertNull(board.tryAddShip(des));
        //check filled board
        Ship<Character> desNew = sf.makeDestroyer(new Placement(new Coordinate(3,0),'H'));
        assertNull(board.tryAddShip(desNew));
    }
    @Test
    public void test_collision(){
        Board<Character> board = new BattleShipBoard<>(3,4, new InBoundsRuleChecker<>(new NoCollisionRuleChecker<>(null)), 'X');
        V1ShipFactory sf = new V1ShipFactory();
        Ship<Character> des0 = sf.makeDestroyer(new Placement(new Coordinate(0,0),'V'));
        assertNull(board.tryAddShip(des0));
        Ship<Character> des1 = sf.makeDestroyer(new Placement(new Coordinate(0,0),'V'));
        assertEquals("That placement is invalid: the ship overlaps another ship.",board.tryAddShip(des1));
    }
    @Test
    void test_apply_two_rules_with_new_shape_ships(){
        Board<Character> board = new BattleShipBoard<>(10,10, new InBoundsRuleChecker<>(new NoCollisionRuleChecker<>(null)), 'X');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> tBattleShip = sf.makeBattleship(new Placement(new Coordinate(0,0),'l'));
        assertNull(board.tryAddShip(tBattleShip));
        Ship<Character> zCarrier0 = sf.makeCarrier(new Placement(new Coordinate(2,0),'d'));
        assertNull(board.tryAddShip(zCarrier0));
    }
    @Test
    void test_apply_two_rules_with_new_shape_ships_bank_overlap(){
        Board<Character> board = new BattleShipBoard<>(10,10, new InBoundsRuleChecker<>(new NoCollisionRuleChecker<>(null)), 'X');
        V2ShipFactory sf = new V2ShipFactory();
        Ship<Character> tBattleShip = sf.makeBattleship(new Placement(new Coordinate(0,0),'d'));
        assertNull(board.tryAddShip(tBattleShip));
        Ship<Character> zCarrier0 = sf.makeCarrier(new Placement(new Coordinate(1,0),'l'));
        assertNull(board.tryAddShip(zCarrier0));
    }

}