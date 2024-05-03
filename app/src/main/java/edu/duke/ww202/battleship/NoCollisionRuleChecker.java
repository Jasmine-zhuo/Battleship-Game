package edu.duke.ww202.battleship;

public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {
  public NoCollisionRuleChecker(PlacementRuleChecker<T> next){
    super(next);
  }
  @Override
  protected String checkMyRule(Ship<T> theShip, Board<T> theBoard){
    for(Coordinate coord: theShip.getCoordinates()){
      if(theBoard.whatIsAtForSelf(coord) != null){
        return "That placement is invalid: the ship overlaps another ship.";//Found a collision, violate the rule
      }
    }
    return null;//no collision found
  }
  
}
