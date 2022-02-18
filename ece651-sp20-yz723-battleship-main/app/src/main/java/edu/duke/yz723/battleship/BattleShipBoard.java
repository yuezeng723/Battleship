package edu.duke.yz723.battleship;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @
 * @param <T>
 */
public class BattleShipBoard<T> implements Board<T> {
    private final int width;
    private final int height;
    final ArrayList<Ship<T>> myShips;
    private final PlacementRuleChecker<T> placementChecker;
    final HashSet<Coordinate> enemyMisses;
    final T missInfo;


    public int getWidth(){return width;};
    public int getHeight(){return height;};
    /**
     * Constructs a BattleShipBoard with the specified width
     * and height
     * @param width is the width of the newly constructed board.
     * @param height is the height of the newly constructed board.
     * @throws IllegalArgumentException if the width or height are less than or equal to zero.
     */
    public BattleShipBoard(int width, int height, PlacementRuleChecker<T> placementChecker, T missInfo) {
        if (width <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + height);
        }
        this.width = width;
        this.height = height;
        this.myShips = new ArrayList<Ship<T>>();
        this.placementChecker = placementChecker;
        this.enemyMisses = new HashSet<>();
        this.missInfo = missInfo;
    }

    /**
     * constructor takes in width and height. Initialize NoCollision and InBound rule checkers
     * @param w width
     * @param h height
     */
    public BattleShipBoard(int w, int h, T missInfo) {
        this(w, h, new NoCollisionRuleChecker<T>(new InBoundsRuleChecker<T>(null)), missInfo);
    }
    @Override
    public String tryAddShip(Ship<T> toAdd){
        if (placementChecker.checkPlacement(toAdd, this) == null){
            myShips.add(toAdd);
            return null;
        }
        return placementChecker.checkPlacement(toAdd, this);
    }

    @Override
    public T whatIsAtForSelf(Coordinate where) {
        return whatIsAt(where, true);
    }

    @Override
    public T whatIsAtForEnemy(Coordinate where) {
        return whatIsAt(where, false);
    }

    /**
     * Get our ship or enemy ship's display info on specific coordinate.
     * @param where is the coordinate you want to check.
     * @param isSelf is true for selfShip, false for enemyShip
     * @return Display info on that coordinate.
     *                                 Our own               Enemy
     * square has unhit ship     ship's letter (s,d,c,b)     blank
     * square has hit ship               *               ship's letter (s,d,c,b)
     * square has miss                                         X
     * square is empty, unmissed
     */
    protected T whatIsAt(Coordinate where, boolean isSelf){
        //enemy's ship
        if (!isSelf){
            if (enemyMisses.contains(where)){
                return missInfo;
            }
        }
        for (Ship<T> s: myShips) {
            if (s.occupiesCoordinates(where)){
                return s.getDisplayInfoAt(where, isSelf);
            }
        }
        return null;
    }

    @Override
    public Ship<T> fireAt(Coordinate c){
        //search for the ship occupied this coordinate
        for (Ship<T> it : myShips){
            //if this ship is hit, record and return this ship
            if(it.occupiesCoordinates(c)){
                it.recordHitAt(c);
                return it;
            }
        }
        //if missed, record this coordinate
        enemyMisses.add(c);
        return null;
    }

    /**
     * Check this player's board is lose
     * @return True for Lose.
     */
    @Override
    public boolean checkLose(){
        int sunkNum = 0;
        for (Ship<T> it : myShips){
            if (it.isSunk()){
                sunkNum++;
            }
        }
        if(sunkNum == myShips.size()){
            return true;
        }else return false;
    }

    @Override
    public Ship<T> getShip(Coordinate c){
        for (Ship<T> it: myShips){
            if (it.occupiesCoordinates(c)){
                return it;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Ship<T>> getAllShips(){return myShips;}


}
