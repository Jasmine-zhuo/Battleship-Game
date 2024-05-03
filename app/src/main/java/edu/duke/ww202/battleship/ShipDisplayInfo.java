package edu.duke.ww202.battleship;
/**
 * Gets the display information for a ship based on a coordinate and whether it has been hit.
 * @param where The coordinate of the ship part.
 * @param hit True if the ship part at the coordinate has been hit, false otherwise.
 * @return The display information of type T for the ship part.
 */
public interface ShipDisplayInfo<T> {
  public T getInfo(Coordinate where, boolean hit);
}
