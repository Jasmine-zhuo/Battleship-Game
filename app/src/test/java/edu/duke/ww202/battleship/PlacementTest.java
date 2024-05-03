package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlacementTest {
  @Test
  public void test_get_placement() {
    Coordinate c = new Coordinate(2, 3);
    Placement p = new Placement(c, 'V');
    Placement p2 = new Placement(c, 'v');
    assertEquals(p.getOrientation(), 'V');
    assertEquals(p.getWhere(), c);
    assertEquals(p2.getOrientation(), 'V');
    assertThrows(IllegalArgumentException.class, () -> new Placement(c, 'a'));
  }
  @Test void test_string_constructor(){    
    Placement placement = new Placement("A9V");//test for valid input
    assertEquals(0, placement.getWhere().getRow());
    assertEquals(9, placement.getWhere().getColumn());
    assertEquals('V', placement.getOrientation());

    Placement placement1 = new Placement("B2H");
    assertEquals(1, placement1.getWhere().getRow());
    assertEquals(2, placement1.getWhere().getColumn());
    assertEquals('H', placement1.getOrientation());

    Placement placement2 = new Placement("T0V");
    assertEquals(19, placement2.getWhere().getRow());
    assertEquals(0, placement2.getWhere().getColumn());
    assertEquals('V', placement2.getOrientation());
    
    Placement placement3 = new Placement("c3v");//test for lower case      
    assertEquals(2, placement3.getWhere().getRow());
    assertEquals(3, placement3.getWhere().getColumn());
    assertEquals('V', placement3.getOrientation());
    
  }

  @Test
  public void test_stringConstructor_Invalid() {
    assertThrows(IllegalArgumentException.class, () -> new Placement("D4X"));//invalid orientation
    assertThrows(IllegalArgumentException.class, () -> new Placement("Z12H"));//invalid column
    assertThrows(IllegalArgumentException.class, () -> new Placement("E10V"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("FV"));//too short string
    assertThrows(IllegalArgumentException.class, () -> new Placement("RT5V"));//too long
    assertThrows(IllegalArgumentException.class, () -> new Placement("A4VT"));
    assertThrows(IllegalArgumentException.class, () -> new Placement(null));
  }
  @Test
  public void test_toString() {
    Coordinate coord = new Coordinate(1, 2);
    Placement placement = new Placement(coord, 'H');
    
    String expected = "(1, 2)H";
    assertEquals(expected, placement.toString());
  }
  
  @Test
  public void test_equals() {
    Coordinate coord1 = new Coordinate(1, 2);
    Placement placement1 = new Placement(coord1, 'H');
    Coordinate coord2 = new Coordinate(1, 2);
    Placement placement2 = new Placement(coord2, 'H');
    Coordinate coord3 = new Coordinate(3, 4);
    Placement placement3 = new Placement(coord3, 'V');
    Placement placement4 = new Placement(coord3, 'v');
    Placement placement5 = new Placement(coord3, 'h');
    Placement p1 = new Placement(coord1, 'v');
    Placement p2 = new Placement(coord2, 'V');
    assertEquals(p1, p2);
    assertTrue(placement1.equals(placement1));
    assertTrue(placement1.equals(placement2));
    assertFalse(placement1.equals(placement3));
    assertTrue(placement3.equals(placement4));
    assertFalse(placement3.equals(placement5));
    assertFalse(placement1.equals("(1, 2)H"));//check different type

    
    Placement placement6 = new Placement("A2H");
    Placement placement7 = new Placement("A2H");
    Placement placement8 = new Placement("A3V");
    assertTrue(placement6.equals(placement7));
    assertFalse(placement6.equals(placement8));
 
  }
  
  @Test
  public void test_hashCode() {
    Coordinate coord1 = new Coordinate(1, 2);//test for basic constructor
    Placement placement1 = new Placement(coord1, 'H');
    
    Coordinate coord2 = new Coordinate(1, 2);
    Placement placement2 = new Placement(coord2, 'H');

    Placement placement3 = new Placement(coord2, 'h');
    assertEquals(placement1.hashCode(), placement2.hashCode());
    assertEquals(placement1.hashCode(), placement3.hashCode());

    Placement placement4 = new Placement("A2H");//test for string constructor
    Placement placement5 = new Placement("A2H");
    Placement placement6 = new Placement("A3V");

    assertEquals(placement4.hashCode(), placement4.hashCode());
    assertEquals(placement4.hashCode(), placement5.hashCode());
    assertNotEquals(placement5.hashCode(), placement6.hashCode());
  }
}
