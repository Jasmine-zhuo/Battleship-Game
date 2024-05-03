package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {
  @Test
  public void testMakeCoords(){
    Coordinate upperLeft = new Coordinate(2, 3);
    int width = 2;
    int height = 3;
    HashSet<Coordinate> expectedCoords = new HashSet<>();
    expectedCoords.add(new Coordinate(2, 3));
    expectedCoords.add(new Coordinate(2, 4));
    expectedCoords.add(new Coordinate(3, 3));
    expectedCoords.add(new Coordinate(3, 4));
    expectedCoords.add(new Coordinate(4, 3));
    expectedCoords.add(new Coordinate(4, 4));
    HashSet<Coordinate> actualCoords = RectangleShip.makeCoords(upperLeft, width, height);
    assertEquals(expectedCoords, actualCoords);
  }
  @Test
  public void testSingleCoordinateShip() {
    Coordinate coord = new Coordinate(3, 4);
    RectangleShip<Character> ship = new RectangleShip<>(coord, 's', '*');
    assertTrue(ship.occupiesCoordinates(coord));
    assertFalse(ship.occupiesCoordinates(new Coordinate(3, 5)));
  }
  private RectangleShip<Character> createTestShip(Coordinate upperLeft, int width, int height, char disPlayChar, char hitDisplayChar){
    return new RectangleShip<>("testShip", upperLeft, width, height, disPlayChar, hitDisplayChar);
  }
  @Test
  void testRecordHitAndWasHit(){
    Coordinate upperLeft = new Coordinate(2, 2);
    RectangleShip<Character> ship = createTestShip(upperLeft, 2, 2, 's', '*');
    Coordinate hitCoord = new Coordinate(2, 2);
    Coordinate missCoord = new Coordinate(1, 1);
    ship.recordHitAt(hitCoord);
    assertTrue(ship.wasHitAt(hitCoord), "Ship should report a hit at " + hitCoord);
    assertFalse(ship.wasHitAt(new Coordinate(3, 2)), "Ship should report no hit at an unhit coordinate");
    assertThrows(IllegalArgumentException.class, () -> ship.wasHitAt(missCoord));
  }
  @Test
  void testIsSunk(){
    Coordinate upperleft = new Coordinate(2, 2);
    RectangleShip<Character> ship = createTestShip(upperleft, 1, 2, 'S', '*');
    //Initially, the ship should be sunk
    assertFalse(ship.isSunk(), "Ship should not be sunk initially");
    //After hitting all parts of the ship, it should be sunk
    ship.recordHitAt(new Coordinate(2, 2));
    ship.recordHitAt(new Coordinate(3, 2));
    assertTrue(ship.isSunk(), "Ship should be sunk after all parts are hit");
  }
  @Test
  void testGetDisplayInfoAt(){
    Coordinate upperLeft = new Coordinate(2, 2);
    char displayChar = 's';
    char hitDisplayChar = '*';
    RectangleShip<Character> ship = createTestShip(upperLeft, 1, 1, displayChar, hitDisplayChar);
    //Verify display info for unhit part
    assertEquals(displayChar, ship.getDisplayInfoAt(upperLeft, true), "Should display the character for unhit parts from the ship owner's perspective");
    assertNull(ship.getDisplayInfoAt(upperLeft, false));
    //Record a hit an verify display info
    ship.recordHitAt(upperLeft);
    assertEquals(hitDisplayChar, ship.getDisplayInfoAt(upperLeft, true));
    assertThrows(IllegalArgumentException.class, () -> ship.getDisplayInfoAt(new Coordinate(5, 5), true), "Exception expected for coordinates not part of the ship.");
  }
  @Test
  public void testShipName(){
    RectangleShip<Character> testShip = new RectangleShip<>("testShip", new Coordinate(2, 2), 3, 1, 's', '*');
    assertEquals("testShip", testShip.getName(), "The name should match the provided at creation.");
  }
  @Test
  public void test_get_coordinates(){
    String name = "testShip";
    Coordinate upperLeft = new Coordinate(2, 3);
    int width = 2;
    int height = 3;
    Character data = 'D';
    Character hitDisplay = '*';
    SimpleShipDisplayInfo<Character> displayInfo = new SimpleShipDisplayInfo<>(data, hitDisplay);
    SimpleShipDisplayInfo<Character> enemDisplayInfo = new SimpleShipDisplayInfo<Character>(null, data);
    //create a Rectangle ship
    RectangleShip<Character> ship = new RectangleShip<Character>(name, upperLeft, width, height, displayInfo, enemDisplayInfo);
    //expected coordinates
    HashSet<Coordinate> expectedCoords = new HashSet<>();
    expectedCoords.add(new Coordinate(2, 3));
    expectedCoords.add(new Coordinate(2, 4));
    expectedCoords.add(new Coordinate(3, 3));
    expectedCoords.add(new Coordinate(3, 4));
    expectedCoords.add(new Coordinate(4, 3));
    expectedCoords.add(new Coordinate(4, 4));
    //get actual coordiantes
    Set<Coordinate> actualCoords = new HashSet<>();
    ship.getCoordinates().forEach(actualCoords::add);//function implemented
    //assert
    assertEquals(expectedCoords, actualCoords, "The ship should occupy the ecpected coordinates.");
  }
}
