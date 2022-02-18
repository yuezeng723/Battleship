package edu.duke.yz723.battleship;

import org.checkerframework.checker.units.qual.C;

import java.util.HashSet;

public class TShip<T> extends BasicShip<T> {
    final String name; //in version2 the name is always Carrier
    protected Coordinate upperLeft;
    final char orientation;


    @Override
    public String getName() {
        return name;
    }

    public TShip (Coordinate upperLeft, Character orien, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(makeCoords(upperLeft, orien), myDisplayInfo, enemyDisplayInfo);
        this.name = "Battleship";
        this.upperLeft = upperLeft;
        this.orientation = Character.toUpperCase(orien);
    }

    /**
     * A simple constructor make a TShip
     * @param upperLeft is the upperLeft coordinate of this ship

     * @param data is the view based display info. e.g. 'c'
     * @param onHit is the view based display info. e.g. '*'
     */
    public TShip(Coordinate upperLeft, Character orien, T data, T onHit) {
        this(upperLeft, orien, new SimpleShipDisplayInfo<T>(data, onHit),new SimpleShipDisplayInfo<T>(null, data));
    }

    public Coordinate getUpperLeft(){return upperLeft;}

    static HashSet<Coordinate> makeCoords(Coordinate upperLeft, Character orien){
        HashSet<Coordinate> hashSet = new HashSet<>();
        int startRow = upperLeft.getRow();
        int startColumn = upperLeft.getColumn();
        char orient = Character.toUpperCase(orien);
        if (orient == 'H' || orient == 'V'  ) {
            throw new IllegalArgumentException("The orientation of Tship should be u or r or d or l\n");
        }else {
            if (orient == 'U') {
                hashSet.add(new Coordinate(startRow, startColumn + 1));
                hashSet.add(new Coordinate(startRow + 1, startColumn));
                hashSet.add(new Coordinate(startRow + 1, startColumn + 1));
                hashSet.add(new Coordinate(startRow + 1, startColumn + 2));
                return hashSet;
            } else if (orient == 'R') {
                hashSet.add(new Coordinate(startRow, startColumn));
                hashSet.add(new Coordinate(startRow + 1, startColumn));
                hashSet.add(new Coordinate(startRow + 1, startColumn + 1));
                hashSet.add(new Coordinate(startRow + 2, startColumn));
                return hashSet;
            } else if (orient == 'D') {
                hashSet.add(new Coordinate(startRow, startColumn));
                hashSet.add(new Coordinate(startRow, startColumn + 1));
                hashSet.add(new Coordinate(startRow, startColumn + 2));
                hashSet.add(new Coordinate(startRow + 1, startColumn + 1));
                return hashSet;
            }
            //the orien must be 'L'
            else {
                hashSet.add(new Coordinate(startRow, startColumn + 1));
                hashSet.add(new Coordinate(startRow + 1, startColumn));
                hashSet.add(new Coordinate(startRow + 1, startColumn + 1));
                hashSet.add(new Coordinate(startRow + 2, startColumn + 1));
                return hashSet;
            }
        }
    }

    @Override
    public Coordinate getCenter(){
        if (orientation == 'U'){
            return new Coordinate(upperLeft.getRow() + 1, upperLeft.getColumn() +1);
        }
        if (orientation == 'R'){
            return new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn());
        }
        if (orientation == 'D'){
            return new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+1);
        }
        if (orientation == 'L'){
            return new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+1);
        }else throw new IllegalArgumentException("The orientation of Tship should be u or r or d or l\n");
    }

    @Override
    public Coordinate[] getRelative(){
        Coordinate center = this.getCenter();
        Coordinate[] relatives = new Coordinate[4];
        if (orientation == 'U') {
            relatives[0] = new Coordinate(center.getRow(), center.getColumn());
            relatives[1] = new Coordinate(center.getRow(), center.getColumn()-1);
            relatives[2] = new Coordinate(center.getRow()-1, center.getColumn());
            relatives[3] = new Coordinate(center.getRow(), center.getColumn()+1);
        }
        else if (orientation == 'R') {
            relatives[0] = new Coordinate(center.getRow(), center.getColumn());
            relatives[1] = new Coordinate(center.getRow()-1, center.getColumn());
            relatives[2] = new Coordinate(center.getRow(), center.getColumn()+1);
            relatives[3] = new Coordinate(center.getRow()+1, center.getColumn());
        }
        else if (orientation == 'D') {
            relatives[0] = new Coordinate(center.getRow(), center.getColumn());
            relatives[1] = new Coordinate(center.getRow(), center.getColumn()+1);
            relatives[2] = new Coordinate(center.getRow()+1, center.getColumn());
            relatives[3] = new Coordinate(center.getRow(), center.getColumn()-1);
        }
        else if (orientation == 'L'){
            relatives[0] = new Coordinate(center.getRow(), center.getColumn());
            relatives[1] = new Coordinate(center.getRow()+1, center.getColumn());
            relatives[2] = new Coordinate(center.getRow(), center.getColumn()-1);
            relatives[3] = new Coordinate(center.getRow()-1, center.getColumn());
        }else throw new IllegalArgumentException("The orientation of Tship should be u or r or d or l\n");
        return relatives;
    }
}
