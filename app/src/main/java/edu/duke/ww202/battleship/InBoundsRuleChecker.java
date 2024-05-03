
package edu.duke.ww202.battleship;
public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {
  public InBoundsRuleChecker(PlacementRuleChecker<T> next){
    super(next);
  }
  @Override
  protected String checkMyRule(Ship<T> theShip, Board<T> theBoard){
    for(Coordinate coord : theShip.getCoordinates()){
      //width and height start from 1 and row/col start from 0 so if =, return false;
      if(coord.getRow() < 0){
        return "That placement is invalid: the ship goes off the top of the board.";
      }
      if(coord.getRow() >= theBoard.getHeight()){
        return "That placement is invalid: the ship goes off the bottom of the board.";
      }
      if(coord.getColumn() < 0){
        return "That placement is invalid: the ship goes off the left of the board.";
      }
      if(coord.getColumn() >= theBoard.getWidth()){
        return "That placement is invalid: the ship goes off the right of the board.";
      }
      
      /**if(coord.getRow() < 0 || coord.getRow() >= theBoard.getHeight() || coord.getColumn() < 0 || coord.getColumn() >= theBoard.getWidth()){
        return false;
        }*/
    }
    return null;//all coordinates are within bounds
  }
}
