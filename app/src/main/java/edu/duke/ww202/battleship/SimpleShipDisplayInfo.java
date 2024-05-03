package edu.duke.ww202.battleship;
/**
 * This class represents the display information for a ship, including what is shown
 * when a ship has not been hit and what is shown when it has been hit.
 *
 * @param <T> The type of information used to display the ship's state (e.g., Character).
 */
public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
  private T myData;// Data to display when the ship has not been hit.
  private T onHit;// Data to display when the ship has been hit.
  /**
   * Constructs a SimpleShipDisplayInfo object with specified display data.
   *
   * @param myData The display data for when the ship is not hit.
   * @param onHit The display data for when the ship is hit.
   */
  public SimpleShipDisplayInfo(T myData, T onHit){
    this.myData = myData;
    this.onHit = onHit;
  }
  /**
   * Returns the appropriate display information based on whether the ship at the
   * given coordinate has been hit.
   *
   * @param where The coordinate of the ship part.
   * @param hit True if the ship part at 'where' has been hit, false otherwise.
   * @return The display data corresponding to the hit status of the ship part.
   */
  public T getInfo(Coordinate where, boolean hit){
    if(hit){
      return onHit;
    }else{
      return myData;
    }
  }
}
