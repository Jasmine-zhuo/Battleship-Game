package edu.duke.ww202.battleship;
import java.util.HashSet;
/**
 * Represents a rectangular ship with a specified type of information on display.
 * @param <T> The type of information that this ship displays.
 */
public class RectangleShip<T> extends BasicShip<T> {
  /**
   * Constructs a RectangleShip with specified upper left coordinate, dimensions, and display information.
   * @param upperLeft The upper-left coordinate of the ship.
   * @param width The width of the ship.
   * @param height The height of the ship.
   * @param data The display information when the ship has not been hit.
   * @param onHit The display information when the ship has been hit.
   */
  private final String name;

  public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo){
    super(makeCoords(upperLeft, width, height), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
    //tell the parent constructor that for my own view display data if not hit or onHit if hit for the enemy view, nothing if not hit, data if hit
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    //new SimpleShipDisplayInfo<T>(null, data);
    //super(makeCoords(upperLeft, width, height), new SimpleShipDisplayInfo<T>(data, onHit));
  }
  /**
   * Convenience constructor for creating a ship of size 1x1 with specified display information.
   * @param upperLeft The coordinate of the ship.
   * @param data The display information when the ship has not been hit.
   * @param onHit The display information when the ship has been hit.
   */
  public RectangleShip(Coordinate upperLeft, T data, T onHit) {
    this("testShip", upperLeft, 1, 1, data, onHit);
  }
  /**
   * Generates a set of coordinates for a rectangle based on the given position and dimensions.
   * @param upperLeft The upper-left coordinate of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   * @return A set of coordinates representing the rectangle.
   */
  static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height){
    HashSet<Coordinate> coords = new HashSet<Coordinate>();
    for(int r = 0; r < height; ++r){
      for(int c = 0; c < width; ++c){
        coords.add(new Coordinate(upperLeft.getRow() + r, upperLeft.getColumn() + c));
      }
    }
    return coords;
  }
  @Override
  public String getName(){
    return this.name;
  }
}

