package edu.duke.yz723.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class HashSetFactoryTest {

    @Test
    void test_creatNewHitSet(){
        V2ShipFactory sf = new V2ShipFactory();
        BattleShipBoard<Character> board = new BattleShipBoard<>(10,20,'X');
        Ship<Character> zShip = sf.makeCarrier(new Placement("a1u"));
        Ship<Character> newShip = sf.makeCarrier(new Placement("c5r"));
        board.tryAddShip(zShip);
        board.fireAt(new Coordinate("c2"));
        board.fireAt(new Coordinate("d1"));
        HashSetFactory hf = new HashSetFactory(zShip, newShip);
        HashSet<Coordinate> expect = new HashSet<>();
        expect.add(new Coordinate("c6"));
        expect.add(new Coordinate("d7"));
        assertEquals(expect, hf.creatNewHitSet());
    }
}