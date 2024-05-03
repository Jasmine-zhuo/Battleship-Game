package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/** Helper function for empty board test
 *@param (int)width and height of board,(String)expectedHeader and Body 
 */

public class BoardTextViewTest {
  private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedBody){
    Board<Character> b1 = new BattleShipBoard<Character>(w, h, 'X');
    BoardTextView view = new BoardTextView(b1);
    assertEquals(expectedHeader, view.makeHeader());
    String expected = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }
  @Test
  public void test_display_empty_2by2() {
    String expectedHeader = "  0|1\n";
    String expectedBody =
      "A  |  A\n"+
      "B  |  B\n";
    emptyBoardHelper(2, 2, expectedHeader, expectedBody);
  }
  @Test
  public void test_display_empty_10by20(){
    String expectedHeader =  "  0|1|2|3|4|5|6|7|8|9\n";
    String expectedBody =
      "A  | | | | | | | | |  A\n" +
      "B  | | | | | | | | |  B\n"+
      "C  | | | | | | | | |  C\n" +
      "D  | | | | | | | | |  D\n" +
      "E  | | | | | | | | |  E\n" +
      "F  | | | | | | | | |  F\n" +
      "G  | | | | | | | | |  G\n" +
      "H  | | | | | | | | |  H\n"+
      "I  | | | | | | | | |  I\n" +
      "J  | | | | | | | | |  J\n" +
      "K  | | | | | | | | |  K\n"+
      "L  | | | | | | | | |  L\n" +
      "M  | | | | | | | | |  M\n" +
      "N  | | | | | | | | |  N\n" +
      "O  | | | | | | | | |  O\n" +
      "P  | | | | | | | | |  P\n" +
      "Q  | | | | | | | | |  Q\n" +
      "R  | | | | | | | | |  R\n" +
      "S  | | | | | | | | |  S\n" +
      "T  | | | | | | | | |  T\n";
    emptyBoardHelper(10, 20, expectedHeader, expectedBody);
  }
  @Test
    public void test_display_empty_3by2() {
    String expectedHeader = "  0|1|2\n";
    String expectedBody =
      "A  | |  A\n"+
      "B  | |  B\n";
    emptyBoardHelper(3, 2, expectedHeader, expectedBody);
  }
   @Test
    public void test_display_empty_3by5() {
     String expectedHeader = "  0|1|2\n";
     String expectedBody =
       "A  | |  A\n"+
       "B  | |  B\n"+
       "C  | |  C\n"+
       "D  | |  D\n"+
       "E  | |  E\n";
     emptyBoardHelper(3, 5, expectedHeader, expectedBody);
 
  }
  /**
     test invalid board size, w>10 or height>26
   */
  @Test
  public void test_invalid_board_size(){
    Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20, 'X');
    Board<Character> tallBoard = new BattleShipBoard<Character>(10, 27, 'X');
    assertThrows(IllegalArgumentException.class, ()-> new BoardTextView(wideBoard));
    assertThrows(IllegalArgumentException.class, ()->new BoardTextView(tallBoard));
  }

  @Test
  public void test_displayMyOwnBoard_with_ships() {
    BattleShipBoard<Character> board = new BattleShipBoard<>(4, 3, 'X');//width, height
    BoardTextView view = new BoardTextView(board);
    Coordinate coord1 = new Coordinate(2, 3);//row, column
    Ship<Character> ship1 = new RectangleShip<>(coord1, 's', '*');
    board.tryAddShip(ship1);
    
    String expected = 
      "  0|1|2|3\n" +
      "A  | | |  A\n" +
      "B  | | |  B\n" +
      "C  | | |s C\n" +
      "  0|1|2|3\n";
    
    assertEquals(expected, view.displayMyOwnBoard());
  }
   @Test
  public void test_displayMyOwnBoard_with_multiple_ships() {
     BattleShipBoard<Character> board = new BattleShipBoard<>(4, 3, 'X');//width, height
    BoardTextView view = new BoardTextView(board);
    Coordinate coord1 = new Coordinate(0, 0);//row, column
    Ship<Character> ship1 = new RectangleShip<Character>(coord1, 's', '*');
    Ship<Character> ship2 = new RectangleShip<Character>(new Coordinate(1, 0), 's', '*');
    Ship<Character> ship3 = new RectangleShip<Character>(new Coordinate(2, 3), 's', '*');
    board.tryAddShip(ship1);
    board.tryAddShip(ship2);
    board.tryAddShip(ship3);
    
    String expected = 
      "  0|1|2|3\n" +
      "A s| | |  A\n" +
      "B s| | |  B\n" +
      "C  | | |s C\n" +
      "  0|1|2|3\n";
    
    assertEquals(expected, view.displayMyOwnBoard());
  }
  @Test
public void testDisplayEnemyBoard() {
    // Setup
    Board<Character> board = new BattleShipBoard<>(4, 4, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), 'X');
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> destroyer = factory.makeDestroyer(new Placement(new Coordinate(1, 0), 'H'));
    Ship<Character> submarine = factory.makeSubmarine(new Placement(new Coordinate(3, 0), 'H'));
    
    board.tryAddShip(destroyer);
    board.tryAddShip(submarine);
    
    // Simulate shots: a hit on the destroyer, a miss, and a hit on the submarine
    board.fireAt(new Coordinate(1, 0)); // Hit destroyer
    board.fireAt(new Coordinate(0, 0)); // Miss
    board.fireAt(new Coordinate(3, 0)); // Hit submarine
    
    BoardTextView view = new BoardTextView(board);
    
    // Expected view from enemy's perspective
    String expectedView =
      "  0|1|2|3\n" +
      "A X| | |  A\n" + // Miss at A0
      "B d| | |  B\n" + // Hit at B0 (destroyer)
      "C  | | |  C\n" +
      "D s| | |  D\n" + // Hit at D0 (submarine)
      "  0|1|2|3\n";
    
    // Act
    String actualView = view.displayEnemyBoard();
    
    // Assert
    assertEquals(expectedView, actualView, "The enemy board view should match the expected representation.");
  }
  @Test
public void testDisplayMyOwnBoard() {
    // Setup
    Board<Character> board = new BattleShipBoard<>(4, 4, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), 'X');
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> destroyer = factory.makeDestroyer(new Placement(new Coordinate(1, 0), 'H'));
    Ship<Character> submarine = factory.makeSubmarine(new Placement(new Coordinate(3, 0), 'H'));
    
    board.tryAddShip(destroyer);
    board.tryAddShip(submarine);

    // Simulate shots: a hit on the destroyer, a miss, and a hit on the submarine
    board.fireAt(new Coordinate(1, 0)); // Hit destroyer
    board.fireAt(new Coordinate(0, 0)); // Miss
    board.fireAt(new Coordinate(3, 0)); // Hit submarine

    BoardTextView view = new BoardTextView(board);
    
    // Expected view from the player's own perspective
    String expectedView =
      "  0|1|2|3\n" +
      "A  | | |  A\n" + // Miss is not shown on the player's own board
      "B *|d|d|  B\n" + // Destroyer with a hit marked at B0
      "C  | | |  C\n" +
      "D *|s| |  D\n" + // Submarine with a hit marked at D0
      "  0|1|2|3\n";
    
    // Act
    String actualView = view.displayMyOwnBoard();
    
    // Assert
    assertEquals(expectedView, actualView, "The player's own board view should match the expected representation.");
  }
  @Test
  public void testDisplayMyBoardWithEnemyNextToIt() {
    // Initialize player A's board and enemy's board
    Board<Character> playerABoard = new BattleShipBoard<>(10, 10, 'X');
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 10, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    //add ship to player's board
    Ship<Character> destroyer = factory.makeDestroyer(new Placement(new Coordinate(0, 1), 'H'));
    Ship<Character> submarine = factory.makeSubmarine(new Placement(new Coordinate(3, 0), 'H'));
    playerABoard.tryAddShip(submarine);
    playerABoard.tryAddShip(destroyer);
    //add ships to enemy's board
    Ship<Character> battleship = factory.makeBattleship(new Placement(new Coordinate(4, 3), 'H'));
    Ship<Character> carrier = factory.makeCarrier(new Placement(new Coordinate(6, 0), 'H'));
    enemyBoard.tryAddShip(battleship);
    enemyBoard.tryAddShip(carrier);
    Ship<Character> battleship2 = enemyBoard.fireAt(new Coordinate(4, 3));
    // Create BoardTextViews for both boards
    Ship<Character> carrier2 = enemyBoard.fireAt(new Coordinate(6, 3));
    BoardTextView playerAView = new BoardTextView(playerABoard);
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    assertSame(battleship, battleship2);
    assertSame(carrier, carrier2);
    // Display boards side by side
    String combinedView = playerAView.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean:", "Player B's ocean:");
    
    // Expected result string
    String expectedResult = "     Your ocean:                          Player B's ocean:\n" +
      "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
      "A  |d|d|d| | | | | |  A                A  | | | | | | | | |  A\n" +
      "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
      "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
      "D s|s| | | | | | | |  D                D  | | | | | | | | |  D\n" +
      "E  | | | | | | | | |  E                E  | | |b| | | | | |  E\n" +
      "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
      "G  | | | | | | | | |  G                G  | | |c| | | | | |  G\n" +
      "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
      "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
      "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
      "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
    
    assertEquals(expectedResult, combinedView, "The combined view should match the expected layout.");
  }  
  
}
