package edu.duke.yz723.battleship;

import java.util.function.Function;

/**
 * This class handles textual display of
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the
 * enemy's board.
 */
public class BoardTextView {
    //the board to display
    private final Board<Character> toDisplay;

    /**
     * Constructs a BoardView, given the board it will display.
     *
     * @param toDisplay is the Board to display
     * @throws IllegalArgumentException if the board is larger than 10x26.
     */
    public BoardTextView(Board<Character> toDisplay) {
        this.toDisplay = toDisplay;
        if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
            throw new IllegalArgumentException(
                    "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
        }
    }

    /**
     * Helper for displayMyOwnBoard and displayEnemyBoard
     * @param getSquareFn is function whatIsAtForSelf or whatIsAtForEnemy
     * @return is your board or enemy's board
     */
    public String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
        return this.makeHeader()+
                this.makeRow(getSquareFn)+
                this.makeHeader();
    }

    /**
     * This makes the header line, e.g."  0|1|2|3|4  \n"
     * @return the String that is the header line for the given board
     */
    String makeHeader() {
        StringBuilder ans = new StringBuilder("  "); // README shows two spaces at
        String sep= ""; //start with nothing to separate, then switch to | to separate
        for (int c = 0; c < toDisplay.getWidth(); c++) {
            ans.append(sep);
            ans.append(c);
            sep = "|";
        }
        ans.append("  ");//assure the header length is 2*w+3
        ans.append("\n");
        return ans.toString();
    }

    /**
     * This make the row lines, e.g." A  | | | |  A\n"
     * @return The string that contends all row lines
     */
    String makeRow(Function<Coordinate, Character> getSquareFn){
        StringBuilder ans = new StringBuilder("");
        int rowIndex = 'A';
        for (int r = 0; r < toDisplay.getHeight(); r++){
            ans.append((char)rowIndex);
            ans.append(" ");
            for (int c = 0; c < toDisplay.getWidth() - 1; c++){
                Coordinate where = new Coordinate(r, c);
                if (getSquareFn.apply(where) == null){
                    ans.append(" ");
                }else{
                    ans.append(getSquareFn.apply(where));
                }
                ans.append("|");
            }
            Coordinate endWhere = new Coordinate(r, toDisplay.getWidth()-1);
            if (getSquareFn.apply(endWhere) == null){
                ans.append(" ");
            }else{
                ans.append(getSquareFn.apply(endWhere));
            }
            ans.append(" ");
            ans.append((char)rowIndex);
            ans.append("\n");
            rowIndex++;
        }
        return ans.toString();
    }

    /**
     * Make printable my board
     * @return string of my borad
     */
    public String displayMyOwnBoard() {
        return displayAnyBoard(toDisplay::whatIsAtForSelf);
    }

    /**
     * Make printable enemy's board
     * @return string of enemy's board
     */
    public String displayEnemyBoard(){
        return displayAnyBoard(toDisplay::whatIsAtForEnemy);
    }

    /**
     *During the game. Display my board on left and enemy's board on right
     * @param enemyView is the text view of enemy's board
     * @param myHeader is customized by yourself
     * @param enemyHeader is customized by yourself
     * @return is the printable board basing on string
     */
    public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader){
        int width = toDisplay.getWidth();
        int height = toDisplay.getHeight();
        //make header. Header = 4 blanks + myHeader + (2*w - myHeader.length() + 17) blanks + enemyHeader
        String header = "     " + myHeader + blankPadding(2*width - myHeader.length() + 17) + enemyHeader + "\n";
        //make body
        String blanksBetween = blankPadding(16);
        String[] myLines = this.displayMyOwnBoard().split("\n");
        String[] enemyLines = enemyView.displayEnemyBoard().split("\n");
        StringBuilder body = new StringBuilder("");
        for (int i = 0; i < height + 2; i++){
            body.append(myLines[i]);
            body.append(blanksBetween);
            body.append(enemyLines[i]);
            body.append("\n");
        }
        return header + body.toString();
    }

    /**
     * Make string of customized size blanks
     * @param size is the number of blanks
     * @return is the size numbered blanks
     */
    public String blankPadding(int size){
        StringBuilder blanks = new StringBuilder("");
        for (int i = 0; i < size; i++){
            blanks.append(" ");
        }
        return blanks.toString();
    }

}
