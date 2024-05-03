package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TextPlayerTest {
  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes){
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h, new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null)), 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    return new TextPlayer("A", board, input, output, shipFactory);
  }
 
  @Test
    void test_read_placement() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\nA4V\n", bytes);
    String prompt = "Please enter a location for a ship:";
    
      Placement[] expected = new Placement[3];
      expected[0] = new Placement(new Coordinate(1, 2), 'V');
      expected[1] = new Placement(new Coordinate(2, 8), 'H');
      expected[2] = new Placement(new Coordinate(0, 4), 'V');
      //conduct the test
      for(int i = 0; i < expected.length; i++){
        Placement p = player.readPlacement(prompt);
        assertEquals(p, expected[i]);
        //System.out.print(p.toString());
        assertEquals(prompt + "\n", bytes.toString());
        bytes.reset();
      }
    }
  /**
  @Test
  void test_doOnePlacement() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);
    player.doOnePlacement();
    //check output
    String expectedPrompt = "Player A Where would you like to place a Destroyer?\n";
    String expectedBoard = player.view.displayMyOwnBoard();

    String actualOutput = bytes.toString();
    assertTrue(actualOutput.startsWith(expectedPrompt));
    assertTrue(actualOutput.endsWith(expectedBoard));
    assertEquals(expectedPrompt + expectedBoard, actualOutput);
    //assertOutputMatchesPromptAndBoard(expectedPrompt, expectedBoard);
    assertNotNull(player.theBoard.whatIsAt(new Coordinate(1, 2)));
    //assertNotNull(b.whatIsAt(new Coordinate(2, 2)));//1 place to 2 places

  }

  @Test
  public void testDoPlacementPhase() throws IOException{
    // Setup the board and player
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<>(10, 10); // Assume no placement rules for simplicity
    V1ShipFactory shipFactory = new V1ShipFactory();
    String inputData = "B2V\n"; // Simulated user input for placement
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    TextPlayer player = new TextPlayer("A", board, input, output, shipFactory);
    
    // Execute the placement phase
    player.doPlacementPhase();
    
    // Verify the output contains the board, instructions, and prompt for placement
    String outputStr = bytes.toString();
    assertTrue(outputStr.contains("Player A: you are going to place the following ships"));
    assertTrue(outputStr.contains("2 \"Submarines\" ships that are 1x2"));
    //assertTrue(outputStr.contains("Where would you like to put your ship?"));
    
    // Verify a ship was added to the board
    assertNotNull(board.whatIsAt(new Coordinate(1, 2))); // Adjust coordinate based on expected placement
  }
  */
  @Test
  void testDoOnePlacementDifferentShips() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader input = new BufferedReader(new StringReader("A0V\n"));
    TextPlayer player = new TextPlayer("A", new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(null), 'X'), input, output, new V1ShipFactory());
    
    // Test placement for Submarine
    player.doOnePlacement("Submarine", (p) -> player.shipFactory.makeSubmarine(p));
    assertTrue(bytes.toString().contains("Player A Where do you want to place a Submarine?"));
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 0)), "Submarine was not placed as expected.");
    bytes.reset(); // Reset the stream for the next test
    
    // Continue similar tests for other ship types, e.g., Destroyer, Battleship, Carrier
    // Ensure you reset the bytes stream and possibly the input reader if needed between tests
  }
  @Test
  void testDoPlacementPhaseDifferentShips() throws IOException{
    String simulatedInput = 
      "A0V\n" + // Submarine
      "A1V\n" + // Submarine
      "A2V\n" + // Destroyer
      "A3V\n" + // Destroyer
      "A4V\n" + // Destroyer
      "A5V\n" + // Battleship
      "A6V\n" + // Battleship
      "A7V\n" + // Battleship
      "A8V\n" + // Carrier
      "B9V\n";  // Carrier
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 10, simulatedInput, bytes); // Adjust board size as needed
    
    player.doPlacementPhase();
    String outputStr = bytes.toString();
    //System.out.print(outputStr);
    assertTrue(outputStr.contains("Player " + player.getName() + ": you are going to place the following ships"));
    assertTrue(bytes.toString().contains("Player A Where do you want to place a Submarine?"));
    assertTrue(bytes.toString().contains("Player A Where do you want to place a Destroyer?"));
    assertTrue(bytes.toString().contains("Player A Where do you want to place a Battleship?"));
    assertTrue(bytes.toString().contains("Player A Where do you want to place a Carrier?"));
    
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 0)), "Ship at A0 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 1)), "Ship at A1 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 2)), "Ship at A2 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 3)), "Ship at A3 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 4)), "Ship at A4 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 5)), "Ship at A5 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 6)), "Ship at A6 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 7)), "Ship at A7 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 8)), "Ship at A8 not found");
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(1, 9)), "Ship at B9 not found");
  }
  @Test
  void testDoOnePlacementWithInvalidFormat() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader input = new BufferedReader(new StringReader("NotAValidFormat\nA0V\n"));//simulate an invalid input and a  valid input
    TextPlayer player = new TextPlayer("A", new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(null), 'X'), input, output, new V1ShipFactory());
    
    // Test placement for Submarine
    player.doOnePlacement("Submarine", (p) -> player.shipFactory.makeSubmarine(p));
    assertTrue(bytes.toString().contains("Invalid placement"));
    assertTrue(bytes.toString().contains("Where do you want to place a Submarine?"));
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 0)));
  }
  @Test
  void testDoOnePlacementWithInvalidFormat2() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader input = new BufferedReader(new StringReader("A0Q\nAAV\nA0V\n"));//simulate an invalid input and a  valid input
    TextPlayer player = new TextPlayer("A", new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(null), 'X'), input, output, new V1ShipFactory());
    
    // Test placement for Submarine
    player.doOnePlacement("Submarine", (p) -> player.shipFactory.makeSubmarine(p));
    assertTrue(bytes.toString().contains("Invalid placement"));
    assertTrue(bytes.toString().contains("Please try again."));
    assertTrue(bytes.toString().contains("Where do you want to place a Submarine?"));
    assertNotNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 0)));
  }
  @Test
  void testDoOnePlacementEOF() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader input = new BufferedReader(new StringReader(""));//simulate an empty input
    TextPlayer player = new TextPlayer("A", new BattleShipBoard<>(10, 10, new NoCollisionRuleChecker<>(null), 'X'), input, output, new V1ShipFactory());
    //Exception exception = assertThrows(EOFException.class, () -> player.doOnePlacement("Destroyer", (p) -> player.shipFactory.makeDestroyer(p))); 
    // Test placement for Submarine
    player.doOnePlacement("Destroyer", (p) -> player.shipFactory.makeDestroyer(p));

    // Convert output stream to a string for analysis
    String outputStr = bytes.toString();

    // Verify that the EOF message is printed to the output
    String expectedEOFMessage = "End of input stream. Game will terminate.";
    assertTrue(outputStr.contains(expectedEOFMessage), "EOF message not found in output");
    assertNull(player.theBoard.whatIsAtForSelf(new Coordinate(0, 0)), "Expected no ship to be placed after EOF");
    //String expectedMessage = "End of input stream. Game will terminate.";
    //String actualMessage = exception.getMessage();
    //assertTrue(actualMessage.contains(expectedMessage));
    
  }
  @Test
  public void testReadAttackCoordinateValidInput() throws IOException {
    // Mocking user input for a valid attack coordinate
    String validCoord = "A5\n";
    BufferedReader reader = new BufferedReader(new StringReader(validCoord));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes);
    
    TextPlayer player = new TextPlayer("Player", new BattleShipBoard<>(10, 10, 'X'), reader, output, new V1ShipFactory());
    
    // Execute
    Coordinate coord = player.readAttackCoordinate();
    
    // Assert
    assertEquals(new Coordinate(0, 5), coord, "Should correctly parse and return the coordinate A5.");
  }
  @Test
  public void testValidAttackHitsAndMisses() throws IOException {
    StringReader input = new StringReader("A1\nB2\n");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader reader = new BufferedReader(input);
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 10, 'X');
    // Assume these methods exist and place ships accordingly
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> submarine = factory.makeSubmarine(new Placement(new Coordinate(1, 2),'H'));
    enemyBoard.tryAddShip(submarine);
    TextPlayer player = new TextPlayer("Player", new BattleShipBoard<Character>(10, 10, 'X'), reader, output, factory);
    player.doAttack(enemyBoard); // Simulate an attack on A1 (hit) and then B2 (miss)
    player.doAttack(enemyBoard);
    
    String outputStr = bytes.toString();
    //System.out.println(outputStr);
    assertTrue(outputStr.contains("You hit a Submarine")); // Check output contains "Hit!" for the first attack
    assertTrue(outputStr.contains("missed!")); // Check output contains "Miss!" for the second attack
  }
  @Test
  public void testReadAttackCoordinate_InvalidInput() throws IOException {
    String data = "Invalid\nC4\n";
    BufferedReader reader = new BufferedReader(new StringReader(data));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes);
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 10, 'X');
    TextPlayer player = new TextPlayer("Player", new BattleShipBoard<>(10, 10, 'X'), reader, output, new V1ShipFactory());
    
    Coordinate coord = player.readAttackCoordinate();
    String outputStr = bytes.toString();

    //System.out.println(outputStr);
    assertTrue(outputStr.contains("Invalid coordinate. Please try again."));
    assertEquals(new Coordinate("C4"), coord);
  }
  @Test
  public void testReadAttackCoordinate_EOF() throws IOException {
    BufferedReader input = new BufferedReader(new StringReader("")); // EOF simulated by empty input
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes);
    TextPlayer player = new TextPlayer("Player", new BattleShipBoard<>(10, 10, 'X'), input, output, new V1ShipFactory());
    
    Coordinate coord = player.readAttackCoordinate();
    String outputStr = bytes.toString();
    
    assertTrue(outputStr.contains("EOF detected. Exiting the game."));
    assertNull(coord); // Expect null due to EOF
  }
  @Test
  public void testReadAttackCoordinate_OutOfBounds() throws IOException {
    String data = "Z9\nA1\n"; // Z9 is out of bounds, A1 is valid
    BufferedReader reader = new BufferedReader(new StringReader(data));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes);
    TextPlayer player = new TextPlayer("Player", new BattleShipBoard<>(10, 10, 'X'), reader, output, new V1ShipFactory());
    
    Coordinate coord = player.readAttackCoordinate();
    String outputStr = bytes.toString();
    
    assertTrue(outputStr.contains("Invalid coordinate. Please try again."));
    assertEquals(new Coordinate("A1"), coord);
  }
   @Test
  public void testAttackHitsAndSunk() throws IOException {
    StringReader input = new StringReader("A1\nB2\nB3\n");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader reader = new BufferedReader(input);
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 10, 'X');
    // Assume these methods exist and place ships accordingly
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> submarine = factory.makeSubmarine(new Placement(new Coordinate(1, 2),'H'));
    enemyBoard.tryAddShip(submarine);
    TextPlayer player = new TextPlayer("Player", new BattleShipBoard<Character>(10, 10, 'X'), reader, output, factory);
    player.doAttack(enemyBoard); // Simulate an attack on A1 (hit) and then B2 (miss)
    player.doAttack(enemyBoard);
    player.doAttack(enemyBoard);
    
    String outputStr = bytes.toString();
    //System.out.println(outputStr);
    assertTrue(outputStr.contains("You hit a Submarine")); // Check output contains "Hit!" for the first attack
    assertTrue(outputStr.contains("missed!")); // Check output contains "Miss!" for the second attack
    assertTrue(outputStr.contains("You sunk a Submarine"));
   }
  @Test
  public void testPlayOneTurn() throws IOException {
    // Setup the input stream to simulate user input for an attack
    String simulatedUserInput = "D4\n"; // Assuming there's a ship at A1
    ByteArrayInputStream input = new ByteArrayInputStream(simulatedUserInput.getBytes());
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    
    ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(outputContent);
    
    Board<Character> enemyBoard = new BattleShipBoard<>(10, 10, 'X');
    // Place a ship on the enemy board at A1
    //Ship<Character> enemyShip = new RectangleShip<>("Destroyer", new Coordinate(0, 0), 1, 2, 's', '*');
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> enemyShip = factory.makeDestroyer(new Placement(new Coordinate(3, 4), 'H'));
    enemyBoard.tryAddShip(enemyShip);
    
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = new TextPlayer("Player1", new BattleShipBoard<>(10, 10, 'X'), reader, output, new V1ShipFactory());
    
    // Execute the method under test
    player.playOneTurn(enemyBoard, enemyView, "Enemy");
    
    // Verify the output
    String outputStr = outputContent.toString();
    assertTrue(outputStr.contains("Your Ocean"));
    assertTrue(outputStr.contains("Enemy's Ocean"));
    // Check if the output contains "Hit!" because we know there's a ship at A1
    assertTrue(outputStr.contains("hit"));
  }
  @Test
  public void testDoAttackingPhaseEndsWithWinner() throws IOException {
    // Prepare simulated user inputs for attacks, including newline characters to simulate pressing Enter
    String simulatedUserInputs = "A1\nA2\nA3\n"; // Assume these coordinates will lead to hitting and sinking a ship
    ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedUserInputs.getBytes());
    System.setIn(testInput);
    
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    PrintStream testOut = new PrintStream(testOutput);
    System.setOut(testOut);
    
    // Setup players, boards, and ships
    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
    BufferedReader reader2 = new BufferedReader(new StringReader(""));
    BattleShipBoard<Character> board1 = new BattleShipBoard<>(10, 10, 'X');
    BattleShipBoard<Character> board2 = new BattleShipBoard<>(10, 10, 'X');
    TextPlayer player1 = new TextPlayer("Player1", board1, reader1, System.out, new V1ShipFactory());
    TextPlayer player2 = new TextPlayer("Player2", board2, reader2, System.out, new V1ShipFactory());
    
    // Manually add a ship to player2's board at a known location that matches the simulatedUserInputs
    Ship<Character> shipToBeSunk = player2.shipFactory.makeDestroyer(new Placement(new Coordinate(0, 1), 'H'));
    Ship<Character> player1Ship = player1.shipFactory.makeSubmarine(new Placement(new Coordinate(5, 4), 'V'));
    board2.tryAddShip(shipToBeSunk);
    board1.tryAddShip(player1Ship);
    
    // Run the attacking phase
    player1.doAttackingPhase(player2);
    assertTrue(player2.theBoard.allShipsSunk());
    // Check the output to confirm a winner is declared
    String output = testOutput.toString();
    System.out.println(output);
    assertTrue(output.contains("Player1 wins!")); // Adjust this assertion based on your game's win message format
    
    // Clean up by resetting System.in and System.out to their original values
    System.setIn(System.in);
    System.setOut(System.out);
  }
  @Test
  public void testDoAttackingPhaseEndsWithEnemyWinner() throws IOException {
    // Prepare simulated user inputs for attacks, including newline characters to simulate pressing Enter
    String simulatedUserInputs = "A0\nB0\nC0\n"; // Assume these coordinates will lead to hitting and sinking a ship
    ByteArrayInputStream testInput = new ByteArrayInputStream(simulatedUserInputs.getBytes());
    System.setIn(testInput);
    
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    PrintStream testOut = new PrintStream(testOutput);
    System.setOut(testOut);
    
    // Setup players, boards, and ships
    BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));
    BufferedReader reader1 = new BufferedReader(new StringReader(""));
    BattleShipBoard<Character> board1 = new BattleShipBoard<>(10, 10, 'X');
    BattleShipBoard<Character> board2 = new BattleShipBoard<>(10, 10, 'X');
    TextPlayer player1 = new TextPlayer("Player1", board1, reader1, System.out, new V1ShipFactory());
    TextPlayer player2 = new TextPlayer("Player2", board2, reader2, System.out, new V1ShipFactory());
    
    // Manually add a ship to player2's board at a known location that matches the simulatedUserInputs
    Ship<Character> shipToBeSunk = player1.shipFactory.makeDestroyer(new Placement(new Coordinate(0, 0), 'V'));
    Ship<Character> player2Ships = player2.shipFactory.makeBattleship(new Placement(new Coordinate(5, 5), 'V'));
    board1.tryAddShip(shipToBeSunk);
    board2.tryAddShip(player2Ships);
    
    // Run the attacking phase
    player1.doAttackingPhase(player2);
    
    // Check the output to confirm a winner is declared
    String output = testOutput.toString();
    assertTrue(player1.theBoard.allShipsSunk());
    System.out.println(output);
    assertTrue(output.contains("Player2 wins!")); // Adjust this assertion based on your game's win message format
    
    // Clean up by resetting System.in and System.out to their original values
    System.setIn(System.in);
    System.setOut(System.out);
  }
  
}
