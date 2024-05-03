package edu.duke.ww202.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import java.nio.charset.StandardCharsets;
class AppTest {
  /**
  private ByteArrayOutputStream bytes;
  private PrintStream ps;
  private Board<Character> b;
  private App app;
  @BeforeEach
  public void setUp(){
    bytes = new ByteArrayOutputStream();
    ps = new PrintStream(bytes, true);
    b = new BattleShipBoard<Character>(10, 20);
    StringReader sr = new StringReader("B2V\nC8H\nA4V\n");
    //StringReader sr = new StringReader
    app = new App(b, sr, ps);
  }

  // Compare the output with the expected Prompt + expected Board
  // if not the same, assert fails
   
  private void assertOutputMatchesPromptAndBoard(String expectedPrompt, String expectedBoard){
    String actualOutput = bytes.toString();
    assertTrue(actualOutput.startsWith(expectedPrompt));
    assertTrue(actualOutput.endsWith(expectedBoard));
    assertEquals(expectedPrompt + expectedBoard, actualOutput);
  }
  *///modification of AppTest
  //disable testcase fo now
  //@Disabled
  @Test
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  //lock to avoid running Junit in parallel
  void test_main() throws IOException{
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");//back
    assertNotNull(input, "input.txt could not be found");//back
    //**********for test, add manual input
    //InputStream input = new ByteArrayInputStream("A2V\nG6H\n".getBytes(StandardCharsets.UTF_8));
    //System.setIn(input);

    //*********for test, print the input file
    //byte[] inputBytes = input.readAllBytes();
    //String inputContent = new String(inputBytes);
    //System.out.println("Input content: " + inputContent); // Diagnostic output
    
    // Reset the input stream since it was consumed by readAllBytes()
    //input = new ByteArrayInputStream(inputBytes);
    //**********for test, print the input file
    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");
    assertNotNull(expectedStream, "output.txt could not be found");
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    try {
      System.setIn(input);
      System.setOut(out);
      App.main(new String[0]);
    }
    finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
    String expected = new String(expectedStream.readAllBytes());
    String actual = bytes.toString();
    assertEquals(expected, actual);
    //System.out.println(actual);
    //System.out.println("input: " + input);
  }
}
