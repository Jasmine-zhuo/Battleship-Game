package edu.duke.ww202.battleship;

import java.util.function.Function;

/**
 * This class handles textual display of
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the 
 * enemy's board.
 */
public class BoardTextView {
  /**
   * The Board to display
   */
  private final Board<Character> toDisplay;
  /**
   *Constructs a BoardView, given the board it will display.
   *@param  toDisplay is the Board to display.
   *Boundary check, board cannot exceed 10*26 to be viewed.
   */
  
  public BoardTextView(Board<Character> toDisplay){
    if(toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26){
      throw new IllegalArgumentException("Board must be no larger than 10*26, but is "+ toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
    this.toDisplay = toDisplay;
  }
  /**
   * Generates a string representation of the player's own board.
   * 
   * @return A string depicting the player's board with ships and hit/miss information.
   */
  public String displayMyOwnBoard(){
    return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
  }
  /**
   * Generates a string representation of the enemy's board from the player's perspective.
   * 
   * @return A string showing the player's view of the enemy board, with hits and misses marked.
   */
  public String displayEnemyBoard(){
    return displayAnyBoard(c -> toDisplay.whatIsAtForEnemy(c));
  }
  /**
   * Utility method to generate a string representation of a board.
   * This method abstracts the common logic for generating board strings.
   * 
   * @param getSquareFn Function to determine what character to display for each board cell.
   * @return A string representation of the board.
   */
  public String displayAnyBoard(Function<Coordinate, Character> getSquareFn){
    StringBuilder ans = new StringBuilder();
    ans.append(makeHeader());
    char label = 'A';
    for(int row = 0; row < toDisplay.getHeight(); ++row){

      ans.append(label + " ");
      for(int column = 0; column < toDisplay.getWidth(); ++column){
        Character shipAtLocation = getSquareFn.apply(new Coordinate(row, column));//here is the functor
        if (shipAtLocation == null) {
          ans.append(" "); // one spaces for empty cell
        } else {
          ans.append(shipAtLocation); // Space and character for ship cell
        }
        if (column < toDisplay.getWidth() - 1) {
          ans.append("|"); // Separator between cells
        }
        
      }
      
      ans.append(" " + label).append("\n");
      label ++;
    }
    ans.append(makeHeader());
    return ans.toString();
  }
  /**
     This makes the header line, e.g. 0|1|2|3|4\n
     *@return the String that is the header line for the given board  
 */
  String makeHeader(){
    StringBuilder ans = new StringBuilder("  ");//README shows two spaces
    String sep = "";////start with nothing to separate, then switch to | to separate
    for(int i = 0; i < toDisplay.getWidth(); ++i){
      ans.append(sep);//no | before 0, and no | after last element
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }
  /**
   * Displays the player's board and the enemy's board side by side.
   * 
   * @param enemyView The textual view of the enemy's board.
   * @param myHeader The header text for the player's board.
   * @param enemyHeader The header text for the enemy's board.
   * @return A string representation showing both the player's and enemy's boards side by side.
   */
  public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader){
    int boardWidth = this.toDisplay.getWidth();
    int firstHeaderStart = 5;
    int secondHeaderStart = 2 * boardWidth + 22;
    int secondBoardStart = 2 * boardWidth + 19;//second board starts here
    //Headers
    StringBuilder headerBuilder = new StringBuilder();
    //headerBuilder.append(String.format("%-" + secondHeaderStart + "s", myHeader));//put placeholder
    headerBuilder.append("     ").append(myHeader); // Ensure 5 spaces before myHeader
    int paddingForSecondHeader = secondHeaderStart - ("     " + myHeader).length();
    for (int i = 0; i < paddingForSecondHeader; i++) {
      headerBuilder.append(" ");
    }
    headerBuilder.append(enemyHeader + "\n");
    
    //Boards
    String myBoard = this.displayMyOwnBoard();
    String enemyBoard = enemyView.displayEnemyBoard();
    
    String[] myBoardLines = myBoard.split("\n");
    String[] enemyBoardLines = enemyBoard.split("\n");
    StringBuilder boardsBuilder = new StringBuilder();
    for (int i = 0; i < myBoardLines.length; i++) {
      String myLine =  myBoardLines[i];
      String enemyLine =  enemyBoardLines[i];
      
      // Append my board line
      boardsBuilder.append(myLine);
      
      // Calculate padding
      int padding = secondBoardStart - myLine.length();
      for (int j = 0; j < padding; j++) {
        boardsBuilder.append(" ");
      }
      
      // Append enemy board line
      boardsBuilder.append(enemyLine);
      
      boardsBuilder.append("\n"); // New line for the next set of board lines
    }
    
    return headerBuilder.toString() + boardsBuilder.toString();
  }
}
