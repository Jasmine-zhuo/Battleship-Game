package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 * Tests for the SimpleShipDisplayInfo class.
 */
public class SimpleShipDisplayInfoTest {
  /**
   * Tests that getInfo returns the correct display data for a ship part that has not been hit..
 */
  @Test
  public void test_GetInfoNotHit() {
    // Setup for not hit scenario
    Character myData = 's';
    Character onHit = '*';
    SimpleShipDisplayInfo<Character> displayInfo = new SimpleShipDisplayInfo<>(myData, onHit);
    
    // Test for not hit
    Coordinate where = new Coordinate(0, 0); // Coordinate is arbitrary here
    Character result = displayInfo.getInfo(where, false); // false indicating not hit
    assertEquals(myData, result, "Expected myData for not hit");
  }
  /**
   * Tests that getInfo returns the correct display data for a ship part that has been hit.
   */
  @Test
  public void testGetInfoHit() {
    // Setup for hit scenario
    Character myData = 's';
    Character onHit = '*';
    SimpleShipDisplayInfo<Character> displayInfo = new SimpleShipDisplayInfo<>(myData, onHit);
    
    // Test for hit
    Coordinate where = new Coordinate(0, 0); // Coordinate is arbitrary here
    Character result = displayInfo.getInfo(where, true); // true indicating hit
    assertEquals(onHit, result, "Expected onHit for hit");
  }
}
