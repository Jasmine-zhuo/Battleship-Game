package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  @Test
  /**
   *test BattleShipBoard constructor
   *and invalid input (width and height should be >= 0)
   */
  public void test_width_and_height() {
    BattleShipBoard<Character> b1 =new BattleShipBoard<Character>(1, 2, 'X');
    assertEquals(b1.getWidth(), 1);
    assertEquals(b1.getHeight(), 2);
    Board<Character> b2 = new BattleShipBoard<Character>(10, 20, 'X');
    assertEquals(10, b2.getWidth());
    assertEquals(20, b2.getHeight());
  }
  @Test
  public void test_invalid_dimensions(){
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5, 'X'));
  }
  /**the board's current state matches what is expected at every coordinate
   */
  private <T> void checkWhatIsAtBoard(BattleShipBoard<T> b, T[][] expected){
    for(int i = 0; i < expected.length; ++i){
      for(int j = 0; j < expected[i].length; ++j){
        assertEquals(b.whatIsAtForSelf(new Coordinate(i, j)), expected[i][j], "Mismatch at coordinate: (" + i + "," + j + ")");
      }
    }
  }
  @Test
  public void test_add_ship_to_board(){
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10, 'X');
    Character[][] expected = new Character[10][10]; // Empty board
    checkWhatIsAtBoard(board, expected);//check empty board
    Coordinate coord = new Coordinate(2, 3);
    Ship<Character> ship = new RectangleShip<Character>(coord, 's', '*'); // Assuming RectangleShip is a simple Ship implementation
    assertNull(board.tryAddShip(ship), "Ship should be placed successfully");
    expected[2][3] = 's'; // Mark where the ship is
    checkWhatIsAtBoard(board, expected);
    
  }
  @Test
  public void test_add_multiple_ship_to_board(){
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10, 'X');
    Ship<Character> ship1 = new RectangleShip<Character>(new Coordinate(1, 1), 's', '*');
    Ship<Character> ship2 = new RectangleShip<Character>(new Coordinate(1, 2), 's', '*');
    Ship<Character> ship3 = new RectangleShip<Character>(new Coordinate(1, 3), 's', '*');   

    assertNull(board.tryAddShip(ship1));
    assertNull(board.tryAddShip(ship2));
    assertNull(board.tryAddShip(ship3));

    Character[][] expected = new Character[10][10];
    expected[1][1] = 's';
    expected[1][2] = 's';
    expected[1][3] = 's';
    checkWhatIsAtBoard(board, expected);
  }
  @Test
  public void testTryAddShipReturnsFalseForInvalidPlacement() {
    // Setup a board with specific dimensions and placement rules
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    
    // Place a ship to occupy a specific location
    Placement validPlacement = new Placement(new Coordinate(1, 1), 'V');
    Ship<Character> firstShip = shipFactory.makeDestroyer(validPlacement);
    assertNull(board.tryAddShip(firstShip), "First ship should be placed successfully.");
    
    // Attempt to place another ship that collides with the first ship
    Placement collidingPlacement = new Placement(new Coordinate(1, 1), 'V');
    Ship<Character> collidingShip = shipFactory.makeSubmarine(collidingPlacement);

    assertNotNull(board.tryAddShip(collidingShip), "Colliding ship should not be placed successfully.");
  }
  @Test
  public void testFireAtHit(){
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    Ship<Character> destroyer = shipFactory.makeDestroyer(new Placement(new Coordinate(2, 2), 'V'));//destroyer at (2, 2), (3, 2), (4, 2)
    Ship<Character> submarine = shipFactory.makeSubmarine(new Placement(new Coordinate(4, 4), 'H'));//submarine at (4, 4), (4, 5)
    board.tryAddShip(submarine);
    board.tryAddShip(destroyer);
    Ship<Character> hitShip = board.fireAt(new Coordinate(2, 2));
    assertSame(destroyer, hitShip, "The hit ship should be the same as the destroyer placed on the board.");
    assertTrue(hitShip.wasHitAt(new Coordinate(2, 2)), "The ship should record the hit");
    assertFalse(hitShip.isSunk(), "The destroyer should not be sunk with 1 hit");
    Ship<Character> misShip = board.fireAt(new Coordinate(0, 0));
    assertNull(misShip, "No ship should be hit at an empty coordinte.");
    assertTrue(board.getEnemyMisses().contains(new Coordinate(0, 0)));

    board.fireAt(new Coordinate(4, 4));
    board.fireAt(new Coordinate(4, 5));
    assertTrue(submarine.isSunk(), "The submarine should be sunk after two hits");
  }
  @Test
  public void testWhatIsAtForEnemy(){
    BattleShipBoard<Character> board = new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    Ship<Character> destroyer = shipFactory.makeDestroyer(new Placement(new Coordinate(2, 2), 'V'));//destroyer at (2, 2), (3, 2), (4, 2)
    board.tryAddShip(destroyer);
    board.fireAt(new Coordinate(2, 2));
    board.fireAt(new Coordinate(0, 0));
    // Test hit ship displays as 'D'
    assertEquals('d', board.whatIsAtForEnemy(new Coordinate(2, 2)));
    // Test miss displays as 'X'
    assertEquals('X', board.whatIsAtForEnemy(new Coordinate(0, 0)));
    // Test unhit ship part displays as blank 
    assertNull(board.whatIsAtForEnemy(new Coordinate(3, 2)));
    // Test empty square displays as blank
    assertNull(board.whatIsAtForEnemy(new Coordinate(1, 1)));
  }
  @Test
  public void testAllShipSunk(){
    BattleShipBoard<Character> board = new BattleShipBoard<Character>(10, 10, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> ship1 = factory.makeDestroyer(new Placement("A0V"));
    Ship<Character> ship2 = factory.makeSubmarine(new Placement("D3H"));
    board.tryAddShip(ship1);
    board.tryAddShip(ship2);
    // Simulate sinking both ships
    for (Coordinate c : ship1.getCoordinates()) {
      ship1.recordHitAt(c);
    }
    assertTrue(ship1.isSunk());
    assertFalse(ship2.isSunk());
    assertFalse(board.allShipsSunk());
    for (Coordinate c : ship2.getCoordinates()) {
      ship2.recordHitAt(c);
    }
    assertTrue(board.allShipsSunk(), "Expected all ships to be sunk.");
  }
}

