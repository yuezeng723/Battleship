package edu.duke.yz723.battleship;

import java.util.HashMap;
import java.util.HashSet;

public class HashSetFactory {
    protected HashSet<Coordinate> oldHits;
    protected Ship<Character> shipToMove;
    protected Ship<Character> newShip;
    protected String name;

    //a constructor
    public HashSetFactory(Ship<Character> shipToMove, Ship<Character> newShip){
        this.oldHits = shipToMove.getAllHitCoords();
        this.shipToMove = shipToMove;
        this.newShip = newShip;
        this.name = shipToMove.getName();

    }

    public HashSet<Coordinate> creatNewHitSet(){
        HashSet<Coordinate> fireSet = new HashSet<>();
        Coordinate[] newShipCoords = newShip.getRelative();
        Coordinate[] oldShipCoords = shipToMove.getRelative();
        for (int i = 0; i < newShipCoords.length; i++){
            if (shipToMove.wasHitAt(oldShipCoords[i])){
                fireSet.add(newShipCoords[i]);
            }
        }
        return fireSet;
    }








}
