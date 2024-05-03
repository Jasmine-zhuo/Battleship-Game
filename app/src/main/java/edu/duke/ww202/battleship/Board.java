package edu.duke.ww202.battleship;

public interface Board<T> {
  public int getHeight();
  public int getWidth();
  public String tryAddShip(Ship<T> toAdd);
  public T whatIsAtForSelf(Coordinate where);
  public Ship<T> fireAt(Coordinate c);
  public T whatIsAtForEnemy(Coordinate where);
  public boolean allShipsSunk();
}
