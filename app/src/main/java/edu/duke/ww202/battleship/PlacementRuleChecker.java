package edu.duke.ww202.battleship;

public abstract class PlacementRuleChecker<T> {
  private final PlacementRuleChecker<T> next;

  public PlacementRuleChecker(PlacementRuleChecker<T> next){
    this.next = next;
  }
  protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);
  /**The chain starts with one rule checker, and if its rule is passed (i.e., the method checkMyRule returns true), it forwards the request to the next checker in the chain (if any). 
     This continues until either a rule fails (returning false and stopping the chain) or all rules pass (the end of the chain is reached, and true is returned, indicating the placement is valid).
  */
  public String checkPlacement(Ship<T> theShip, Board<T> theBoard){
    //if we fail our own rule; stop the placement is not legal
    String error = checkMyRule(theShip, theBoard);
    if(error != null){
      return error;
    }
    //other wise, ask the rest of the chain.
    if(next != null){
      return next.checkPlacement(theShip, theBoard);
    }
    //if there are no more rules, then the placemen is legal
    return null;
  }
}
