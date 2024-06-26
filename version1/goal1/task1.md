## Task 1. BattleShipBoard class

Now lets write the simplest BattleShipBoard class we can write.  
Hit `C-x C-f` to open a new file and specify:
`app/src/main/java/edu/duke/YOURNETID/battleship/BattleShipBoard.java`

Note you should be able to `TAB` complete most of that
`app/sr[TAB]ma[TAB]j[TAB][TAB][TAB][TAB][TAB]BattleShipBoard.java`

- As this will be the first java file in your project, Emacs should ask you how you want your project
to be setup (where the root of the project is).  As you should be working in a git repository,
the first option (lowercase i) should be the top of your git repository, so choose that.
If that is not correct, choose captial I and enter the correct top directory of your project (but then
get your git setup fixed ASAP).

You should now have a blank `BattleShipBoard.java`.   
In that buffer, hit `C-c C-s` to ask Emacs
to create a skeleton class (or write it yourself):

```java
package edu.duke.adh39.battleship;

public class BattleShipBoard {

}
```

- Now your first task is to make the following:
	- (1) a private final int width
	- (2) a public int getWidth()
      - Note, we are NOT making setWidth nor setHeight: the width/height are fixed once created
      - Note: you can use "M-g g" to do this.  It will only generate a getter since width is final
      - M-g g doesn't indent the generated code nicely. Don't forget that "C-c C-f" will format your code.
	- (3) a private final int height
      - Again, you can just have Emacs makes this for you with "M-g g".
	- (4) a public int getHeight()
	- (5) a constructor that takes two ints and initializes the width and height with them
  
- Once you have written that, save the file. 

- Now it is time to write testcases!   I know, you are thinking "there is no way my code is wrong..."
but you would be surprised.  Hit "C-x t" which is bound to "switch between my main code
and my test code".  You should get switched to `BattleShipBoardTest.java` in your test
directory, and it should be filled in with this skeleton:

```java
package edu.duke.adh39.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  @Test
  public void test_() {

  }

}
```

>[!CAUTION]
> In this step-by-step we always will refer to the package `edu.duke.adh39`, however, you need to make sure it goes to the right net ID, your net id, so replace as needed.

Note that test_ is a placeholder, and you should fill that in with the name of your first
test.  Lets make our first test be test_width_and_height.  Let's start with a quick
test that our constructor makes a board and the getWidth/getHeight return the right values.
Your entire java file should look like this now:
```java
package edu.duke.adh39.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  @Test
  public void test_width_and_height() {
    BattleShipBoard b1 = new BattleShipBoard(10, 20);
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

}
```
Now hit "C-c C-t" to run your test cases.  We should have 100% coverage on our code, but
the auto-generated "App.java" is not covered---don't worry about that (nor the auto
generated AppTest.java) for now.

Are we done here?   Not really.  We should make sure our width and height are positive.
Lets go back to BattleShipBoard.java.  You can either use "C-x b" to switch buffers
in general.  However, if you are in BattleShipBoardTest.java, you can use "C-x t"
(switch between main code <-> test code).

Let's put some code that checks for invalid width/height and throws an exception.  Here
is what my constructor looks like now:
```java
  public BattleShipBoard(int w, int h) {
    if (w <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
    }
    if (h <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
    }
    this.width = w;
    this.height = h;
  }
```
If you hit "C-c C-t" (run test cases) you will see that these lines are not covered!
This should not be surprising as we did not write test cases for them yet.. So lets go do that now.
Head back to BattleShipBoardTest.java (again, "C-x t" in BattleShipBoard.java will take you there,
or you could keep them both open in split windows).

Now let's write a new test (another method with @Test on it) for test_invalid_dimension.

We are going to use assertThrows since we want to assert that our code throws an exception.
Here is my test_invalid_dimensions:
```java
  @Test
  public void test_invalid_dimensions() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(10, 0));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(0, 20));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(10, -5));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(-8, 20));
  }
```

Note that this may look a little weird, but the first argument to assertThrows is the Class
that you expect to have thrown.  Every classname (e.g., IllegalArgumentException) has a ".class"
which is the Class object for that particular class.

The second argument is a lambda (an anonymous function) that is what we want to do.  Basically,
JUnit will execute that lambda inside a try-catch, and make sure it catches the exception class
we specify.  To break it down a bit:
```java
  () -> new BattleShipBoard(10, 0)
  -- -- ----------------------------
   \  \   \
    \  \   -- the body of the "function" we want to execute
     \  --- the arrow is the syntax for a lambda
      ---- this is the argument list.  Our function takes no arguments.
           If it took arguments, we'd write them here, e.g. (int x, int y) -> x + y
```
Go ahead and hit "C-c C-t" again to run all your tests.  Everything should pass,
and you should have 100% coverage on your BattleShipBoard :)

The last thing we are going to do before we declare Step 1 as done is make
our Board interface.  Go back to BattleShipBoard.java (so you are in the right
directory) and then do "C-x C-f" to create a new file. Make Board.java
You can use "C-c C-s" to create a skeleton class, but then change "class"
to "interface.

Lets put two methods: getWidth and getHeight in it:

```java
package edu.duke.adh39.battleship;

public interface Board {
  public int getWidth();

  public int getHeight();
}
```

Now, lets go back over to our BattleshipBoardTest and change our test_width_and_height
to make use of that interface:
```java
  @Test
  public void test_width_and_height() {
    Board b1 = new BattleShipBoard(10, 20);
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }
```
Oh no! Emacs has a red underline under new BattleShipBoard(10,20);  what did we do wrong?
If you put your cursor on "new" you will see that the types are incompatible.  We
forgot to make BattleShipBoard actually implement the interface. Go change
BattleShipBoard to: `public class BattleShipBoard implements Board`
and then run all your test cases.

We also need to take a moment to document our code.  We should go write [Javadocs](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) for
all the interfaces, classes, and methods in our main code.  Here is the documentation
on my constructor, as an example:
```java
 /**
   * Constructs a BattleShipBoard with the specified width
   * and height
   * @param w is the width of the newly constructed board.
   * @param h is the height of the newly constructed board.
   * @throws IllegalArgumentException if the width or height are less than or equal to zero.
   */
```
Ok, great you have done step 1 (only 21 more to go!).  Git commit, and git push.

- A few notes before we proceed:
    - You can read more about [lambdas](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
    - Check the  documentation for [JUnit asserts](https://junit.org/junit5/docs/5.0.1/api/org/junit/jupiter/api/Assertions.html)
        - There is a REALLY long list of things you can assert.  Most often you want
            - assertEquals
            - assertThrows
            - assertNull
            - assertNotNull
            - assertTrue
            - assertFalse

>[!NOTE]
> - Do take note of the amount of code we wrote before we tested it.  Not much!
> - Note that a Test Driven Development approach would be to write the tests FIRST, before we write any code.

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 2](./task2.md)