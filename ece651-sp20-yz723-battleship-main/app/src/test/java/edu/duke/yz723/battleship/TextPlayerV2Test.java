package edu.duke.yz723.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TextPlayerV2Test {

    /**
     * Helper to creat TextPlayerV2
     * @param w the width of her board
     * @param h the height of her board
     * @param inputData location of placement. e.g. A0V
     * @param bytes ByteArrayOutputStream
     * @param name player's name. e.g. A
     * @return TextPlayer instance
     */
    private TextPlayerV2 createTextPlayerV2(int w, int h, String inputData, OutputStream bytes, String name) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V1ShipFactory shipFactory = new V1ShipFactory();
        return new TextPlayerV2(false,name, board, input, output, shipFactory,3,3);
    }
    private TextPlayerV2 createTextPlayerV2_V2(int w, int h, String inputData, OutputStream bytes, String name) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V2ShipFactory shipFactory = new V2ShipFactory();
        return new TextPlayerV2(false,name, board, input, output, shipFactory,3,3);
    }
    private TextPlayerV2 createTextPlayerV2_V2_new(int w, int h, String inputData, OutputStream bytes, String name, int sonarRemain, int moveRemain) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V2ShipFactory shipFactory = new V2ShipFactory();
        return new TextPlayerV2(false,name, board, input, output, shipFactory, sonarRemain, moveRemain);
    }


    @Test
    void test_read_placement_eof()throws IOException{
        TextPlayerV2 testPlayer = createTextPlayerV2(10,20, "",new ByteArrayOutputStream(),"A");
        String prompt = "Please enter a location for a ship:";
        assertThrows(EOFException.class, ()->testPlayer.readPlacement(prompt));
    }

    @Test
    void test_read_placement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2(10, 20, "B2V\nC8H\na4v\n", bytes, "A");
        String prompt = "Please enter a location for a ship:";
        //byte[] pro = prompt.getBytes();
        Placement[] expected = new Placement[3];
        expected[0] = new Placement(new Coordinate(1, 2), 'V');
        expected[1] = new Placement(new Coordinate(2, 8), 'H');
        expected[2] = new Placement(new Coordinate(0, 4), 'V');

        for (int i = 0; i < expected.length; i++) {
            Placement p = playerA.readPlacement(prompt);
            //bytes.writeBytes(pro);
            assertEquals(p, expected[i]);
            assertEquals(prompt +"\n", bytes.toString());
            bytes.reset();
        }
    }

    @Test
    void test_readPlacement_wrong_Placement_till_to_right() throws IOException{
        //over three Characters
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "B1Z\nB1U", bytes, "A");
        String prompt = "Player "+ playerA.name +" where do you want to place a Destroyer?";
        System.setOut(new PrintStream(bytes));
        playerA.readPlacement(prompt);
        String expected = "Player A where do you want to place a Destroyer?\n" +
                "java.lang.IllegalArgumentException: The orientation should be V or H or U or R or D or L or v or h or u or r or d or l\n" +
                "Player A where do you want to place a Destroyer?\n";
        assertEquals(expected, bytes.toString());
        //assertThrows(IllegalArgumentException.class, ()->playerA.readPlacement(prompt));
        System.setOut(ps);
    }

    //doOnePlacement of placing destroyer
    @Test
    void test_doOnePlacement_success() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "B1V\n", bytes, "A");
        String prompt = "Player "+ playerA.name +" where do you want to place a Destroyer?";
        String expectedHeader ="  0|1|2  \n";
        String expectedRow =
                "A  | |  A\n"+
                        "B  |d|  B\n"+
                        "C  |d|  C\n"+
                        "D  |d|  D\n"+
                        "E  | |  E\n";
        String expected = expectedHeader + expectedRow +expectedHeader;
        playerA.doOnePlacement("Destroyer", playerA.shipCreationFns.get("Destroyer"));
        System.setOut(new PrintStream(bytes));
        assertEquals(prompt + "\n" + expected, bytes.toString());
    }
    @Test
    void test_doOnePlacement_failed_ruleChecker()throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "B2H\n", bytes, "A");
        String prompt = "Player "+ playerA.name +" where do you want to place a Destroyer?\n";
        String expectedHeader ="  0|1|2  \n";
        String expectedRow =
                "A  | |  A\n"+
                        "B  | |  B\n"+
                        "C  | |  C\n"+
                        "D  | |  D\n"+
                        "E  | |  E\n";
        String expectedMessage = "That placement is invalid: the ship goes off the right of the board.\n";
        String expected = expectedHeader + expectedRow +expectedHeader;
        playerA.doOnePlacement("Destroyer", playerA.shipCreationFns.get("Destroyer"));
        System.setOut(new PrintStream(bytes));
        assertEquals(prompt, bytes.toString());
    }

    @Test
    void test_read_one_coordinate_validInput() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2(10, 20, "B2\nC8\na4\n", bytes, "A");
        String prompt = "Player A's turn:\n";
        Coordinate[] expected = new Coordinate[3];
        expected[0] = new Coordinate(1,2);
        expected[1] = new Coordinate(2,8);
        expected[2] = new Coordinate(0,4);
        for (int i = 0; i < expected.length; i++) {
            Coordinate coordinate = playerA.readCoordinate();
            //bytes.writeBytes(pro);
            assertEquals(coordinate, expected[i]);
            assertEquals("Please enter a coordinate:\n", bytes.toString());
            bytes.reset();
        }
    }
    @Test
    void test_read_one_coordinate_invalid_Input() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2(10, 20, "@8\na0\n", bytes, "A");
        String prompt =
                "Please enter a coordinate:\n";

        String exception_letter = "java.lang.IllegalArgumentException: The letter should between A to Z\n";
        String turnPrompt = "Please enter a valid coordinate:\n";
        Coordinate c = playerA.readCoordinate();
        assertEquals( prompt+exception_letter + turnPrompt + "\n", bytes.toString());
        assertEquals(new Coordinate(0,0), c);
    }

    @Test
    void test_play_one_turn_hit() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "a0\n", bytes, "A");
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<Character>(3,5,'X');
        BoardTextView enemyView = new BoardTextView(enemyBoard);
        V1ShipFactory sf = new V1ShipFactory();
        playerA.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        enemyBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        String prompt = "Player A's turn:\n";
        String expected =
                "     Your ocean             Player Yue's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A s|s|  A                A  | |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n";

        String expectedHit =
                "Please enter a coordinate:\n" +
                        "--------------------------------------------------------------------------------\n"+
                        "You hit a Submarine!\n"+
                        "--------------------------------------------------------------------------------\n";

        System.setOut(new PrintStream(bytes));
        assertThrows(EOFException.class, ()->playerA.playOneRound("Yue", enemyBoard, enemyView));
        System.setOut(ps);
    }
    @Test
    void test_play_one_turn_miss() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        //fire at b0, it's a miss
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "b0\n", bytes, "A");
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<Character>(3,5,'X');
        BoardTextView enemyView = new BoardTextView(enemyBoard);
        V1ShipFactory sf = new V1ShipFactory();
        playerA.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        enemyBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        String prompt = "Player A's turn:\n";
        String expected =
                "     Your ocean             Player Yue's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A s|s|  A                A  | |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n";

        String expectedHit =
                "Please enter a coordinate:\n"+
                        "--------------------------------------------------------------------------------\n"+
                        "You missed!\n"+
                        "--------------------------------------------------------------------------------\n";
        System.setOut(new PrintStream(bytes));
        assertThrows(EOFException.class, ()->playerA.playOneRound("Yue", enemyBoard, enemyView));
        System.setOut(ps);
    }

    @Test
    void test_playOneTurn_lose() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        //initialize a condition:
        //playerA and PlayerB placed submarine on the same location.
        //playerA is hit (0,0) by B, left(0,1)not hit
        //playerB is hit (1,1) by A, its a miss
        //playerA is hit (0,1) bt B, player B win
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "b1\n", bytes, "A");
        TextPlayerV2 playerB = createTextPlayerV2(3, 5, "a1\n", bytes, "B");
        V1ShipFactory sf = new V1ShipFactory();
        playerA.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        playerB.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        playerA.theBoard.fireAt(new Coordinate(0,0));

        String prompt_A_turn = "Player A's turn:\n";
        String expected_A_turn =
                "     Your ocean             Player B's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A *|s|  A                A  | |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n";

        String expectedHit_A_turn ="Please enter a coordinate:\n"+
                "--------------------------------------------------------------------------------\n"+
                "You missed!\n"+
                "--------------------------------------------------------------------------------\n";

        String expected_B_turn =
                "Player B's turn:\n" +
                        "     Your ocean             Player A's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A s|s|  A                A s| |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "Please enter a coordinate:\n"+
                        "B win the game!\n";

        //assertTrue(playerA.playOneRound("B", playerB.theBoard, playerB.view));
        assertThrows(EOFException.class, ()->playerB.playOneRound("A", playerA.theBoard, playerA.view));
        //now, player A is lose, but enter into the playOneTurn
        //player A will do nothing, but get a false return
        assertThrows(EOFException.class,()->playerA.playOneRound("B", playerB.theBoard, playerB.view));
        System.setOut(new PrintStream(bytes));
        //assertEquals(prompt_A_turn + expected_A_turn + expectedHit_A_turn +expected_B_turn, bytes.toString());
        System.setOut(ps);
    }
    @Test
    void test_doPlacementPhase() throws IOException {
        //set out System.out
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps = System.out;
        System.setOut(new PrintStream(bytes));
        //run doPlacementPhase, get System.out
        TextPlayerV2 playerA = createTextPlayerV2_V2(10, 20, "A0V\nA1u\nA0v\nB0v\nA1V\nA2V\nA3V\nA4V\nA5u\nC5U\nE5U\nA8U\nf8v\nf8r\nt0v\nG5D\n", bytes, "A");
        playerA.doPlacementPhase();

        String expectedAns =
                "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "--------------------------------------------------------------------------------\n" +
                        "Player A: you are going to place the following ships (which are all\n" +
                        "rectangular). For each ship, type the coordinate of the upper left\n" +
                        "side of the ship, followed by either H (for horizontal) or V (for\n" +
                        "vertical).  For example M4H would place a ship horizontally starting\n" +
                        "at M4 and going to the right.  You have\n" +
                        "\n" +
                        "2 \"Submarines\" ships that are 1x2 \n" +
                        "3 \"Destroyers\" that are 1x3\n" +
                        "3 \"Battleships\" that are 1x4\n" +
                        "2 \"Carriers\" that are 1x6\n" +
                        "--------------------------------------------------------------------------------\n" +
                        "Player A where do you want to place a Submarine?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s| | | | | | | | |  A\n" +
                        "B s| | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Submarine?\n" +
                        "java.lang.IllegalArgumentException: The orientation for rectangle ship should be v or h!\n" +
                        "Player A where do you want to place a Submarine?\n" +
                        "That placement is invalid: the ship overlaps another ship.\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s| | | | | | | | |  A\n" +
                        "B s| | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Submarine?\n" +
                        "That placement is invalid: the ship overlaps another ship.\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s| | | | | | | | |  A\n" +
                        "B s| | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Submarine?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s| | | | | | | |  A\n" +
                        "B s|s| | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Destroyer?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d| | | | | | |  A\n" +
                        "B s|s|d| | | | | | |  B\n" +
                        "C  | |d| | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Destroyer?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d| | | | | |  A\n" +
                        "B s|s|d|d| | | | | |  B\n" +
                        "C  | |d|d| | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Destroyer?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| | | | |  A\n" +
                        "B s|s|d|d|d| | | | |  B\n" +
                        "C  | |d|d|d| | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Battleship?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| |b| | |  A\n" +
                        "B s|s|d|d|d|b|b|b| |  B\n" +
                        "C  | |d|d|d| | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Battleship?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| |b| | |  A\n" +
                        "B s|s|d|d|d|b|b|b| |  B\n" +
                        "C  | |d|d|d| |b| | |  C\n" +
                        "D  | | | | |b|b|b| |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Battleship?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| |b| | |  A\n" +
                        "B s|s|d|d|d|b|b|b| |  B\n" +
                        "C  | |d|d|d| |b| | |  C\n" +
                        "D  | | | | |b|b|b| |  D\n" +
                        "E  | | | | | |b| | |  E\n" +
                        "F  | | | | |b|b|b| |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Carrier?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| |b| |c|  A\n" +
                        "B s|s|d|d|d|b|b|b|c|  B\n" +
                        "C  | |d|d|d| |b| |c|c C\n" +
                        "D  | | | | |b|b|b|c|c D\n" +
                        "E  | | | | | |b| | |c E\n" +
                        "F  | | | | |b|b|b| |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Carrier?\n" +
                        "java.lang.IllegalArgumentException: The orientation of Zship should be u or r or d or l\n" +
                        "Player A where do you want to place a Carrier?\n" +
                        "That placement is invalid: the ship goes off the right of the board.\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| |b| |c|  A\n" +
                        "B s|s|d|d|d|b|b|b|c|  B\n" +
                        "C  | |d|d|d| |b| |c|c C\n" +
                        "D  | | | | |b|b|b|c|c D\n" +
                        "E  | | | | | |b| | |c E\n" +
                        "F  | | | | |b|b|b| |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "K  | | | | | | | | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "Player A where do you want to place a Carrier?\n" +
                        "java.lang.IllegalArgumentException: The orientation of Zship should be u or r or d or l\n" +
                        "Player A where do you want to place a Carrier?\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n" +
                        "A s|s|d|d|d| |b| |c|  A\n" +
                        "B s|s|d|d|d|b|b|b|c|  B\n" +
                        "C  | |d|d|d| |b| |c|c C\n" +
                        "D  | | | | |b|b|b|c|c D\n" +
                        "E  | | | | | |b| | |c E\n" +
                        "F  | | | | |b|b|b| |  F\n" +
                        "G  | | | | |c| | | |  G\n" +
                        "H  | | | | |c|c| | |  H\n" +
                        "I  | | | | |c|c| | |  I\n" +
                        "J  | | | | | |c| | |  J\n" +
                        "K  | | | | | |c| | |  K\n" +
                        "L  | | | | | | | | |  L\n" +
                        "M  | | | | | | | | |  M\n" +
                        "N  | | | | | | | | |  N\n" +
                        "O  | | | | | | | | |  O\n" +
                        "P  | | | | | | | | |  P\n" +
                        "Q  | | | | | | | | |  Q\n" +
                        "R  | | | | | | | | |  R\n" +
                        "S  | | | | | | | | |  S\n" +
                        "T  | | | | | | | | |  T\n" +
                        "  0|1|2|3|4|5|6|7|8|9  \n";
        assertEquals(expectedAns, bytes.toString());
        System.setOut(ps);
    }

    @Test
    void test_readChoice_F() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2(3,5,"F\n",bytes,"A");
        Character c = playerA.readChoice();
        assertEquals('F', c);
    }
    @Test
    void test_readChoice_M_2_remains() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2(3,5,"M\n",bytes,"A");
        Character c = playerA.readChoice();
        assertEquals('M', c);
        assertEquals(3, playerA.moveRemain);
    }
    @Test
    void test_readChoice_S_2_remains() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2(3,5,"s\n",bytes,"A");
        Character c = playerA.readChoice();
        assertEquals('S', c);
        assertEquals(3, playerA.sonarRemain);
    }
    @Test
    void test_readChoice_S_failed() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2_V2_new(3,5,"s\nf\n",bytes,"A", 0, 3);
        playerA.readChoice();
        //assertEquals(0, playerA.sonarRemain);
        String expected =
                "Please enter into your choice:\n"+
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "\n" +
                        " F Fire at a square\n" +
                        " M Move a ship to another square (3 remaining)\n" +
                        " S Sonar scan (0 remaining)\n" +
                        "\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "\n";
        System.setOut(new PrintStream(bytes));
        assertEquals(expected, bytes.toString());
        System.setOut(ps);
    }

    @Test
    void test_readChoice_M_failed() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2_V2_new(3,5,"m\nf\n",bytes,"A", 2, 0);
        playerA.readChoice();
        //assertEquals(0, playerA.sonarRemain);
        String expected =
                "Please enter into your choice:\n"+
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "\n" +
                        " F Fire at a square\n" +
                        " M Move a ship to another square (0 remaining)\n" +
                        " S Sonar scan (2 remaining)\n" +
                        "\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "\n";
        System.setOut(new PrintStream(bytes));
        assertEquals(expected, bytes.toString());
        System.setOut(ps);
    }

    @Test
    void test_playFire_hit() throws  IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "F\na0\n", bytes, "A");
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<Character>(3,5,'X');
        BoardTextView enemyView = new BoardTextView(enemyBoard);
        V1ShipFactory sf = new V1ShipFactory();
        playerA.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        enemyBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        String prompt = "Player A's turn:\n";
        String expected =
                "     Your ocean             Player Yue's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A s|s|  A                A  | |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n";

        String expectedHit =
                "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "\n" +
                        " F Fire at a square\n" +
                        " M Move a ship to another square (3 remaining)\n" +
                        " S Sonar scan (3 remaining)\n" +
                        "\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "\n" +
                        "Please enter into your choice:\n" +
                        "Please enter a coordinate:\n"+
                        "--------------------------------------------------------------------------------\n"+
                        "You hit a Submarine!\n"+
                        "--------------------------------------------------------------------------------\n";
        playerA.playOneRound("Yue", enemyBoard, enemyView);
        System.setOut(new PrintStream(bytes));
        assertEquals(prompt + expected + expectedHit, bytes.toString());
        System.setOut(ps);
    }

    @Test
    void test_playFire_miss() throws  IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "F\ne0\n", bytes, "A");
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<Character>(3,5,'X');
        BoardTextView enemyView = new BoardTextView(enemyBoard);
        V1ShipFactory sf = new V1ShipFactory();
        playerA.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        enemyBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        String prompt = "Player A's turn:\n";
        String expected =
                "     Your ocean             Player Yue's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A s|s|  A                A  | |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n";

        String expectedHit =
                "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "\n" +
                        " F Fire at a square\n" +
                        " M Move a ship to another square (3 remaining)\n" +
                        " S Sonar scan (3 remaining)\n" +
                        "\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "\n" +
                        "Please enter into your choice:\n" +
                        "Please enter a coordinate:\n" +
                        "--------------------------------------------------------------------------------\n"+
                        "You missed!\n"+
                        "--------------------------------------------------------------------------------\n";
        playerA.playOneRound("Yue", enemyBoard, enemyView);
        System.setOut(new PrintStream(bytes));
        assertEquals(prompt + expected + expectedHit, bytes.toString());
        System.setOut(ps);
    }
    @Test
    void test_playFire_you_win() throws  IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps =System.out;
        TextPlayerV2 playerA = createTextPlayerV2(3, 5, "F\na1\n", bytes, "A");
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<Character>(3,5,'X');
        BoardTextView enemyView = new BoardTextView(enemyBoard);
        V1ShipFactory sf = new V1ShipFactory();
        playerA.theBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        enemyBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
        enemyBoard.fireAt(new Coordinate(0,0));
        String prompt = "Player A's turn:\n";
        String expected =
                "     Your ocean             Player Yue's ocean\n" +
                        "  0|1|2                    0|1|2  \n" +
                        "A s|s|  A                A s| |  A\n" +
                        "B  | |  B                B  | |  B\n" +
                        "C  | |  C                C  | |  C\n" +
                        "D  | |  D                D  | |  D\n" +
                        "E  | |  E                E  | |  E\n" +
                        "  0|1|2                    0|1|2  \n";

        String expectedHit =
                "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "\n" +
                        " F Fire at a square\n" +
                        " M Move a ship to another square (3 remaining)\n" +
                        " S Sonar scan (3 remaining)\n" +
                        "\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "\n" +
                        "Please enter into your choice:\n" +
                        "Please enter a coordinate:\n"+
                        "A win the game!\n";
        playerA.playOneRound("Yue", enemyBoard, enemyView);
        System.setOut(new PrintStream(bytes));
        assertEquals(prompt + expected + expectedHit, bytes.toString());
        System.setOut(ps);
    }

    @Test
    void test_play_one_round_with_fire_and_move() throws IOException{
        //set out System.out
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps = System.out;
        System.setOut(new PrintStream(bytes));
        //run doPlacementPhase, get System.out
        TextPlayerV2 playerA = createTextPlayerV2_V2(10, 20,"M\nE2\nc5R" , bytes, "A");
        V2ShipFactory sf = new V2ShipFactory();
        BattleShipBoard<Character> enemyBorad = new BattleShipBoard<Character>(10,20,'X');
        BoardTextView enemyView = new BoardTextView(enemyBorad);
        playerA.theBoard.tryAddShip(sf.makeCarrier(new Placement("a1u")));
        playerA.theBoard.fireAt(new Coordinate("d1"));
        playerA.theBoard.fireAt(new Coordinate("c2"));
        assertTrue(playerA.playOneRound("B", enemyBorad, enemyView));
        System.setOut(ps);
    }

    @Test
    void test_PlaySonar() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayerV2 playerA = createTextPlayerV2_V2_new(10,20,"a0\n",bytes, "A",3,3);
        BattleShipBoard<Character> enemyBorad = new BattleShipBoard<Character>(10,20,'X');
        BoardTextView enemyView = new BoardTextView(enemyBorad);
        V2ShipFactory sf = new V2ShipFactory();
        enemyBorad.tryAddShip(sf.makeSubmarine(new Placement("a0v")));
        enemyBorad.tryAddShip(sf.makeSubmarine(new Placement("a1v")));
        enemyBorad.tryAddShip(sf.makeDestroyer(new Placement("a2v")));
        enemyBorad.tryAddShip(sf.makeDestroyer(new Placement("a3v")));
        enemyBorad.tryAddShip(sf.makeBattleship(new Placement("c1r")));
        playerA.playSonar(enemyBorad);
        System.setOut(new PrintStream(bytes));
        String s = "Please enter a coordinate:\n" +
                "---------------------------------------------------------------------------\n" +
                "Submarines occupy 4 squares\n" +
                "Destroyers occupy 3 squares\n" +
                "Battleships occupy 1 squares\n" +
                "Carriers occupy 0 square\n" +
                "---------------------------------------------------------------------------\n";
        assertEquals(s, bytes.toString());
    }










}