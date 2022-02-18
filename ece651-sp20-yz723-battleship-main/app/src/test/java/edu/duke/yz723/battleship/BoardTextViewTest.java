package edu.duke.yz723.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardTextViewTest {
    /**
     * help test display empty board with customized width and height
     * @param w width of the tested board
     * @param h height of the tested board
     * @param expectedHeader string format expected header
     * @param body string format expected board body
     */
    private void emptyBoardHelper(int w, int h, String expectedHeader, String body){
        Board<Character> b1 = new BattleShipBoard<Character>(w, h, 'X');
        BoardTextView view = new BoardTextView(b1);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + body + expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());
    }

    @Test
    public void test_display_empty_2by2() {
        Board<Character> b1 = new BattleShipBoard<Character>(2, 2, 'X');
        BoardTextView view = new BoardTextView(b1);
        String expectedHeader= "  0|1  \n";
        String expectedRow = "A  |  A\n"+
                             "B  |  B\n";
        emptyBoardHelper(2, 2, expectedHeader, expectedRow);
    }

    @Test
    public void test_display_empty_3by2() {
        String expectedHeader = "  0|1|2  \n";
        String expectedRow = "A  | |  A\n" +
                              "B  | |  B\n";
        emptyBoardHelper(3,2,expectedHeader,expectedRow);
    }

    @Test
    public void test_display_empty_3by5(){
        String expectedHeader ="  0|1|2  \n";
        String expectedRow =
                "A  | |  A\n"+
                        "B  | |  B\n"+
                        "C  | |  C\n"+
                        "D  | |  D\n"+
                        "E  | |  E\n";
        emptyBoardHelper(3,5,expectedHeader,expectedRow);
    }

    @Test
    public void test_invalid_board_size() {
        Board<Character> wideBoard = new BattleShipBoard<Character>(11,20, 'X');
        Board<Character> tallBoard = new BattleShipBoard<Character>(10,27, 'X');
        //you should write two assertThrows here
        assertThrows(IllegalArgumentException.class, ()->new BoardTextView(wideBoard));
        assertThrows(IllegalArgumentException.class, ()->new BoardTextView(tallBoard));
    }

    //test : add new ship to the boardTextView, check wether it meet the expected graph
    @Test
    public void test_displayMyOwnBoard_full_board_3by5(){
        Board<Character> fullBoard = new BattleShipBoard<Character>(3,5, 'X');
        BoardTextView view = new BoardTextView(fullBoard);
        fullBoard.tryAddShip(new RectangleShip<Character>(new Coordinate(4, 2),'s', '*'));
        fullBoard.tryAddShip(new RectangleShip<Character>(new Coordinate(0, 0),'s', '*'));
        fullBoard.tryAddShip(new RectangleShip<Character>(new Coordinate(1, 1),'s', '*'));
        String expectedHeader ="  0|1|2  \n";
        String expectedRow =
                        "A s| |  A\n"+
                        "B  |s|  B\n"+
                        "C  | |  C\n"+
                        "D  | |  D\n"+
                        "E  | |s E\n";
        String expectedAns = expectedHeader + expectedRow +expectedHeader;
        String outPut = view.displayMyOwnBoard();
        assertEquals(expectedAns, outPut);
    }
    @Test
    void test_displayEnemyBoard(){
        String expectedEnemyBoard_notHit =
                "  0|1|2  \n" +
                "A  | |  A\n"+
                "B  | |  B\n"+
                "C  | |  C\n"+
                "D  | |  D\n"+
                "E  | |  E\n"+
                "  0|1|2  \n";
        String expectedMyOwnBoard_notHit =
                "  0|1|2  \n" +
                "A d| |  A\n"+
                "B d| |  B\n"+
                "C d| |  C\n"+
                "D  | |  D\n"+
                "E  | |  E\n"+
                "  0|1|2  \n";
        String expectedEnemyBoard_miss =
                "  0|1|2  \n" +
                "A  | |  A\n"+
                "B  | |  B\n"+
                "C  | |  C\n"+
                "D  | |X D\n"+
                "E  | |  E\n"+
                "  0|1|2  \n";
        String expectedEnemyBoard_hit =
                "  0|1|2  \n" +
                "A d| |  A\n"+
                "B  | |  B\n"+
                "C  | |  C\n"+
                "D  | |X D\n"+
                "E  | |  E\n"+
                "  0|1|2  \n";
        Board<Character> board = new BattleShipBoard<Character>(3,5, 'X');
        BoardTextView view = new BoardTextView(board);
        V1ShipFactory shipFactory = new V1ShipFactory();
        board.tryAddShip(shipFactory.makeDestroyer(new Placement("a0v")));
        //check my board display to myself
        assertEquals(expectedMyOwnBoard_notHit, view.displayMyOwnBoard());
        //check my board display to enemy
        assertEquals(expectedEnemyBoard_notHit, view.displayEnemyBoard());
        //fire at, but missed
        board.fireAt(new Coordinate(3,2));
        assertEquals(expectedEnemyBoard_miss, view.displayEnemyBoard());
        //fire at, and hit
        board.fireAt(new Coordinate(0,0));
        assertEquals(expectedEnemyBoard_hit, view.displayEnemyBoard());
    }

    @Test
    void test_displayMyBoardWithEnemyNextToIt(){
        BattleShipBoard<Character> myBoard = new BattleShipBoard<>(10,20,'X');
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10,20,'X');
        V1ShipFactory sf = new V1ShipFactory();
        myBoard.tryAddShip(sf.makeDestroyer(new Placement("a0v")));
        myBoard.fireAt(new Coordinate(0,0));
        enemyBoard.tryAddShip(sf.makeDestroyer(new Placement("a0h")));
        enemyBoard.fireAt(new Coordinate(0,1));
        enemyBoard.fireAt(new Coordinate(0,9));
        BoardTextView myView = new BoardTextView(myBoard);
        BoardTextView enemyView = new BoardTextView(enemyBoard);
        String expected =
                "     Your ocean                           Player B's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9  \n" +
                        "A *| | | | | | | | |  A                A  |d| | | | | | | |X A\n" +
                        "B d| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C d| | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K                K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L                L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M                M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N                N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O                O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P                P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q                Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R                R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S                S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T                T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9  \n";
        assertEquals(expected, myView.displayMyBoardWithEnemyNextToIt(enemyView,"Your ocean", "Player B's ocean"));
    }


}