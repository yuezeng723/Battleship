package edu.duke.yz723.battleship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BasicShip<T> implements Ship<T>{
    /**
     * hold any shape of ship (by putting in all coordinates
     * it occupies), and track which ones have been hit.
     * false: not hit
     * true: hit
     */
    protected HashMap<Coordinate, Boolean> myPieces;
    protected ShipDisplayInfo<T> myDisplayInfo;
    protected ShipDisplayInfo<T> enemyDisplayInfo;
    protected Coordinate upperLeft;


    /**
     * constructor: initialize myPieces to have each Coordinate in where mapped to false.
     */
    public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo){
        myPieces = new HashMap<Coordinate, Boolean>();
        for (Coordinate c : where) {
            myPieces.put(c, false);
        }
        this.myDisplayInfo = myDisplayInfo;
        this.enemyDisplayInfo = enemyDisplayInfo;
    }
    public BasicShip(Coordinate upperLeft, Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo){
        this(where, myDisplayInfo, enemyDisplayInfo);
        this.upperLeft = upperLeft;
    }

    @Override
    public boolean occupiesCoordinates(Coordinate where) {
        return this.myPieces.containsKey(where);
    }

    @Override
    public boolean isSunk() {
        for (Map.Entry<Coordinate, Boolean> entry : myPieces.entrySet()){
            if (!entry.getValue()){return false;}
        }
        return true;
    }

    @Override
    public void recordHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        if (!myPieces.get(where)){
            myPieces.replace(where, false, true);
        }
    }

    @Override
    public boolean wasHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        return myPieces.get(where);
    }

    @Override
    public T getDisplayInfoAt(Coordinate where, boolean myShip) {
        checkCoordinateInThisShip(where);
        if (myShip){
            return myDisplayInfo.getInfo(where, wasHitAt(where));
        }else{
            return enemyDisplayInfo.getInfo(where, wasHitAt(where));
        }
    }

    /**
     * check wether the coordinate is in this ship, if not throw an exception
     * @param c Coordinate you want to check
     */
    protected void checkCoordinateInThisShip(Coordinate c){
        if (!myPieces.containsKey(c)){
            throw new IllegalArgumentException("This Coordinate is not in the ship!\n");
        }
    }

    @Override
    public Iterable<Coordinate> getCoordinates(){
        return myPieces.keySet();
    }

    @Override
    public Coordinate getUpperLeft(){
        return upperLeft;
    }

    @Override
    public HashSet<Coordinate> getAllHitCoords(){
        HashSet<Coordinate> allHitCoords = new HashSet<>();
        for (Coordinate it : this.getCoordinates()){
            if (this.wasHitAt(it)){
                allHitCoords.add(it);
            }
        }
        return allHitCoords;
    }

    @Override
    public Coordinate getCenter(){
        return upperLeft;
    }

    @Override
    public boolean checkShipHit(){
        int hitNum = getAllHitCoords().size();
        if (hitNum != 0){
            return true;
        }else return false;
    }





}
