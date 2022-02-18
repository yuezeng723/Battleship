package edu.duke.yz723.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleShipDisplayInfoTest {
    @Test
    public void test_getInfo(){
        ShipDisplayInfo<Character> disInfo = new SimpleShipDisplayInfo<>('s', '*');
        Coordinate c1= new Coordinate(1,2);
        Coordinate c2= new Coordinate(2,3);
        assertEquals('*', disInfo.getInfo(c1, true));
        assertEquals('s', disInfo.getInfo(c2,false));
    }
    @Test
    public void test_constructor(){
        SimpleShipDisplayInfo<Character> disInfo = new SimpleShipDisplayInfo<>('s', '*');
        assertEquals('s',disInfo.myData);
        assertEquals('*',disInfo.onHit);
    }

}