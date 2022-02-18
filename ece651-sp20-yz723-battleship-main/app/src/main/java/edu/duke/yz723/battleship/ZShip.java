package edu.duke.yz723.battleship;

import java.util.Arrays;
import java.util.HashSet;

public class ZShip<T> extends BasicShip<T> {
    final String name; //in version2 the name is always Battleship
    private Coordinate upperLeft;
    protected char orientation;


    @Override
    public String getName() {
        return name;
    }

    public ZShip (Coordinate upperLeft, Character orien, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(makeCoords(upperLeft, orien), myDisplayInfo, enemyDisplayInfo);
        this.name = "Carrier";
        this.upperLeft = upperLeft;
        this.orientation = Character.toUpperCase(orien);
    }

    /**
     * A simple constructor make a ZShip
     * @param upperLeft is the upperLeft coordinate of this ship
     * @param data is the view based display info. e.g. 'b'
     * @param onHit is the view based display info. e.g. '*'
     */
    public ZShip(Coordinate upperLeft, Character orien, T data, T onHit) {
        this(upperLeft, orien, new SimpleShipDisplayInfo<T>(data, onHit),new SimpleShipDisplayInfo<T>(null, data));
    }

    /**
     * UpperLeft coordinate getter
     * @return is the upperLeft
     */
    public Coordinate getUpperLeft(){
        return upperLeft;
    }

    static HashSet<Coordinate> makeCoords(Coordinate upperLeft, Character orien){
        HashSet<Coordinate> hashSet = new HashSet<>();
        int startRow = upperLeft.getRow();
        int startColumn = upperLeft.getColumn();
        Character orientation = Character.toUpperCase(orien);
        if (orientation == 'H' || orientation == 'V'  ){
            throw new IllegalArgumentException("The orientation of Zship should be u or r or d or l\n");
        }else {
            if (orientation == 'U') {
                hashSet.addAll(Arrays.asList(
                        new Coordinate(startRow, startColumn), new Coordinate(startRow + 1, startColumn),
                        new Coordinate(startRow + 2, startColumn), new Coordinate(startRow + 2, startColumn + 1),
                        new Coordinate(startRow + 3, startColumn), new Coordinate(startRow + 3, startColumn + 1),
                        new Coordinate(startRow + 4, startColumn + 1)));
                return hashSet;
            } else if (orientation == 'R') {
                hashSet.addAll(Arrays.asList(
                        new Coordinate(startRow, startColumn + 1), new Coordinate(startRow, startColumn + 2),
                        new Coordinate(startRow, startColumn + 3), new Coordinate(startRow, startColumn + 4),
                        new Coordinate(startRow + 1, startColumn), new Coordinate(startRow + 1, startColumn + 1),
                        new Coordinate(startRow + 1, startColumn + 2)));
                return hashSet;
            } else if (orientation == 'D') {
                hashSet.addAll(Arrays.asList(
                        new Coordinate(startRow, startColumn), new Coordinate(startRow + 1, startColumn),
                        new Coordinate(startRow + 1, startColumn + 1), new Coordinate(startRow + 2, startColumn),
                        new Coordinate(startRow + 2, startColumn + 1), new Coordinate(startRow + 3, startColumn + 1),
                        new Coordinate(startRow + 4, startColumn + 1)));
                return hashSet;
            }
            //the orien must be 'L'
            else {
                hashSet.addAll(Arrays.asList(
                        new Coordinate(startRow, startColumn + 2), new Coordinate(startRow, startColumn + 3),
                        new Coordinate(startRow, startColumn + 4), new Coordinate(startRow + 1, startColumn),
                        new Coordinate(startRow + 1, startColumn + 1), new Coordinate(startRow + 1, startColumn + 2),
                        new Coordinate(startRow + 1, startColumn + 3)));
                return hashSet;
            }
        }
    }

    @Override
    public Coordinate getCenter(){
        if (orientation == 'U'){
            return new Coordinate(upperLeft.getRow() + 3, upperLeft.getColumn());
        }
        if (orientation == 'R'){
            return new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+1);
        }
        if (orientation == 'D'){
            return new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+1);
        }else{
            return new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+3);
        }
    }
    @Override
    public Coordinate[] getRelative(){
        Coordinate[] relatives = new Coordinate[7];
        if (orientation == 'U') {
            relatives[0] = new Coordinate(upperLeft.getRow()+3, upperLeft.getColumn());
            relatives[1] = new Coordinate(upperLeft.getRow()+2, upperLeft.getColumn());
            relatives[2] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn());
            relatives[3] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn());
            relatives[4] = new Coordinate(upperLeft.getRow()+2, upperLeft.getColumn()+1);
            relatives[5] = new Coordinate(upperLeft.getRow()+3, upperLeft.getColumn()+1);
            relatives[6] = new Coordinate(upperLeft.getRow()+4, upperLeft.getColumn()+1);
        }
        else if (orientation == 'R') {
            relatives[0] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+1);
            relatives[1] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+2);
            relatives[2] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+3);
            relatives[3] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+4);
            relatives[4] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+2);
            relatives[5] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+1);
            relatives[6] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn());
        }
        else if (orientation == 'D') {
            relatives[0] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+1);
            relatives[1] = new Coordinate(upperLeft.getRow()+2, upperLeft.getColumn()+1);
            relatives[2] = new Coordinate(upperLeft.getRow()+3, upperLeft.getColumn()+1);
            relatives[3] = new Coordinate(upperLeft.getRow()+4, upperLeft.getColumn()+1);
            relatives[4] = new Coordinate(upperLeft.getRow()+2, upperLeft.getColumn());
            relatives[5] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn());
            relatives[6] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn());
        }
        else if (orientation == 'L'){
            relatives[0] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+3);
            relatives[1] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+2);
            relatives[2] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn()+1);
            relatives[3] = new Coordinate(upperLeft.getRow()+1, upperLeft.getColumn());
            relatives[4] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+2);
            relatives[5] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+3);
            relatives[6] = new Coordinate(upperLeft.getRow(), upperLeft.getColumn()+4);
        }else throw new IllegalArgumentException("The orientation of Zship should be u or r or d or l\n");
        return relatives;
    }
}
