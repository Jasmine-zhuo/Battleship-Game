## Task 2. BoardTextView that only does empty boards

Our next task is to make a `BoardTextView` that ONLY handles empty boards.  Go to one of
your main source files (Board.java or BattleShipBoard.java) so you are in the right
directory and create BoardTextView.java.  Use "C-c C-s" to make a skeleton for it,
and then create a field for the Board to display and a constructor that
initializes it.  E.g.:
```java
package edu.duke.adh39.battleship;

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
  private final Board toDisplay;
  /**
   * Constructs a BoardView, given the board it will display.
   * @param toDisplay is the Board to display
   */
  public BoardTextView(Board toDisplay) {
    this.toDisplay = toDisplay;
  }
  
}
```
- Next, lets add
```java
 public String displayMyOwnBoard() {
    return ""; //this is a placeholder for the moment
 }
```
Let us take a minute to try out TDD (Test Driven Development) this time around.
That means we are goign to write our test cases FIRST then the code.

Hit "C-x t" to switch to the test code for BoardTextView (you will get
a new `BoardTextViewTest.java` class in your test directory). 

Let's make this our first test:
```java
package edu.duke.adh39.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {
  @Test
  public void test_display_empty_2by2() {
    Board b1 = new BattleShipBoard(2, 2);
    BoardTextView view = new BoardTextView(b1);
    String expected=
      "  0|1\n"+
      "A  |  A\n"+
      "B  |  B\n"+
      "  0|1\n";
    assertEquals(expected, view.displayMyOwnBoard());
  }

}
```
Note that while very new versions of Java support "text blocks" not
all tools support it (including clover).  I also prefer "old school"
string concatenation with explcit new lines:  it makes it clearer
where whitespace is, and avoid *painful* problems where you have
an extra space on a line that you don't see that throws off your test.

Now, run the test and it should fail (remember
the TDD model is write test -> fail test -> write
code to make test pass -> repeat).  Now let's go back to BoardTextView.java
and write the code for this.

The first thing I want to do here is abstract out the code to make the "header"
(the first row with the 0|1|2...), especially since we do it
twice (at the top and bottom of the board). So I write
```java
 /**
   * This makes the header line, e.g. 0|1|2|3|4\n
   * 
   * @return the String that is the header line for the given board
   */
  String makeHeader() {
    StringBuilder ans = new StringBuilder("  "); // README shows two spaces at
    String sep=""; //start with nothing to separate, then switch to | to separate
    for (int i = 0; i < toDisplay.getWidth(); i++) {
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    return ans.toString();
  }
```
Since I abstracted that out, I can refactor my test to *explicitly* check that.  Even
though the rest of of my code is still a placeholder, I should pass that and fail
at the prior point:
```java
package edu.duke.adh39.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {
  @Test
  public void test_display_empty_2by2() {
    Board b1 = new BattleShipBoard(2, 2);
    BoardTextView view = new BoardTextView(b1);
    String expectedHeader= "  0|1\n";
    assertEquals(expectedHeader, view.makeHeader());
    String expected=
      expectedHeader+
      "A  |  A\n"+
      "B  |  B\n"+
      expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }

}
```
However, running this fails at the first assert (for expectedHeader)---I thought that
part was right.
```java
edu.duke.adh39.battleship.BoardTextViewTest > test_2by2() FAILED
    org.opentest4j.AssertionFailedError: expected: <  0|1
    > but was: <  0|1>
```
Ooops! I forgot a newline at the end.  We can fix that with
`ans.append("\n");`
right before we return.  Now we run our tests again and fail in the expected
place.  (Again, notice how little code we write before we test!).

Go ahead and finish displayMyOwnBoard(), remembering that right now, we
ONLY want this to work for EMPTY boards. 

As you write this function, you are probably thinking "what if
we had more than 10 columns or more than 26 rows???"  That is a GREAT
question (and we are glad you are thinking it!).  There are many valid approaches:
we could consider making the columns wider to display larger numbers,
using other symbols for the rows, etc.  However, the requirements for THIS
assignment don't need anything larger than that.  So let's make our BoardTextView
constructor enforce that rule.  You can make a BattleshipBoard larger than 10x26,
but you can't use it with BoardTextView.
```java
 /**
   * Constructs a BoardView, given the board it will display.
   * 
   * @param toDisplay is the Board to display
   * @throws IllegalArgumentException if the board is larger than 10x26.  
   */
  public BoardTextView(Board toDisplay) {
    this.toDisplay = toDisplay;
    if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
      throw new IllegalArgumentException(
          "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
  }
```
Note the addition of the @throws documentation.
What should you do now?

Write tests!  Head back over to BoardTextViewTest.java and write
```java
  @Test
  public void test_invalid_board_size() {
    Board wideBoard = new BattleShipBoard(11,20);
    Board tallBoard = new BattleShipBoard(10,27);
    //you should write two assertThrows here
  }
```
Run your tests, make sure they all pass and that your coverage is 100% :)

Even though we have 100% coverage, we still have not tested our displayMyOwnBoard() method
very well: we just did a 2x2 test case.  For one thing, 2x2 is pretty small.  For another,
2x2 has width=height.  What if we messed up somewhere and go those backwards?   We should
at least test with width > height and height > width to rule out a lot of potential
mistakes in that area.

You should now write

`public void test_display_empty_3by2()`
and
`public void test_display_empty_3by5()`

As their names suggest the first should test a 3x2 board, and the second
should test a 3x5 board.  Don't forget to put @Test on them!

Did you find yourself copying and pasting code when writing these tests?
Don't forget that it is better to abstract out the code into a method
than to copy and paste!   

I abstracted out:
```java
 private void emptyBoardHelper(int w, int h, String expectedHeader, String expected Body){
    Board b1 = new BattleShipBoard(w, h);
    BoardTextView view = new BoardTextView(b1);
    assertEquals(expectedHeader, view.makeHeader());
    String expected = expectedHeader + body + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }
``` 

and then each of my three tests just say what the expected header and body are and call
that method.

Make sure all your tests pass.   Are you feeling confident in this code?

Are you missing any documentation you need to write?   Best to do it now
rather than trying to do it all at once at the end...

You finished step 2.


***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 3](./task3.md)