package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.util.concurrent.ClosingFuture.Combiner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoCollisionRuleCheckerTest {
  private BattleShipBoard<Character> board;
  private V1ShipFactory shipFactory;
  private PlacementRuleChecker<Character> noCollisionRuleChecker;

  @BeforeEach
  public void setUp(){
    noCollisionRuleChecker = new NoCollisionRuleChecker<>(null);
    board = new BattleShipBoard<>(10, 10, noCollisionRuleChecker, 'X');
    shipFactory = new V1ShipFactory();
  }
  
  @Test
  public void test_no_collision() {
    Ship<Character> firstShip = shipFactory.makeDestroyer(new Placement("F0V"));
    assertNull(board.tryAddShip(firstShip), "Should successfully place the first ship");
    Ship<Character> secondShip = shipFactory.makeSubmarine(new Placement("E1V"));
    assertNull(noCollisionRuleChecker.checkMyRule(secondShip, board), "Should allow placing second ship without collision.");
  }
  @Test
  public void test_collision(){
    Ship<Character> firstShip = shipFactory.makeDestroyer(new Placement("F0V"));
    assertNull(board.tryAddShip(firstShip), "Should successfully place the first ship");
    Ship<Character> collidingShip = shipFactory.makeSubmarine(new Placement("F0V"));
    String result = noCollisionRuleChecker.checkMyRule(collidingShip, board);
    //assertNotNull(noCollisionRuleChecker.checkMyRule(collidingShip, board));
    String expectedError = "That placement is invalid: the ship overlaps another ship.";
    assertEquals(expectedError, result);
  }
  
  @Test
  public void testValidShipPlacement(){
    PlacementRuleChecker<Character> combinedChecker = new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null));
    
    Ship<Character> validShip = shipFactory.makeDestroyer(new Placement("B3V"));
    assertNull(combinedChecker.checkPlacement(validShip,board), "Ship should be placed within bounds and without collision.");
  }

  @Test
  public void testShipPlacementOutOfBounds(){
    PlacementRuleChecker<Character> combinedChecker = new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null));
    Ship<Character> outOfBoundsShip = shipFactory.makeCarrier(new Placement ("J9V"));
    String result = combinedChecker.checkPlacement(outOfBoundsShip, board);
    String expectedError = "That placement is invalid: the ship goes off the bottom of the board.";
    //assertFalse(combinedChecker.checkPlacement(outOfBoundsShip, board), "Ship should not be placed out of bounds.");
    assertEquals(expectedError, result);
  }

  @Test
  public void testShipPlacementWithCollision(){
    PlacementRuleChecker<Character> combinedChecker = new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null));
    Ship<Character> firstShip = shipFactory.makeSubmarine(new Placement("C5V"));
    board.tryAddShip(firstShip); // Assume this succeeds
    
    Ship<Character> collidingShip = shipFactory.makeDestroyer(new Placement("C4H"));
    Ship<Character> secondShip = shipFactory.makeCarrier(new Placement("A5v"));
    board.tryAddShip(secondShip);
    Ship<Character> collidingShip2 = shipFactory.makeBattleship(new Placement("A5v"));
    assertNotNull(combinedChecker.checkPlacement(collidingShip, board), "Ship should not be placed where it collides with another ship.");
    String result = combinedChecker.checkPlacement(collidingShip, board);
    String ExpectedError = "That placement is invalid: the ship overlaps another ship.";
    assertEquals(result, ExpectedError);
    String result2 =combinedChecker.checkPlacement(collidingShip2, board);
    String ExpectedError2 = "That placement is invalid: the ship overlaps another ship.";
    assertEquals(result2, ExpectedError2);
    assertNotNull(combinedChecker.checkPlacement(collidingShip2, board));
    
  }
}
