package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {
  private BattleShipBoard<Character> board;
  private V1ShipFactory shipFactory;
  private Placement validPlacement;
  private Placement invalidPlacement;
  private Ship<Character> validShip;
  private Ship<Character> invalidShip;

  @BeforeEach
  public void setUp(){
    board = new BattleShipBoard<Character>(10, 10, new InBoundsRuleChecker<Character>(null), 'X');
    shipFactory = new V1ShipFactory();
      }
  @Test
  public void testCheckMyRule_ShipWithinBounds() {
    validPlacement = new Placement(new Coordinate(5, 5), 'V');
    validShip = shipFactory.makeDestroyer(validPlacement);
    InBoundsRuleChecker<Character> inBoundsChecker = new InBoundsRuleChecker<>(null);
    assertNull(inBoundsChecker.checkMyRule(validShip, board));
    
  }
  
  @Test
  public void testCheckMyRule_ShipOutOfBound() {
    invalidPlacement = new Placement(new Coordinate(9, 9), 'V'); // Assuming this placement would be out of bounds for a destroyer
    invalidShip = shipFactory.makeDestroyer(invalidPlacement);
    InBoundsRuleChecker<Character> inBoundsChecker = new InBoundsRuleChecker<>(null);
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", inBoundsChecker.checkMyRule(invalidShip, board));
  }

  @Test
  public void shipPlacementWithinBoundsReturnsNull(){
    Placement validPlacement = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> ship = shipFactory.makeSubmarine(validPlacement);
    PlacementRuleChecker<Character> inBoundsChecker = new InBoundsRuleChecker<>(null);
    
    //boolean result = inBoundsChecker.checkPlacement(ship, board);
    String error = inBoundsChecker.checkPlacement(ship, board);
    assertNull(error, "Expected the ship placement to be valid and within bounds");
  }
  
  @Test
  public void shipPlacementOutsideBoundsReturnsErrorMessage() {
    Placement invalidPlacement = new Placement(new Coordinate(1, 9), 'H');
    Ship<Character> ship = shipFactory.makeDestroyer(invalidPlacement);
    PlacementRuleChecker<Character> inBoundsChecker = new InBoundsRuleChecker<>(null);
    
    String error = inBoundsChecker.checkPlacement(ship, board);
    assertNotNull(error, "Expected the ship placement to be invalid as it is out of bounds");
    String expectedError = "That placement is invalid: the ship goes off the right of the board.";
    assertEquals(expectedError, error, "Expect a specific error message for an out-of-bound placement");
  }
  @Test
  public void shipPlacementOutsideLeftTopBound(){
    Placement invalidPlacement = new Placement(new Coordinate(2, -1), 'H');
    Placement invalidPlacement2 = new Placement(new Coordinate(-1, 5), 'V');
    Ship<Character> ship = shipFactory.makeDestroyer(invalidPlacement);
    Ship<Character> ship2 = shipFactory.makeBattleship(invalidPlacement2);
    PlacementRuleChecker<Character> inBoundsChecker = new InBoundsRuleChecker<>(null);
    String error = inBoundsChecker.checkPlacement(ship, board);
    String error2 = inBoundsChecker.checkPlacement(ship2, board);
    assertNotNull(error, "Expected the ship placement to be invalid as it is out of bounds");
    String expectedError = "That placement is invalid: the ship goes off the left of the board.";
    String expectedError2 ="That placement is invalid: the ship goes off the top of the board."; 
    assertEquals(expectedError, error, "Expect a specific error message for an out-of-bound placement");
    assertEquals(expectedError2, error2);
  }
}
