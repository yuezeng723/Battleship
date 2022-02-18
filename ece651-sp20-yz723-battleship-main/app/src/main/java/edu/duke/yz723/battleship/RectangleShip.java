package edu.duke.yz723.battleship;

import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T>{
    final String name;
    protected int width;
    protected int height;

    public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(upperLeft, makeCoords(upperLeft,width,height), myDisplayInfo, enemyDisplayInfo);
        this.name = name;
        this.width = width;
        this.height = height;
    }
    //two quick convinient constructor, refactorings of BasicShip and RectangleShip

    /**
     * Constructor for rectangle ship.
     * @param name is the name of this rectangle ship. e.g. "Submarine".
     * @param upperLeft is the upper-left coordinate of this ship.
     * @param width is the width of this ship.
     * @param height is the height of this ship.
     * @param data is the display info of this ship. e.g.'c', 'd'
     * @param onHit is the hit display info of this ship. e.g. "*"
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
        this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit),new SimpleShipDisplayInfo<T>(null, data));
    }
    public RectangleShip(Coordinate upperLeft, T data, T onHit) {
        this("testship", upperLeft, 1, 1, data, onHit);
    }


    /**
     * a name getter
     */
    @Override
    public String getName(){
        return name;
    }

    /**
     * Takes in the upperLeft Coordinate, width and height
     * to make rectangle ship. Filled in the hashSet with
     * it's all occupied Coordinates.
     * @param upperLeft The upperleft Coordinate of this ship
     * @param width ship's width
     * @param height ship's height
     * @return a hash-set filled in with the Coordinates occupied by this ship
     */
    static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height){
        HashSet<Coordinate> hashSet = new HashSet<>();
        for (int r = upperLeft.getRow(); r < height + upperLeft.getRow(); r++){
            for (int c = upperLeft.getColumn(); c < width + upperLeft.getColumn(); c++){
                hashSet.add(new Coordinate(r, c));
            }
        }
        return hashSet;
    }

    @Override
    public Coordinate[] getRelative(){
        Coordinate start = this.getUpperLeft();
        int row = start.getRow();
        int colum = start.getColumn();
        Coordinate[] relativeCoords = new Coordinate[width * height];
        int i = 0;
        for (int r = 0; r < height; r++){
            for (int c = 0; c < width; c++){
                i++;
                relativeCoords[i-1] = new Coordinate(row+r,colum+c);
            }
        }
        return relativeCoords;
    }



}
