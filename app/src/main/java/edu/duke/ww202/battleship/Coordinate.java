package edu.duke.ww202.battleship;

public class Coordinate {
  private final int row;
  private final int column;
  /**constructor with int input
   */
  public Coordinate(int r, int c){
    this.row = r;
    this.column = c;
  }
  /**constructor with string input
   *@param A-Z/a-z + 0-10 
   */
  public Coordinate(String descr){
    if(descr == null || descr.length() < 2){
      throw new IllegalArgumentException("Invalid string format for Coordinate.");
    }
    char rowChar = Character.toUpperCase(descr.charAt(0));//Convert the first character to uppercase for case-insensitive
    if(rowChar < 'A' || rowChar > 'Z'){
      throw new IllegalArgumentException("Row character must be between A and Z.");
    }
    this.row = rowChar - 'A';// Convert character to row number ('A' -> 0, 'B' -> 1, ...)
    try{
      this.column = Integer.parseInt(descr.substring(1));
    }catch(NumberFormatException e){
      throw new IllegalArgumentException("Invalid format for column number.");
    }
    // Check if the column number is between 0 and 10
    if (this.column < 0 || this.column > 9) {
      throw new IllegalArgumentException("Column number must be between 0 and 9.");
    }
  }
    
  public int getRow(){
    return row;
  }
  public int getColumn(){
    return column;
  }
  @Override
  public boolean equals(Object o){
    if(o.getClass().equals(getClass())){
      Coordinate c = (Coordinate) o;//casting: create a Coordinate to compare, o is Object
      return row == c.row && column == c.column;
    }
    return false;
  }
  @Override
  public String toString(){
    return "(" + row + ", " + column + ")";
  }
  /** Override the hashcode leveraging toString()
   */
  @Override
  public int hashCode(){
    return toString().hashCode();
  }
}
