package edu.duke.ww202.battleship;

public class Placement {
  private final Coordinate where;
  private final char orientation;
  /**basic constructor
   *@param Coordinate and orientation, orientation normalize to uppercase
   */
  public Placement(Coordinate c, char o){
    this.where = c;
    this.orientation = Character.toUpperCase(o);//normalize to uppercase
    validateOrientation(this.orientation);//check valid orientation
  }
  /**Constructor that parses a string to create a Placement.
   *Assumes the string is in the format "A0V" where "A0" is the Coordinate
   * and "V" is the orientation.
   */
  public Placement(String descr) {
    if (descr == null || descr.length() != 3) {
      throw new IllegalArgumentException("Invalid string format for Placement.");
    } 
    this.where = new Coordinate(descr.substring(0, 2)); // Using Coordinate's string constructor
    char ori = Character.toUpperCase(descr.charAt(2)); // Normalize to uppercase
    validateOrientation(ori);// Check if orientation is either 'H' or 'V'
    this.orientation = ori;
  }
  /** check the orientation is H/V
   */
  private void validateOrientation(char orientation) {
    if (orientation != 'H' && orientation != 'V' && orientation != 'h' && orientation != 'v') {
      throw new IllegalArgumentException("Orientation must be 'H' or 'V'.");
    }
  }
  public Coordinate getWhere(){
    return where;
  }
  public char getOrientation(){
    return orientation;
  }
  
  @Override
  public String toString(){
    return where.toString() + orientation;
  }
  
  @Override
  public boolean equals(Object o){
    if(o.getClass().equals(getClass())){
      Placement p = (Placement) o;
      return where.equals(p.where) && orientation == p.orientation;
    }
    return false;
  }
  @Override
  public int hashCode(){
    return toString().hashCode();
  }
}
