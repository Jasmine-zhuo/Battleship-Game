package edu.duke.ww202.battleship;

/**
 * This class implements the AbstractShipFactory interface for Version 1 of the Battleship game.
 * It creates ships with specific dimensions and characters based on the type of ship.
 */


public class V1ShipFactory implements AbstractShipFactory<Character> {
  /*final AbstractShipFactory<Character> shipFactory;
  public V1ShipFactory(){
    shipFactory = new V1ShipFactory();
    }*/
  /**
   * Creates a ship with specified dimensions, character, and name.
   * Swaps width and height if the orientation is horizontal.
   *
   * @param where The placement of the ship, including its upper left coordinate and orientation.
   * @param w The width of the ship.
   * @param h The height of the ship.
   * @param letter The character to display for the ship.
   * @param name The name of the ship.
   * @return A new instance of RectangleShip with the specified parameters.
   */
  protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name){
   Coordinate upperLeft = where.getWhere();
   //if(where.getOrientation() != 'H' && where.getOrientation() != 'V'){
   //throw new IllegalArgumentException("Orientation must be 'H' or 'V\'.");
   //
   // }
   //no need to check here, boundary check implemented in Placement class
   
   if(where.getOrientation() == 'H'){
     int temp = w;
     w = h;
     h = temp;
   }
   return new RectangleShip<Character>(name, upperLeft, w, h, letter, '*');
}
  @Override
  public Ship<Character> makeSubmarine(Placement where){
    return createShip(where, 1, 2, 's', "Submarine");
  }
  @Override
  public Ship<Character> makeBattleship(Placement where){
    return createShip(where, 1, 4, 'b', "Battleship");
  }
  @Override
  public Ship<Character> makeCarrier(Placement where){
    return createShip(where, 1, 6, 'c', "Carrier");
  }
  @Override
  public Ship<Character> makeDestroyer(Placement where){
    return createShip(where, 1, 3, 'd', "Destroyer");
  }
}
