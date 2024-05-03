package edu.duke.ww202.battleship;

import java.util.ArrayList;
import java.util.HashSet;

public class BattleShipBoard<T> implements Board<T>{
  private final int width;
  private final int height;
  private final ArrayList<Ship<T>> myShips;
  private final PlacementRuleChecker<T> placementChecker;
  private final HashSet<Coordinate> enemyMisses;
  private final T missInfo;
  /**
   * Constructs a BattleShipBoard with the specified width
   * and height
   * @param w is the width of the newly constructed board.
   * @param h is the height of the newly constructed board.
   *@param placementChecker Rule checker for ship placement.
   * @param missInfo Display info for misses.
   * @throws IllegalArgumentException if the width or height are less than or equal to zero.
   */
  public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker, T missInfo){
    if(w <= 0 || h <= 0){
      throw new IllegalArgumentException("BattleShipBoard's width must be positive:");
    }
    this.width = w;
    this.height = h;
    this.myShips = new ArrayList<>();
    this.placementChecker = placementChecker;
    this.enemyMisses = new HashSet<>();
    this.missInfo = missInfo;
  }
  //add a constructor for backward compatibility without rule checking
  /**
   * Backward compatible constructor for BattleShipBoard without missInfo.
   * Defaults missInfo to 'X' (or another suitable default for your implementation).
   * @param width Width of the board.
   * @param height Height of the board.
   */
  public BattleShipBoard(int width, int height, T missInfo){
    this(width, height, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), missInfo);
  }
  public int getWidth(){
    return width;
  }
  public int getHeight(){
    return height;
  }
  public HashSet<Coordinate> getEnemyMisses(){
    return enemyMisses;
  }

  
  public String tryAddShip(Ship<T> toAdd){
    //Check if the ship placement is valid during the placementChecker
    //"this" is battleShipBoard
    String placementError = placementChecker.checkPlacement(toAdd, this);
    if(placementError == null){
      myShips.add(toAdd);
      return null;//success
    }
    else{
      return placementError;//return the descriptive error
    }
  }
  public T whatIsAtForSelf(Coordinate where){
    return whatIsAt(where, true);
  }
  protected T whatIsAt(Coordinate where, boolean isSelf){
    if(!isSelf && enemyMisses.contains(where)){
      return missInfo;
    }
    for(Ship<T> s: myShips){
      if(s.occupiesCoordinates(where)){
        return s.getDisplayInfoAt(where, isSelf);
      }
    }
    return null; 
  }
  public T whatIsAtForEnemy(Coordinate where){
    return whatIsAt(where, false);
  }

  
  public Ship<T> fireAt(Coordinate c){
    for(Ship<T> ship: myShips){
      if(ship.occupiesCoordinates(c)){
        ship.recordHitAt(c);
        return ship;//ship was hit
      }
    }
    enemyMisses.add(c);
    return null;
  }
  public boolean allShipsSunk(){
    for(Ship<T> ship: myShips){
      if(!ship.isSunk()){
        return false;//found a ship not sunk
      }
    }
    return true;//all ships have sunk
  }
}

