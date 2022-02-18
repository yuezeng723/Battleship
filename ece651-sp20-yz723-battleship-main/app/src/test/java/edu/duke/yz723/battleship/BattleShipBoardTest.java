package edu.duke.yz723.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class  BattleShipBoardTest {
  @Test
  public void test_width_and_height(){
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

  @Test
  public void test_invalid_dimensions() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20, 'X'));
  }


  private <T> void checkWhatIsAtBoard(BattleShipBoard<T> battleBoard, T[][] expected){
    for(int c = 0; c < battleBoard.getHeight(); c++){
      for(int r = 0; r < battleBoard.getWidth(); r++){
        if (expected[c][r] == null){
          assertNull(battleBoard.whatIsAtForSelf(new Coordinate(c,r)));
        }else{assertEquals(expected[c][r], battleBoard.whatIsAtForSelf(new Coordinate(c,r)));}
      }
    }
  }

  @Test
  public void test_check_shipList() {
    BattleShipBoard<Character> battleShipBoard = new BattleShipBoard<Character>(4, 3, 'X');
    //check the battleShipBoard is empty
    Character[][] expected = new Character[3][4];
    for (int c = 0; c < 3; c++) {
      for (int r = 0; r < 4; r++) {
        expected[c][r] = null;
      }
    }
    checkWhatIsAtBoard(battleShipBoard, expected);
  }

  @Test
  public void test_check_tryToAdd(){
    BattleShipBoard<Character> battleShipBoard = new BattleShipBoard<Character>(8, 5, 'X');
    Character[][]expected = new Character[5][8];
    //add ships to the battleShipBoard
    ///update the expected array
    expected[0][7] = 's';
    expected[4][2] = 's';
    expected[2][2] = 's';
    ///add new ships
    Coordinate c1 = new Coordinate(0, 7);
    Coordinate c2 = new Coordinate(4, 2);
    Coordinate c3 = new Coordinate(2, 2);
    BasicShip<Character> b1 = new RectangleShip<>(c1,'s', '*');
    BasicShip<Character> b2 = new RectangleShip<>(c2,'s', '*');
    BasicShip<Character> b3 = new RectangleShip<>(c3,'s', '*');
    ///check add successful
    assertNull(battleShipBoard.tryAddShip(b1));
    assertNull(battleShipBoard.tryAddShip(b2));
    assertNull(battleShipBoard.tryAddShip(b3));
    ///check add to correct place
    checkWhatIsAtBoard(battleShipBoard, expected);
    BasicShip<Character> b4 = new RectangleShip<>(c3,'s', '*');
    String expected1 = "That placement is invalid: the ship overlaps another ship.";
    assertEquals(expected1, battleShipBoard.tryAddShip(b4));
  }

  @Test
  void test_fireAt(){
    BattleShipBoard<Character> battleShipBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory sf = new V1ShipFactory();
    Ship<Character> des = sf.makeDestroyer(new Placement(new Coordinate(0,0),'v'));
    ///Ship<Character> bat = sf.makeSubmarine(new Placement(new Coordinate(0,1),'v'));
    battleShipBoard.tryAddShip(des);
    ///battleShipBoard.tryAddShip(bat);
    //not hit the ship
    assertNull(battleShipBoard.fireAt(new Coordinate(10,10)));
    //hit the ship des first time, check the reference pointing to the same ship
    assertSame(des, battleShipBoard.fireAt(new Coordinate(0,0)));
    //hit the ship des second time, check the reference pointing to the same ship
    assertSame(des, battleShipBoard.fireAt(new Coordinate(1,0)));
    //hit the ship des third time, check the reference pointing to the same ship
    assertSame(des, battleShipBoard.fireAt(new Coordinate(2,0)));
    //check ship des is sunk. The des should be sunk
    assertTrue(des.isSunk());
  }
  @Test
  void test_whatIsAtForEnemy_missInfo(){
    BattleShipBoard<Character> battleShipBoard = new BattleShipBoard<Character>(4,5,'X');
    Coordinate fireTo = new Coordinate(0,0);
    battleShipBoard.fireAt(fireTo);
    assertEquals('X', battleShipBoard.whatIsAtForEnemy(fireTo));
  }
  @Test
  void test_checkLose(){
    BattleShipBoard<Character> myBoard = new BattleShipBoard<Character>(3,4,'X');
    BattleShipBoard<Character> enemyBoard = new BattleShipBoard<Character>(3,4,'X');
    V1ShipFactory sf = new V1ShipFactory();
    myBoard.tryAddShip(sf.makeSubmarine(new Placement("a0h")));
    myBoard.tryAddShip(sf.makeSubmarine(new Placement("b0h")));
    myBoard.fireAt(new Coordinate(0,0));
    myBoard.fireAt(new Coordinate(0,1));
    myBoard.fireAt(new Coordinate(1,0));
    myBoard.fireAt(new Coordinate(1,1));
    assertTrue(myBoard.checkLose());
    enemyBoard.tryAddShip(sf.makeSubmarine(new Placement("a0v")));
    assertFalse(enemyBoard.checkLose());
  }
  @Test
  void test_get_ship(){
    BattleShipBoard<Character> myBoard = new BattleShipBoard<Character>(3,4,'X');
    V1ShipFactory sf = new V1ShipFactory();
    Ship<Character> ship1 = sf.makeSubmarine(new Placement("a0h"));
    myBoard.tryAddShip(ship1);
    assertNull(myBoard.getShip(new Coordinate(2,2)));
    assertEquals(ship1, myBoard.getShip(new Coordinate(0,1)));
  }
  @Test
  void test_getAllShips(){
    BattleShipBoard<Character> myBoard = new BattleShipBoard<Character>(3,4,'X');
    V1ShipFactory sf = new V1ShipFactory();
    Ship<Character> ship1 = sf.makeSubmarine(new Placement("a0h"));
    Ship<Character> ship2 = sf.makeSubmarine(new Placement("b0h"));
    Ship<Character> ship3 = sf.makeSubmarine(new Placement("c0h"));
    myBoard.tryAddShip(ship1);
    myBoard.tryAddShip(ship2);
    myBoard.tryAddShip(ship3);
    ArrayList<Ship<Character>> expected = new ArrayList<>();
    expected.add(ship1);
    expected.add(ship2);
    expected.add(ship3);
    assertEquals(expected, myBoard.getAllShips());
    assertSame(myBoard.myShips, myBoard.getAllShips());
  }

}
