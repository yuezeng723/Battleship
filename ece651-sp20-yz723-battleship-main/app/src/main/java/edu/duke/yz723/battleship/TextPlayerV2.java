package edu.duke.yz723.battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

public class TextPlayerV2 {
    final String name;
    final Board<Character> theBoard;
    final BoardTextView view;
    final BufferedReader inputReader;
    final PrintStream out;
    final AbstractShipFactory<Character> shipFactory;
    final ArrayList<String> shipsToPlace;
    final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
    int moveRemain;
    int sonarRemain;
    boolean computer;//this is true for computer player
    final RandomGenerator randGenerator;

    /**
     * Mapping from the ship's name to it's creating function in shipFactory
     */
    protected void setupShipCreationMap() {
        shipCreationFns.put("Submarine", shipFactory::makeSubmarine);
        shipCreationFns.put("Destroyer", shipFactory::makeDestroyer);
        shipCreationFns.put("Battleship", shipFactory::makeBattleship);
        shipCreationFns.put("Carrier", shipFactory::makeCarrier);
    }

    /**
     * Contains all ships need to be created by their names with order
     */
    protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
    }

    /**
     * A constructor for test
     */
    public TextPlayerV2(boolean computer, String playerName, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, AbstractShipFactory<Character> sf, int sonarRemain, int moveRemain) {
        this(playerName, theBoard, inputSource, out, sf);
        this.sonarRemain = sonarRemain;
        this.moveRemain = moveRemain;
        this.computer = computer;
    }

    /**
     * Version 2 constructor
     * @param playerName is the name of player
     * @param theBoard is the board of player
     * @param inputSource is bufferedReader
     * @param out is printStream
     * @param sf is the V2ShipFactory
     */
    public TextPlayerV2(String playerName, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, AbstractShipFactory<Character> sf) {
        this.name = playerName;
        this.theBoard = theBoard;
        this.view = new BoardTextView(theBoard);
        this.inputReader = inputSource;
        this.out = out;
        this.shipFactory = sf;
        this.shipsToPlace = new ArrayList<>();
        this.shipCreationFns = new HashMap<>();
        setupShipCreationMap();
        setupShipCreationList();
        this.sonarRemain = 3;
        this.moveRemain = 3;
        this.randGenerator = new RandomGenerator();
    }

    /**
     * Read the placement from terminal typed in by player
     *
     * @param prompt is s prompt guiding the player with instruction information
     * @return Create an object basing on the string typed in on terminal
     * @throws EOFException "It is end of the file.\n"
     */
    public Placement readPlacement(String prompt) throws IOException {
        out.println(prompt);
        while (true) {
            String s = inputReader.readLine();
            try {
                if (s == null) {
                    throw new EOFException("It is end of the file.\n");
                }
                return new Placement(s);
            } catch (IllegalArgumentException e) {
                out.print(e);
                out.println(prompt);
            }
        }
    }

    /**
     * Read the coordinate typed in by player. If illegal, continue until correct one
     * @return parsed coordinate typed in by player
     * @throws IOException no specific sentence
     */
    public Coordinate readCoordinate() throws IOException {
        if (!isComputer()) {
            out.print("Please enter a coordinate:\n");
        }
        while (true) {
            String s = inputReader.readLine();
            try {
                return new Coordinate(s);
            } catch (IllegalArgumentException e) {
                out.print(e);
                if (!isComputer()) {
                    out.println("Please enter a valid coordinate:\n");
                }
            }
        }
    }

    /**
     * Read the possible action choice till the valid input
     * @return is the valid uppercase input e.g. 'F', 'M', 'S'
     * @throws IOException no specific sentence
     */
    public Character readChoice() throws IOException {
        if (!isComputer()) {
            out.print("Please enter into your choice:\n");
        }
        Character choice = null;
        while (true) {
            String s = inputReader.readLine();
            if (s == null){
                throw new EOFException("This is end of the file!\n");
            }
            try {
                char[] choices = s.toCharArray();
                choice = Character.toUpperCase(choices[0]);
                if (choice == 'S' && sonarRemain != 0) {
                    break;
                }
                if (choice == 'M' && moveRemain != 0) {
                    break;
                }
                if (choice == 'F') {
                    break;
                }
                if (!isComputer()) {
                    printActionsPrompt();
                }
            }catch (IndexOutOfBoundsException e){
                continue;
            }
        }
        return choice;
    }

    /**
     * Print the F and M and S guidance prompt in the playing phase
     */
    public void printActionsPrompt() {
        String s =
                "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player " + name + ":\n" +
                        "\n" +
                        " F Fire at a square\n" +
                        " M Move a ship to another square (" + moveRemain + " remaining)\n" +
                        " S Sonar scan (" + sonarRemain + " remaining)\n" +
                        "\n" +
                        "Player " + name + ", what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "\n";
        out.print(s);
    }

    /**
     * Make one placement during doPlacementPhase
     * @param shipName is to be-added ship's name
     * @param createFn is the param function applied for creating ship
     * @return is the boolean flag. True for successfully make this placement
     * @throws IOException no specific sentence
     */
    public boolean doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        Placement p = randGenerator.getRandomPlacement(theBoard.getHeight(), theBoard.getWidth());
        if (!isComputer()) {
            p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
        }
        if (p != null) {
            try {
                Ship<Character> s = createFn.apply(p);
                String message = theBoard.tryAddShip(s);
                //if tryAddShip failed, display board again
                if (message != null) {
                    if (!checkLose()) {
                        out.println(message);
                        out.print(view.displayMyOwnBoard());
                    }
                    return false;
                }
                if (!isComputer()) {
                    out.print(view.displayMyOwnBoard());
                }
                return true;
            } catch (IllegalArgumentException e) {
                if (!isComputer()) {
                    out.print(e);
                }
                return false;
            }
        } else return false;
    }


    /**
     * Version 2. Invalid input handled
     * (a) display the starting (empty) board
     * (b) print the instructions message
     * (c) call doOnePlacement to place one ship
     * @throws IOException IO exception
     */
    public void doPlacementPhase() throws IOException {
        String line = "--------------------------------------------------------------------------------\n";
        String message =
                line +
                        "Player " + name + ": you are going to place the following ships (which are all\n" +
                        "rectangular). For each ship, type the coordinate of the upper left\n" +
                        "side of the ship, followed by either H (for horizontal) or V (for\n" +
                        "vertical).  For example M4H would place a ship horizontally starting\n" +
                        "at M4 and going to the right.  You have\n" +
                        "\n" +
                        "2 \"Submarines\" ships that are 1x2 \n" +
                        "3 \"Destroyers\" that are 1x3\n" +
                        "3 \"Battleships\" that are 1x4\n" +
                        "2 \"Carriers\" that are 1x6\n"
                        + line;
        //print empty board
        if (!isComputer()) {
            out.print(view.displayMyOwnBoard());
            out.print(message);
        }
        int placingTimes = shipsToPlace.size();
        int index = 0;
        while (placingTimes > 0) {
            if (doOnePlacement(shipsToPlace.get(index), shipCreationFns.get(shipsToPlace.get(index)))) {
                index++;
                placingTimes--;
            }
        }
    }

    /**
     * Check if this player lose.
     * @return true for lose; false for not lose
     */
    public boolean checkLose() {
        return this.theBoard.checkLose();
    }

    /**
     * Play phase making fire action. Print prompt and update board's state
     * @param enemyBoard is the Board of enemy
     * @return is false the game end
     * @throws IOException is the IOException
     */
    public boolean playFire(Board<Character> enemyBoard) throws IOException {
        Coordinate hitTo = readCoordinate();
        Ship<Character> enemyShip = enemyBoard.fireAt(hitTo);
        if (!enemyBoard.checkLose()) {
            String segLine = "--------------------------------------------------------------------------------\n";
            if (enemyShip != null) {
                if (!isComputer()) {
                    out.print(segLine + "You hit a " + enemyShip.getName() + "!\n" + segLine);
                }else out.print(segLine + "Player" + name +" hit a " + enemyShip.getName() + "!\n" + segLine);
                return true;
            } else {
                if (!isComputer()) {
                    out.print(segLine + "You missed!\n" + segLine);
                }
                return true;
            }
        } else {
            if (!isComputer()) {
                out.print(this.name + " win the game!\n");
            }
            return false;
        }
    }

    /**
     * Version 2
     * @param shipToMove is the ship wait to move
     * @param p is the placement
     * @return true for successfully make on movement
     * @throws IOException no specific sentence
     */
    public boolean doMovement(Ship<Character> shipToMove, Placement p) throws IOException {
        theBoard.getAllShips().remove(shipToMove);
        Ship<Character> newShip = shipCreationFns.get(shipToMove.getName()).apply(p);
        String info = theBoard.tryAddShip(newShip);
        if (info == null) {
            //fire the coordinate if the old ship contains hit
            if (shipToMove.checkShipHit()) {
                HashSetFactory hashFactory = new HashSetFactory(shipToMove, newShip);
                HashSet<Coordinate> waitToFire = hashFactory.creatNewHitSet();
                for (Coordinate it : waitToFire) {
                    theBoard.fireAt(it);
                }
            }
            return true;
        } else {
            theBoard.tryAddShip(shipToMove);
            return false;
        }
    }

    /**
     * Version 2
     * Read the placement typed in by player during "doAttackPhase"
     * @return is the placement object.
     * @throws IOException "It is end of the file.\n"
     */
    public Placement readMovePlacement() throws IOException {
        if (!isComputer()) {
            out.print("Please enter a placement:\n");
        }
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("It is end of the file.\n");
        }
        Placement p = new Placement(s);
        return p;
    }

    /**
     * Version 2
     * Handle all tasks after the movement action, call readCoordinate,
     * call do Movement, passing flag towards playMove
     * @return true for playMove action successfully made
     * @throws IOException nothing
     */
    public boolean playMove(Board<Character> enemyBoard) throws IOException {
        Coordinate selectCoord = readCoordinate();
        Ship<Character> shipToMove = theBoard.getShip(selectCoord);
        if (shipToMove == null) {
            return false;
        }
        try {
            Placement p = readMovePlacement();
            if (p == null) {
                return false;
            }
            if (doMovement(shipToMove, p)) {
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Version 2
     * Get all the  25 coordinates with the given center coordinate
     * @param center is the graphical center of the sonar.
     * Including the coordinate out of the bond
     * @return is the hashset holding all square ship coordinates
     */
    public HashSet<Coordinate> getSonarSquare(Coordinate center) {
        int row = center.getRow();
        int column = center.getColumn();
        HashSet<Coordinate> sonarArea = new HashSet<>();
        for (int r = row - 2; r < row + 3; r++) {
            for (int c = column - 1; c < column + 2; c++) {
                sonarArea.add(new Coordinate(r, c));
            }
        }
        sonarArea.add(new Coordinate(row, column - 3));
        sonarArea.add(new Coordinate(row, column - 2));
        sonarArea.add(new Coordinate(row, column + 2));
        sonarArea.add(new Coordinate(row, column + 3));
        sonarArea.add(new Coordinate(row + 3, column));
        sonarArea.add(new Coordinate(row - 3, column));
        sonarArea.add(new Coordinate(row - 1, column - 2));
        sonarArea.add(new Coordinate(row - 1, column + 2));
        sonarArea.add(new Coordinate(row + 1, column - 2));
        sonarArea.add(new Coordinate(row + 1, column + 2));
        return sonarArea;
    }

    /**
     * Traverse the hashset of sonar area coords and get an array
     * array[0] holds submarine's number;
     * array[1] holds destroyer's number;
     * array[2] holds battleship's number;
     * array[3] holds carrier's number;
     * @param center is the center of the sonar area
     * @param enemyBoard is the enemy's borad
     * @return is the int array holding ship coords' number
     */
    public int[] getSonarInfo(Coordinate center, Board<Character> enemyBoard) {
        HashSet<Coordinate> sonarArea = getSonarSquare(center);
        int[] sonarOut = new int[4];
        Arrays.fill(sonarOut, 0);//initialize all to 0
        for (Coordinate it : sonarArea) {
            Ship<Character> ship = enemyBoard.getShip(it);
            if (ship != null) {
                if (ship.occupiesCoordinates(it)) {
                    if (ship.getName() == "Submarine") {
                        sonarOut[0]++;
                    }
                    if (ship.getName() == "Destroyer") {
                        sonarOut[1]++;
                    }
                    if (ship.getName() == "Battleship") {
                        sonarOut[2]++;
                    }
                    if (ship.getName() == "Carrier") {
                        sonarOut[3]++;
                    }
                }
            }
        }
        return sonarOut;
    }

    /**
     * Version 2
     * @param enemyBoard is the enmy's board
     * @throws IOException no specific sentence
     */
    public void playSonar(Board<Character> enemyBoard) throws IOException {
        Coordinate c = readCoordinate();
        int[] sonarOutPut = getSonarInfo(c, enemyBoard);
        String sonarPrint =
                "---------------------------------------------------------------------------\n" +
                        "Submarines occupy " + sonarOutPut[0] + " squares\n" +
                        "Destroyers occupy " + sonarOutPut[1] + " squares\n" +
                        "Battleships occupy " + sonarOutPut[2] + " squares\n" +
                        "Carriers occupy " + sonarOutPut[3] + " square\n" +
                        "---------------------------------------------------------------------------\n";
        if (!isComputer()) {
            out.print(sonarPrint);
        }
    }

    /**
     * Version 2 playOneRound of the game
     * @param enemyName is enemy's name
     * @param enemyBoard is enemy's borad
     * @param enemyView is enemy's board's view
     * @return is true for game not end
     * @throws IOException no specific sentence
     */
    public boolean playOneRound(String enemyName, Board<Character> enemyBoard, BoardTextView enemyView) throws IOException {
        if (!checkLose()) {
            if (!isComputer()) {
                String prompt = "Player " + this.name + "'s turn:\n";
                out.print(prompt);
                String myHeader = "Your ocean";
                String enemyHeader = "Player " + enemyName + "'s ocean";
                out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
            }
            while (true) {
                //fire
                Character yourChoice;
                if (!isComputer()) {
                    printActionsPrompt();
                    yourChoice = readChoice();
                } else {
                    yourChoice = randGenerator.getRandChar();
                }
                if (yourChoice == 'F') {
                    return playFire(enemyBoard);
                }
                if (yourChoice == 'M' && moveRemain > 0) {
                    //若移动失败,继续选while的FMS选择循环。 若成功，返回true
                    if (!playMove(enemyBoard)) {
                        continue;
                    }
                    moveRemain--;
                    return true;
                }
                if (yourChoice == 'S' && sonarRemain > 0) {
                    playSonar(enemyBoard);
                    sonarRemain--;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the state of the computer.
     * @return true for player is computer
     */
    public boolean isComputer() {
        return computer;
    }

    /**
     * Read character typed in by player
     * @return the upper case character
     * @throws IOException "It is the end of the file!\n"
     */
    String readCharacter() throws IOException {
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("It is the end of the file!\n");
        }
        return s;
    }

    /**
     * Set up robort mode or human mode
     * @throws IOException "Please type in a valid input!\n"
     */
    public void doRobotChosePhase() throws IOException {
        out.print("What player do you want to play with? Want Computer hit C, want human hit H\n");
        String choice = readCharacter().toUpperCase();
        if (choice.equals("C") || choice.equals("c")) {
            computer = true;
        } else if (choice.equals("H") || choice.equals("h")) {
            computer = false;
        } else
            throw new IOException("Please type in a valid input!\n");
    }

}
