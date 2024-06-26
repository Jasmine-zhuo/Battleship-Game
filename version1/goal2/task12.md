## Task 12. Add name and display info to BasicShip

This task is going to change slightly relative to its
original description (which is unmodified above).

Originally, I had planned to put the ShipDisplayInfo
into BasicShip here.  However, when I got to task 10,
it looked like a really good time to do that.   So I went
ahead and did it then.  Plans change: adapting is good.

This also means that we could handle getDisplayInfoAt
in task 11, which is great.

On the flip side, I have realized that our RectangleShip
creation is getting a bit unweidly.  For any real ships,
we are going to need to pass 4 or 5 parameters to the constructor---
and that is before we add a name in this step.  Accordingly,
Im going to add a bit to this step.  We are going to
use the Abstract Factory design pattern to create our ships.
You'll learn about design patterns and Abstract Factory in particular
in class (if you haven't already).  

Let's start with adding a name to our ships.  We want this name
(like "submarine" or "destroyer") because we need to print
messages like "You hit a carrier!" which include the name.

Lets go to Ship.java and add this to our interface:
```java
  /**
   * Get the name of this Ship, such as "submarine".
   * @return the name of this ship
   */
 public String getName();
```

Now go to RectangleShip, and add a final String field for the name,
and a public String getName() that returns that name.  I made
two of my three constructors take name as a parameter, but left the third
one to not take it:
```java
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo) {
    super(makeCoords(upperLeft, width, height), myDisplayInfo);
    this.name = name;
  }
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit));
  }
  public RectangleShip(Coordinate upperLeft, T data, T onHit) {
    this("testship", upperLeft, 1, 1, data, onHit);
  }
```
I did this because the last constructor is just a convience constructor to avoid changes in our code
as we change the behavior from "Always 1x1" to "width x height".  Since we only use that in testing,
we'll just put "testship" for the name.  If we ever see that in actual game play, we messed up somewhere
(and constructed a 1x1 ship, which is not correct for our game!).

There are a few places in our testing code where we are using the constructors that now take a name
parameter.  We need to go change those to take a name (like "submarine") quickly.  While we are
at it, we may as well throw in an assert to check that getName worked correctly.

Make sure you have 100% test coverage and that your documentation is up to date.

As we mentioned, we are also going to use the AbstractFactory design pattern to
simplify our ship making.  This will also help with changes in Version 2, as
well as if you want to make a GUI.

- For any of these options (Version 1, Version 2, GUI) we need to make 4 kinds of ships:
    - `makeSubmarine()`
    - `makeDestroyer()`
    - `makeBattleship()`
    - `makeCarrier()`

We want the same 4 creation operations, but HOW they work is different.

For Version 1, these are going to make RectangleShip<Character>s (with appropriate dimensions),
and pass in the appropriate letters ('s' 'd' etc) to the constructor.

For Version 2, two of these methods will need to construct our strangely shaped ships,
which are not going to be RectangleShip<Character>---they will instead be some other classes
you make.

For a GUI, we might construct the same types of Ships, but instead of
Rectangleship<Character>, we might Rectangleship<Color> or Rectangleship<ShipImageInfo>
(e.g., we might draw our ships as colored squares, or a more complicated thing that
 has one image per square that go together to draw the ship).

We could even imagine being able to select different rules (e.g., V1 or V2 or some others)
at the start of the game and they choosing a different way to construct the ships
based on that.

so lets go create `AbstractShipFactory.java`.  Here is what I put in mine:
```java
package edu.duke.adh39.battleship;

/**
 * This interface represents an Abstract Factory pattern for Ship creation.
 */
public interface AbstractShipFactory<T> {
  /**
   * Make a submarine.
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the Ship created for the submarine.
   */
  public Ship<T> makeSubmarine(Placement where);

  /**
   * Make a battleship.
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the Ship created for the battleship.
   */
  public Ship<T> makeBattleship(Placement where);

  /**
   * Make a carrier.
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the Ship created for the carrier.
   */

  public Ship<T> makeCarrier(Placement where);

  /**
   * Make a destroyer.
   * 
   * @param where specifies the location and orientation of the ship to make
   * @return the Ship created for the destroyer.
   */

  public Ship<T> makeDestroyer(Placement where);

}
```

Now, lets go create V1ShipFactory.java.   Make the skeleton, add implements AbstractShipFactory<Character>
then hit C-c C-a to add the methods required by the interface.

For V1, all of these do basically the same thing, but have different values they pass
to the constructor for RectangleShip.  Accordingly, you should abstract out most of hte work into
```java
 protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name)
```
Now, the various methods are just one line, e.g.:
```java
    @Override
    public Ship<Character> makeSubmarine(Placement where) {
      return createShip(where, 1, 2, 's', "Submarine");
    }
```

- As a quick reminder the ships are:
    - Submarine:   1x2 's'
    - Destroyer:   1x3 'd'
    - Battleship:  1x4 'b'
    - Carrier:     1x6 'c'

We'll note that createShip takes w and h assuming a vertical orientation.  It should
check for horizontal orientation and reverse those values (and if the orientation is invalid,
throw an exception).

Of course, we should go write tests for this!  As the behavior of the makeXXX methods
are all quite similar, so are the tests.  This suggests we want to use good abstraction
in our tests.

I found it convient to use the "variable arguments" feature of Java in writing my test
helper:

```java
private void checkShip(Ship<Character> testShip, String expectedName,
                       char expectedLetter, Coordinate... expectedLocs)
```

I pass it the ship I created, the expected name ("submarine"), letter ('s'),
and locations.  The ... means "0 or more Coordinates" and they end up in an array---that
is, inside this method, expectedLocs is a Coordinate[].  When I call the method, I pass
each element of the array as its own parameter, e.g.:
```java
 Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
 Ship<Character> dst = f.makeDestroyer(v1_2);
 checkShip(dst, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2));
```
Here locs would be the array with 3 coordings (1,2), (2,2) and (3,2).

Ok, now our Ships know their names, and we have a nice little factory to make them.
The last thing we are going to do is go to our App.java and integrate this change
into the main program.

We'll add a `final AbstractShipFactory<Character> shipFactory;`
and initialize it in the constructor to a `new V1ShipFactory()`.

Now lets find where we did `RectangleShip<Character> s = new RectangleShip<Character>(p.getWhere(), 's', '*');` and replace it with `Ship<Character> s  = shipFactory.makeDestroyer(p);`

If you run your tests now, they will fail since the expected
output describes a board with a 1 square "submarine" (not really a
submarine---a fake test ship), and we are now placing a 3 square
destoyer.

Lets go back and "play" the game
```bash
./gradlew installDist
./build/install/battleship/bin/battleship
```
Then type one placement (like A4V) and it should output the board.
We can then update our input/output files with
```bash
tee src/test/resources/input.txt | ./build/install/battleship/bin/battleship | tee src/test/resources/output.txt
```
Type one placement, then hit `ctrl-d`

I also had to update my other tests in AppTest to reflect the fact I was now putting a 3 square destroyer
instead of one 's'.  Once you are passing all your tests, make sure you have 100% coverage and
that you have all your documentation written!

That wraps up "Goal 2" which was to get make "real" ships.

Next up is Goal 3 (placement rules).

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!CAUTION]
> - **Generate a Release**
>   - Go to your GitHub Repository Page, and generate a new release. 
>       - Use `Extra Credit` as Tag. 
>       - Use `Goal 2` as Release name.

>[!NOTE]
> - You can now proceed to [Goal 3](../goal3/task13.md)