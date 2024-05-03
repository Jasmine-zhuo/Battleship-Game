package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V1ShipFactoryTest {
  private void checkShip(Ship<Character> testShip, String expectedName, char expectedLetter, Coordinate... expectedLocs){
    assertEquals(expectedName, testShip.getName());
    //check each coordinate
    for(Coordinate loc: expectedLocs){
      assertTrue(testShip.occupiesCoordinates(loc), "Ship does not occupy expected location: " + loc);
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(loc, true), "Display info does not match for unit ship at " + loc);
      
    }
  }
  @Test
  public void test_makeDestroyer() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement p1 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> dst = factory.makeDestroyer(p1);
    checkShip(dst, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2));
    Placement placement = new Placement(new Coordinate(2, 3), 'H');
    Ship<Character> destroyer = factory.makeDestroyer(placement);
    checkShip(destroyer, "Destroyer", 'd', new Coordinate(2, 3), new Coordinate(2, 4), new Coordinate(2, 5));
  }
    @Test
  public void test_makeSubmarine() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement verticalPlacement = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> sub = factory.makeSubmarine(verticalPlacement);
    checkShip(sub, "Submarine", 's', new Coordinate(1, 2), new Coordinate(2, 2));
    Placement horizontalPlacement = new Placement(new Coordinate(1, 2), 'H');
    Ship<Character> subHorizontal = factory.makeSubmarine(horizontalPlacement);
    checkShip(subHorizontal, "Submarine", 's', new Coordinate(1, 2), new Coordinate(1, 3));
  }
    @Test
  public void test_makeBattlShip() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement p1 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> bat = factory.makeBattleship(p1);
    checkShip(bat, "Battleship", 'b', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(4, 2));
    Placement placement = new Placement(new Coordinate(3, 4), 'H');
    Ship<Character> battleship = factory.makeBattleship(placement);
    checkShip(battleship, "Battleship", 'b', new Coordinate(3, 4), new Coordinate(3, 5), new Coordinate(3, 6), new Coordinate(3, 7)); 
    }
  @Test
  public void test_makeCarrier() {
    V1ShipFactory factory = new V1ShipFactory();
    Placement p1 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> car = factory.makeCarrier(p1);
    checkShip(car, "Carrier", 'c', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(4, 2), new Coordinate(5, 2), new Coordinate(6, 2));
    Placement placement = new Placement(new Coordinate(4, 5), 'H');
    Ship<Character> carrier = factory.makeCarrier(placement);
    checkShip(carrier, "Carrier", 'c', new Coordinate(4, 5), new Coordinate(4, 6), new Coordinate(4, 7), new Coordinate(4, 8), new Coordinate(4, 9), new Coordinate(4, 10));
  }
  @Test
  public void test_illegal_orientation(){
    V1ShipFactory factory = new V1ShipFactory();
    //Placement invalidPlacement = new Placement(new Coordinate(1, 2), 'r');
    assertThrows(IllegalArgumentException.class, () -> factory.makeSubmarine(new Placement(new Coordinate(1, 2), 'r'))); 
  }
}
