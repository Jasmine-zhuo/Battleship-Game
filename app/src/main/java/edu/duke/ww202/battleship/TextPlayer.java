package edu.duke.ww202.battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;
/**
 * This class represents a text-based player in the battleship game. It handles
 * interactions with the player, such as placing ships and attacking opponent's ships.
 */
public class TextPlayer {
  protected final String name;
  public final Board<Character> theBoard;
  private final BufferedReader inputReader;
  public final BoardTextView view;
  private final PrintStream out;
  protected final AbstractShipFactory<Character> shipFactory;
  private final ArrayList<String> shipsToPlace;
  private final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
  /**
   * Constructs a TextPlayer with specified name, board, input source, output stream, and ship factory.
   *
   * @param name The name of the player.
   * @param theBoard The game board associated with the player.
   * @param inputSource Reader to read player's input.
   * @param out Output stream to display messages to the player.
   * @param shipFactory Factory to create ships.
   */
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, AbstractShipFactory<Character> shipFactory ){
    this.name = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = inputSource;
    this.out = out;//out, not System.out
    this.shipFactory = new V1ShipFactory();
    this.shipsToPlace = new ArrayList<>();
    this.shipCreationFns = new HashMap<>();
    setupShipCreationList();
    setupShipCreationMap();
  }
  /**
   * Initializes the ship creation functions map. Associates ship types with their creation functions.
   */
  protected void setupShipCreationMap(){
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }
  
  /**
   * Initializes the list of ships to be placed. Specifies the types and quantities of ships.
   */
  protected void setupShipCreationList(){
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }
  public String getName(){
    return name;
  }
  /**
   * Reads a placement from the player.
   *
   * @param prompt The message prompting the player for input.
   * @return The placement based on the player's input.
   * @throws IOException If an I/O error occurs.
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();//read input line
    //System.out.println("input: "+s);
    //String s = inputReader.readLine();//trim() remove trailing whitespace
    return new Placement(s);
    //return new Placement(s.trim());
  }
  /**public void doOnePlacement() throws IOException{
    String prompt = String.format("Player %s Where would you like to place a Destroyer?", name);
    Placement userPlacement = readPlacement(prompt);//read a placemen from the user
    Ship<Character> userShip = shipFactory.makeDestroyer(userPlacement);//new version
    //create a basic ship
    //For testing a different ship:
    //Ship<Character> userShip = shipFactory.makeCarrier(userPlacement);
    theBoard.tryAddShip(userShip);//add the ship to the board
    
    out.print(view.displayMyOwnBoard());//print out the board    
    }*/
  
  /**
   * Reads a placement from the player.
   *
   * @param prompt The message prompting the player for input.
   * @return The placement based on the player's input.
   * @throws IOException If an I/O error occurs.
   */
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException{
    boolean validPlacement = false;
    while(!validPlacement){
      try{
        //Placement p = readPlacement("Player " + name + " Where do you want to place a " + shipName + "?");// Prompt the user for a placement
        out.println("Player " + name + " Where do you want to place a " + shipName + "?");
        String line = inputReader.readLine();
        if(line == null){//EOF
          throw new EOFException("End of input stream. Game will terminate.");
        }
        Placement p = new Placement(line);
        Ship<Character> s = createFn.apply(p);
        String result = theBoard.tryAddShip(s);// Attempt to create and add the ship based on the user's input
        if(result == null){//assuming add ship successfully and retuen null
          validPlacement = true;
          out.println(view.displayMyOwnBoard());
        }else{
          out.println(result);//display the reason why the placement failed
        }
      }catch(EOFException e){
        out.println(e.getMessage());
        break;//exit the loop and the game
      } catch(IllegalArgumentException e){
        out.println("Invalid placement: " + e.getMessage() + "Please try again.");
      }
    }
    //out.println(view.displayMyOwnBoard());//display the board
  }
  /**
   * Manages the ship placement phase for the player. Prompts the player to place all ships as specified
   * in the ship creation list. Continues to prompt for each ship's placement until all ships are placed
   * on the board successfully.
   * 
   * @throws IOException If an I/O error occurs during input reading.
   */
  public void doPlacementPhase() throws IOException{
    out.println(view.displayMyOwnBoard());
     // Print the instructions message
    out.println("--------------------------------------------------------------------------------");
    out.println("Player " + name + ": you are going to place the following ships (which are all");
    out.println("rectangular). For each ship, type the coordinate of the upper left");
    out.println("side of the ship, followed by either H (for horizontal) or V (for");
    out.println("vertical).  For example M4H would place a ship horizontally starting");
    out.println("at M4 and going to the right.  You have");
    out.println();
    out.println("2 \"Submarines\" ships that are 1x2");
    out.println("3 \"Destroyers\" that are 1x3");
    out.println("3 \"Battleships\" that are 1x4");
    out.println("2 \"Carriers\" that are 1x6");
    out.println("--------------------------------------------------------------------------------");
    for(String shipType : shipsToPlace){
      Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipType);
      //if(createFn != null){
      doOnePlacement(shipType, createFn);//decide no need to check null
        //}
        //else{
        //throw new IllegalArgumentException("No creation function found for ship type: " + shipType);
        //}
    }
    // Call doOnePlacement to place a single ship
    //doOnePlacement();
  }
  /** Check if the coordinate is in the boarboar
   * 
   * @param coord
   * @return boolean
   */
  private boolean isValidCoordinate(Coordinate coord){
    return coord.getRow() >= 0 && coord.getRow() < theBoard.getHeight() &&
      coord.getColumn() >= 0 && coord.getColumn() < theBoard.getWidth();
  }
  /**
   * Prompts the player for coordinates to attack on the enemy's board, validating the input and repeating
   * the prompt if the input is invalid or outside the board's bounds.
   * 
   * @return The valid coordinate where the player wishes to attack.
   * @throws IOException If an I/O error occurs during input reading.
   */
  public Coordinate readAttackCoordinate() throws IOException{
    Coordinate attackCoord = null;
    boolean validInput = false;
    while(!validInput){
      try{
        out.println("Enter the coordinate for your attack: (e.g., A5):");
        String line = inputReader.readLine();
        if(line == null){
          out.println("EOF detected. Exiting the game.");
          return null;
        }
        attackCoord = new Coordinate(line);
        if(!isValidCoordinate(attackCoord)){
          out.println("Invalid coordinate. Please try again.");
          continue;
        }
        validInput = true;
      }catch(IllegalArgumentException | EOFException e){
        out.println("Invalid coordinate. Please try again.");
      }
    }
    return attackCoord;
    
  }
  /**
   * Executes an attack at the specified coordinate on the enemy's board, reporting the outcome of the attack
   * (hit, miss, or sinking a ship).
   * 
   * @param enemyBoard The enemy's board on which to perform the attack.
   * @throws IOException If an I/O error occurs during the attack process.
   */
  public void doAttack(Board<Character> enemyBoard) throws IOException{
    Coordinate attackCoord = readAttackCoordinate();
    Ship<Character> targetShip = enemyBoard.fireAt(attackCoord);
    if(targetShip == null){
      out.println("You missed!");
    }else{
      out.println("You hit a " + targetShip.getName() + "!");
      if(targetShip.isSunk()){
        out.println("You sunk a " + targetShip.getName() + "!");
      }
    } 
  }
  /**
   * Plays a single turn for the player, including displaying the current state of both the player's and
   * the enemy's boards, performing an attack, and then displaying the updated board states.
   * 
   * @param enemyBoard The enemy's board to attack.
   * @param enemyView The textual view of the enemy's board.
   * @param enemyName The name of the enemy player.
   * @throws IOException If an I/O error occurs during the turn.
   */
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException{
    String myHeader = "Your Ocean";
    String enemyHeader = enemyName + "'s Ocean";
    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
    doAttack(enemyBoard);
    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
  }
  /**
   * Manages the attacking phase of the game, alternating turns between the player and the enemy until one
   * player has no ships left. Announces the winner of the game.
   * 
   * @param enemyPlayer The enemy player.
   * @throws IOException If an I/O error occurs during the attacking phase.
   */
  public void doAttackingPhase(TextPlayer enemyPlayer) throws IOException{
    boolean gameOn = true;
    while(gameOn){
      //Player's turn
      out.println(this.name + "'s turn:");
      playOneTurn(enemyPlayer.theBoard, enemyPlayer.view, enemyPlayer.name);
      if(enemyPlayer.theBoard.allShipsSunk()){
        out.println(this.name + " wins! " + enemyPlayer.name + " has no more ships left.");
        gameOn = false;
        continue;
      }
      //enemy's turn
      out.println(enemyPlayer.name + "'s turn:");
      enemyPlayer.playOneTurn(this.theBoard, this.view, this.name);
      if(this.theBoard.allShipsSunk()){
        out.println(enemyPlayer.name + " wins! " + this.name + " has no more ships left.");
        gameOn = false;  
      }
    }
      
  }
}

