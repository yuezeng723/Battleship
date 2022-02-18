package edu.duke.yz723.battleship;


public class Placement {
    final Coordinate where;
    final char orientation;

    public Coordinate getCoordinate(){
        return where;
    }

    /**
     * getter for Orientation
     * @return the upper class of orientation
     */
    public char getOrientation(){
        return orientation;
    }

    /**
     * convert the class fields to string
     * @return converted string object
     */
    @Override
    public String toString(){
        String ans = where.toString() + orientation;
        return ans;
    }

    /**
     * check if the this object equals to the target one
     * @param o target object
     * @return true for equal, otherwise returns false
     */
    @Override
    public boolean equals(Object o){
        if (this.getClass().equals(o.getClass())){
            Placement c = (Placement) o;
            return where.equals(c.getCoordinate()) && orientation == c.getOrientation();
        }
        return false;
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    //constructor
    public Placement(Coordinate where, char orientation){
        this.where = where;
        this.orientation = Character.toUpperCase(orientation);
    }

    /**
     * a constructor takes in A0V, set Coordinate to (0,0) and orientation to 'V'
     * @param descr a string with 3 characters. e.g. A0V
     */
    public Placement(String descr){
        if (descr.length() != 3){
            throw new IllegalArgumentException("The input for placement should be three characters\n");
        }
        String coordinate = "" + descr.charAt(0) + descr.charAt(1);
        Coordinate where = new Coordinate(coordinate);
        this.where = where;
        char orientation = Character.toUpperCase(descr.charAt(2));
        if (orientation != 'V' && orientation != 'H'&& orientation != 'U' && orientation != 'R' && orientation != 'D' && orientation != 'L'){
            throw new IllegalArgumentException("The orientation should be V or H or U or R or D or L or v or h or u or r or d or l\n");
        }
        this.orientation = orientation;
    }
}
