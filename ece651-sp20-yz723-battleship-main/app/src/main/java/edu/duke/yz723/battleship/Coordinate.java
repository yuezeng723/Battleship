package edu.duke.yz723.battleship;

import java.util.Locale;

public class Coordinate {
    private final int column;
    private final int row;
    public int getColumn(){
        return column;
    }
    public int getRow(){
        return row;
    }
    //a constructor that takes the row and column
    // and initalize the members
    public Coordinate(int row, int column){
        this.column = column;
        this.row = row;
    }
    //a constructor takes in a string like "A2" and makes the Coordinate
    //that corresponds to that string (e.g. row=0, column =2).
    public Coordinate(String descr){
        if (descr.length() != 2){
            throw new IllegalArgumentException("The coordinate typed in should be one character and one number.\n");
        }
        descr = descr.toUpperCase();
        int letter = descr.charAt(0);
        int number = descr.charAt(1);
        if (letter < 'A' || letter > 'Z') {
            throw new IllegalArgumentException("The letter should between A to Z\n");
        }
        if (number < '0' || number > '9') {
            throw new IllegalArgumentException("The number should between 0 to 9\n");
        }
        this.row = letter - 'A';
        this.column = number - '0';
    }
    @Override
    public boolean equals(Object o) {
        //判断类相同，然后判断filed
        if (o.getClass().equals(getClass())) {
            Coordinate c = (Coordinate) o;
            return row == c.row && column == c.column;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
