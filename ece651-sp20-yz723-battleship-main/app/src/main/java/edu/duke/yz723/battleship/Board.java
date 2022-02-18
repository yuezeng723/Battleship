package edu.duke.yz723.battleship;

import java.util.ArrayList;

public interface Board<T> {
    public int getWidth();
    public int getHeight();

    /**
     * Try to add a ship on the board
     * @param toAdd is the ship you want to add
     * @return is null if add success; return a String description for failed
     */
    public String tryAddShip(Ship<T> toAdd);

    /**
     * Takes coordinate and return view-based display info of our ship.
     * @param where is the coordinate
     * @return If coordinate on the ship, return '*' for hit;
     * If not hit, ship's letter e.g.'c';
     * If missed, return blank.
     */
    public T whatIsAtForSelf(Coordinate where);

    /**
     * Takes coordinate and return view-based display info of enemy's ship
     * @param where is the coordinate
     * @return If coordinate on enemy' ship, return ship's letter e.g. 'c' for hit;
     * If not hit, return blank;
     * If missed, return 'X';
     */
    public T whatIsAtForEnemy(Coordinate where);

    /**
     * Search for any ship that occupies coordinate c.
     * If one is found, that Ship is "hit" by the attack and should
     * record it (you already have a method for that!).  Then we
     * should return this ship.
     * @param c the coordinate shoot by enemy
     * @return same ship with updated hit state
     */
    public Ship<T> fireAt(Coordinate c);

    public boolean checkLose();

    /**
     * Get the ship occupied the given coordinate
     * @param c is the coordinate in the ship
     * @return is the ship occupied the coordinate
     * if the coordinate is not in ship, return null
     */
    public Ship<T> getShip(Coordinate c);

    /**
     * A getter for all the ships on the board
     * @return is a list of all the ships on board
     */
    public ArrayList<Ship<T>> getAllShips();




}
