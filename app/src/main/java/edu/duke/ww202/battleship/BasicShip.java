package edu.duke.ww202.battleship;

import java.util.HashMap;
import java.util.HashSet;
/**
 * Abstract class representing the basic functionality of a ship.
 * @param <T> The type of information that this ship displays.
 */
public abstract class BasicShip<T> implements Ship<T> {
  protected HashMap<Coordinate, Boolean> myPieces;
  protected ShipDisplayInfo<T> myDisplayInfo;
  protected ShipDisplayInfo<T> enemyDisplayInfo;
  //abstract HashSet<Coordinate> getCoords();
  /**
   * Constructs a BasicShip with specified coordinates and display information.
   * @param where Iterable of coordinates where the ship is located.
   * @param myDisplayInfo The display information for the ship.
   */
  public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo){
    myPieces = new HashMap<>();
    for(Coordinate coord : where){
      myPieces.put(coord, false);
    }
    this.myDisplayInfo = myDisplayInfo;
    this.enemyDisplayInfo = enemyDisplayInfo;
  }
  protected void checkCoordinateInthisShip(Coordinate c){
    if(!myPieces.containsKey(c)){
      throw new IllegalArgumentException("Coordinate " + c + "is not part of the ship");
    }
  }
  /**
   * Checks if a given coordinate is part of the ship.
   *
   * @param where The coordinate to check.
   * @return true if the coordinate is part of the ship, false otherwise.
   */
  @Override
  public boolean occupiesCoordinates(Coordinate where){
    return myPieces.containsKey(where);
  }
  /**
   * Checks if the ship is sunk. A ship is considered sunk if all its parts have been hit.
   *
   * @return true if the ship is sunk, false otherwise.
   */
  @Override
  public boolean isSunk(){
    for(Boolean hit: myPieces.values()){
      if(!hit){
        return false;
      }
    }
    return true;
  }
  /**
   * Records that a part of the ship has been hit at the specified coordinate.
   *
   * @param where The coordinate where the ship has been hit.
   */
  @Override
  public void recordHitAt(Coordinate where){
    checkCoordinateInthisShip(where);
    myPieces.put(where, true);
  }
  /**
   * Checks if a part of the ship at a given coordinate has been hit.
   *
   * @param where The coordinate to check.
   * @return true if the part of the ship at the coordinate has been hit, false otherwise.
   */
  @Override
  public boolean wasHitAt(Coordinate where){
    checkCoordinateInthisShip(where);
    return myPieces.get(where);
  }
  /**
   * Gets the display information for a part of the ship at a given coordinate.
   * The information displayed is based on whether the part has been hit.
   *
   * @param where The coordinate of the part of the ship.
   * @param hit True if the part has been hit, false otherwise.
   * @return The display information for the part of the ship at the given coordinate.
   */
  @Override
  public T getDisplayInfoAt(Coordinate where, boolean myShip){
    Boolean wasHit = myPieces.get(where);
    if(wasHit == null){
      throw new IllegalArgumentException("Coordinate " + where + " is not part of the ship");
    }
    if(myShip){
      return myDisplayInfo.getInfo(where, wasHitAt(where));
    }
    else{
      return enemyDisplayInfo.getInfo(where, wasHitAt(where));
    }
    //return myShip? myDisplayInfo.getInfo(where, wasHitAt(where)) : enemyDisplayInfo.getInfo(where, wasHitAt(where));
  }
  @Override
  public Iterable<Coordinate> getCoordinates(){
    return myPieces.keySet();
  }
}
