package edu.duke.yz723.battleship;

import java.io.*;
import java.util.*;
import java.util.function.Function;

/**
 * This is Version 1 TextPlayer
 */
public class TextPlayer {
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
    final RandomGenerator randGenerator;

    /**
     * Mapping from the ship's name to it's creating function in shipFactory
     */
    protected void setupShipCreationMap(){
        shipCreationFns.put("Submarine", shipFactory::makeSubmarine);
        shipCreationFns.put("Destroyer", shipFactory::makeDestroyer);
        shipCreationFns.put("Battleship", shipFactory::makeBattleship);
        shipCreationFns.put("Carrier", shipFactory::makeCarrier);
    }

    /**
     * Contains all ships need to be created by their names with order
     */
    protected void setupShipCreationList(){
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
    }

    /**
     * Constructor for test sonarRemain and moveRemain
     * @param playerName is this player's name
     * @param theBoard is this player's name
     * @param inputSource is the buffer reader
     * @param out is the printStream
     * @param sf is abstract ship factory. Use V1ShipFacoty
     * @param sonarRemain set the remainning number you want
     * @param moveRemain set the reamining number you want
     */
    public TextPlayer(String playerName, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, AbstractShipFactory<Character>sf, int sonarRemain, int moveRemain){
        this( playerName, theBoard, inputSource, out, sf);
        this.sonarRemain = sonarRemain;
        this.moveRemain = moveRemain;
    }

    /**
     * Constructor
     * @param playerName TextPlayer's name
     * @param theBoard TextPlayer's board
     * @param inputSource shared reader
     * @param out shared outStream
     */
    public TextPlayer( String playerName, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, AbstractShipFactory<Character>sf) {
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
     * @param prompt is s prompt guiding the player with instruction information
     * @return Create an object basing on the string typed in on terminal
     * @throws IOException "It is end of the file.\n"
     */
    public Placement readPlacement(String prompt) throws IOException {
        out.println(prompt);
        while(true){
            String s = inputReader.readLine();
            try{
                if (s == null){
                    throw new EOFException("It is end of the file.\n");
                }
                Placement p = new Placement(s);
                return p;
            }catch (IllegalArgumentException e){
                out.print(e);
                out.println(prompt);
                continue;
            }
        }
    }

    /**
     * Read the coordinate typed in by player. If illegal, continue until correct one
     * @return parsed coordinate typed in by player
     * @throws IOException no specific sentence
     */
    public Coordinate readCoordinate() throws IOException{
        out.print("Please enter a coordinate:\n");
        while (true){
            String s = inputReader.readLine();
            try{
                Coordinate hitTo = new Coordinate(s);
                return hitTo;
            }catch (IllegalArgumentException e){
                out.print(e);
                out.println("Please enter a valid coordinate:\n");
                continue;
            }
        }
    }

    /**
     * Read the possible action choice till the valid input
     * @return is the valid uppercase input e.g. 'F', 'M', 'S'
     * @throws IOException no specific sentence
     */
    public Character readChoice()throws  IOException{
        out.print("Please enter into your choice:\n");
        Character choice;
        while (true){
            String s = inputReader.readLine();
            char[] choices = s.toCharArray();
            choice = Character.toUpperCase(choices[0]);
            if (choice == 'S' && sonarRemain != 0){
                break;
            }
            if (choice == 'M' && moveRemain != 0){
                break;
            }
            if (choice == 'F'){
                break;
            }
            printActionsPrompt();
        }
        return choice;
    }

    /**
     * Print the F and M and S guidance prompt in the playing phase
     */
    public void printActionsPrompt(){
        String s =
                "---------------------------------------------------------------------------\n" +
                "Possible actions for Player "+name+":\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square ("+moveRemain+" remaining)\n" +
                " S Sonar scan ("+sonarRemain+" remaining)\n" +
                "\n" +
                "Player "+name+", what would you like to do?\n" +
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
        Placement p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
        if (p != null){
            try {
                Ship<Character> s = createFn.apply(p);
                String message = theBoard.tryAddShip(s);
                //if tryAddShip failed, display board again
                if (message != null) {
                    out.println(message);
                    out.print(view.displayMyOwnBoard());
                    return false;
                }
                out.print(view.displayMyOwnBoard());
                return true;
            }catch (IllegalArgumentException e){
                out.print(e);
                return false;}} else return false;
    }


    /**
     * Version 1 doPlacementPhase. Invalid input not handled here. But fixed in Version 2
     * (a) display the starting (empty) board
     * (b) print the instructions message
     * (c) call doOnePlacement to place one ship
     * @throws IOException IO exception
     */
    public void doPlacementPhase() throws IOException{
        String line =  "--------------------------------------------------------------------------------\n";
        String message =
               line+
                "Player "+ name +": you are going to place the following ships (which are all\n" +
                "rectangular). For each ship, type the coordinate of the upper left\n" +
                "side of the ship, followed by either H (for horizontal) or V (for\n" +
                "vertical).  For example M4H would place a ship horizontally starting\n" +
                "at M4 and going to the right.  You have\n" +
                "\n" +
                "2 \"Submarines\" ships that are 1x2 \n" +
                "3 \"Destroyers\" that are 1x3\n" +
                "3 \"Battleships\" that are 1x4\n" +
                "2 \"Carriers\" that are 1x6\n"
              +line;
        //print empty board
        out.print(view.displayMyOwnBoard());
        //print instruction message
        out.print(message);
        int placingTimes = shipsToPlace.size();
        int index = 0;
        while (placingTimes > 0){
            if (doOnePlacement(shipsToPlace.get(index),shipCreationFns.get(shipsToPlace.get(index)))){
                index++;
                placingTimes--;
            }
        }
    }

    /**
     * Check if this player lose.
     * @return true for lose; false for not lose
     */
    public boolean checkLose(){
        return this.theBoard.checkLose();
    }

    /**
     *Play one turn game and check to display win or lose
     * @param enemyName is enemy's name
     * @param enemyBoard is enemy's board
     * @param enemyView is enemy's text view
     * @return is true, the game not end. return false, game end
     * @throws IOException is ioException
     */
    public boolean playOneTurn( String enemyName, Board<Character> enemyBoard, BoardTextView enemyView) throws IOException{
        //if i don't lose
        if (!checkLose()) {
            String prompt = "Player " + this.name + "'s turn:\n";
            out.print(prompt);
            String myHeader = "Your ocean";
            String enemyHeader = "Player " + enemyName + "'s ocean";
            out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
            Coordinate hitTo = readCoordinate();
            Ship<Character> enemyShip = enemyBoard.fireAt(hitTo);
            if (!enemyBoard.checkLose()) {
                String segLine = "--------------------------------------------------------------------------------\n";
                if (enemyShip != null) {
                    out.print(segLine + "You hit a " + enemyShip.getName() + "!\n" + segLine);
                    return true;
                } else {
                    out.print(segLine + "You missed!\n" + segLine);
                    return true;
                }
            } else {
                out.print(this.name + " win the game!\n");
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Play phase making fire action
     * @param enemyBoard is the Board of enemy
     * @return is false the game end
     * @throws IOException is the IOException
     */
    public boolean playFire(Board<Character> enemyBoard) throws IOException{
        Coordinate hitTo = readCoordinate();
        Ship<Character> enemyShip = enemyBoard.fireAt(hitTo);
        if (!enemyBoard.checkLose()) {
            String segLine = "--------------------------------------------------------------------------------\n";
            if (enemyShip != null) {
                out.print(segLine + "You hit a " + enemyShip.getName() + "!\n" + segLine);
                return true;
            } else {
                out.print(segLine + "You missed!\n" + segLine);
                return true;
            }
        } else {
            out.print(this.name + " win the game!\n");
            return false;
        }
    }

    /**
     * Version 1.5 doMovement. Updated in Version 2
     * @param shipToMove is the ship wait to move
     * @param p is the placement
     * @return true for successfully make on movement
     * @throws IOException no specific sentence
     */
    public boolean doMovement(Ship<Character> shipToMove, Placement p) throws IOException{
        theBoard.getAllShips().remove(shipToMove);
        Ship<Character> newShip = shipCreationFns.get(shipToMove.getName()).apply(p);
        String info = theBoard.tryAddShip(newShip);
        if (info == null) {
            if (shipToMove.checkShipHit()){
                HashSetFactory hashFactory = new HashSetFactory(shipToMove, newShip);
                HashSet<Coordinate> waitToFire = hashFactory.creatNewHitSet();
                for (Coordinate it : waitToFire) {
                    theBoard.fireAt(it);
                }
            }
            moveRemain--;
            return true;
        } else {
            theBoard.tryAddShip(shipToMove);
            return false;
        }
    }

    /**
     * Version 1.5 readMovePlacement. Updated in Version 2
     * Read the placement typed in by player during "doAttackPhase"
     * @return is the placement object.
     * @throws IOException "It is end of the file.\n"
     */
    public Placement readMovePlacement() throws IOException{
        out.print("Please enter a placement:\n");
        String s = inputReader.readLine();
        if (s == null){throw new EOFException("It is end of the file.\n");
        }
        Placement p = new Placement(s);
        return p;
    }

    /**
     * Version 1.5 playMove, Updates in Version 2
     * Handle all tasks after the movement action, call readCoordinate,
     * call do Movement, passing flag towards playMove
     * @return true for playMove action successfully made
     * @throws IOException nothing
     */
    public boolean playMove() throws IOException{
        Coordinate selectCoord = readCoordinate();
        Ship<Character> shipToMove = theBoard.getShip(selectCoord);
        if (shipToMove == null){return false;
        }
        try{
            Placement p = readMovePlacement();
            if (p == null){
                return false;
            }
            if(doMovement(shipToMove, p)){
                return true;
            }
            return false;
        }catch (IOException e){
            return false;
        }
    }

    /**
     * Version 1.5
     * Get all the  25 coordinates with the given center coordinate
     * @param center is the graphical center of the sonar.
     * Including the coordinate out of the bond
     * @return is the hashset holding all square ship coordinates
     */
    public HashSet<Coordinate> getSonarSquare(Coordinate center){
        int row = center.getRow();
        int column = center.getColumn();
        HashSet<Coordinate> sonarArea = new HashSet<>();
        for (int r = row-2; r < row+3; r++){
            for (int c = column-1; c< column+2;c++){
                sonarArea.add(new Coordinate(r,c));
            }
        }
        sonarArea.add(new Coordinate(row, column-3));
        sonarArea.add(new Coordinate(row, column-2));
        sonarArea.add(new Coordinate(row, column+2));
        sonarArea.add(new Coordinate(row, column+3));
        sonarArea.add(new Coordinate(row+3, column));
        sonarArea.add(new Coordinate(row-3, column));
        sonarArea.add(new Coordinate(row-1, column-2));
        sonarArea.add(new Coordinate(row-1, column+2));
        sonarArea.add(new Coordinate(row+1, column-2));
        sonarArea.add(new Coordinate(row+1, column+2));
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
    public int[] getSonarInfo(Coordinate center, Board<Character> enemyBoard){
        HashSet<Coordinate> sonarArea = getSonarSquare(center);
        //construct an array. index 0 is submarine
        //1 is destroyer, 2 is battleship, 3 is carrier
        int[] sonarOut = new int[4];
        Arrays.fill(sonarOut, 0);//initialize all to 0
        for (Coordinate it : sonarArea){
            Ship<Character> ship = enemyBoard.getShip(it);
            if (ship != null ){
                if(ship.occupiesCoordinates(it)){
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
     * Version 1.5 updated in Version 2.0
     * @param enemyBoard is the enmy's board
     * @throws IOException no specific sentence
     */
    public void playSonar(Board<Character> enemyBoard) throws IOException{
        Coordinate c = readCoordinate();
        int[] sonarOutPut = getSonarInfo(c, enemyBoard);
        String sonarPrint =
                "---------------------------------------------------------------------------\n" +
                "Submarines occupy "+sonarOutPut[0]+" squares\n" +
                "Destroyers occupy "+sonarOutPut[1]+" squares\n" +
                "Battleships occupy "+sonarOutPut[2]+" squares\n" +
                "Carriers occupy "+sonarOutPut[3]+" square\n" +
                "---------------------------------------------------------------------------\n";
        out.print(sonarPrint);
        sonarRemain--;
    }

    /**
     * Version 1.5 playOneRound of the game without the robot.
     * @param enemyName is enemy's name
     * @param enemyBoard is enemy's borad
     * @param enemyView is enemy's board's view
     * @return is true for game not end
     * @throws IOException no specific sentence
     */
    public boolean playOneRound( String enemyName, Board<Character> enemyBoard, BoardTextView enemyView) throws IOException {
        if (!checkLose()) {
            String prompt = "Player " + this.name + "'s turn:\n";
            out.print(prompt);
            String myHeader = "Your ocean";
            String enemyHeader = "Player " + enemyName + "'s ocean";
            out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
            while(true) {
                printActionsPrompt();
                Character yourChoice = readChoice();
                if (yourChoice == 'F') {
                    return playFire(enemyBoard);
                }
                if (yourChoice == 'M' && moveRemain > 0) {
                    if (!playMove()){
                        continue;
                    }
                    return true;
                }
                if (yourChoice == 'S' && sonarRemain > 0) {
                    playSonar(enemyBoard);
                    return true;
                }
            }
        }
        return false;
    }

}
