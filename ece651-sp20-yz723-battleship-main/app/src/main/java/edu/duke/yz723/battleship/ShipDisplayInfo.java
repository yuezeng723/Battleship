package edu.duke.yz723.battleship;

public interface ShipDisplayInfo<T> {
    public T getInfo(Coordinate where, boolean hit);


}