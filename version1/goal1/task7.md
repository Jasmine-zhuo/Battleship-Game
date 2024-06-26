## Task 7. Add ships to BattleShipBoard

Now, let us head back to BattleShipBoard.java (remember: you created this file back in Step 1).
We want to add an ArrayList of Ships to our BattleShipBoard.  An ArrayList is very much
like a std::vector from C++.  Java also has Vector, which behaves pretty much the same way.
The difference between Vector and ArrayList is that Vector is thread-safe (it locks a mutex
for every operation).  Since we don't need that thread safety, we'll juse ArrayList.

One thing we need to handle here is that Ship isn't a type, its a generic (we need Ship<something>).
What type do we want?  On the one hand, we want Character, and could just use that. On the other,
we put in the `<T>` to have flexibility later.  We may as well go ahead and put that flexibility
into BattleShipBoard and Board now.  That will require a few changes, but not many.

here are the changes that we make for this:
- (1) Change BattleShipBoard's delcaration to
     public class `BattleShipBoard<T>` implements `Board<T>`
- (2) Go to Board.java and change its declaration to
     public interface `Board<T>`
- (3) Go to BattleShipBoardTest.java and use M-x query-replace to change Board
     to `Board<Character>`  in all the right places (it will ask you for each one:
     choose y to replace or n to skip).
- (4) In BoardTextView.java, make toDisplay and the constructor parameter be
     `Board<Character>` instead of Board
- (5) In BoardTextViewTest.java, change Board to `Board<Character>` and
     BattleShipBoard to `BattleShipBoard<Character>`
     Note that because we abstracted out emptyBoardHelper, these
     should each only occur in a few places (I had once for my non-error
     cases and two error-case boards).
Before we proceed, run all your tests again.  This will not only check that
your code compiles with these changes, but also that we did not mess anything up!
We would much rather catch a problem now, than later...

Now that we have finished that refactoring, we are ready to add
```java
   final ArrayList<Ship<T>> myShips;
```
to our BattleShipBoard class.  When you do this, ArrayList will get a red underline,
since it is in java.util and we need to import that.  Hit "C-c TAB" and Emacs will
automatically add the import to your java file.  Go into the constructor
and initalize myShips to a new ArrayList (which will be empty).

Note that we are NOT going to write a getter for this list.  Nothing outside
of this class should ever know we keep our ships in an ArrayList, or be able to operate
directly on that ArrayList. If we later decide to change to e.g.
```java
HashMap<Coordinate, Ship<T>> myShips; //maybe we need faster lookup?
```
we want to ONLY change code in THIS class and no others.   With mention of a HashMap
(i.e. hashtable), I suspect some of you are wondering "why not use one? Wouldnt it be faster?"
For our purposes N=10 (we have 10 ships in our game), so we aren't concerned about asymptotic
performance.   In fact, most of the time for this game will be waiting for the user: it won't matter.
But, if we ever needed to change that, we want that change to be easy.

Instead, we are going to write
```java
 public boolean tryAddShip(Ship<T> toAdd)
```
which for now will add the ship to the list and return true.  Later (tasks 13-15)
we will make it so that it checks the validity of the placement and returns
true if the placement was ok, and false if it was invalid (and thus not actually placed).
Note that we name our method to indicate that it might not succeed.

We'll also write:
```java
  public T whatIsAt(Coordinate where) {
    for (Ship<T> s: myShips) {
      if (s.occupiesCoordinates(where)){
        return s.displayInfoAt(where);
      }
    }
    return null;
  }
```
This method takes a Coordinate, and sees which (if any) Ship
occupies that coordinate.  If one is found, we return whatever
displayInfo it has at those coordinates (for now, just 's').  If
none is found, we return null.  We should also take a moment
to add these two methods (tryAddShip and whatIsAt) to the Board
interface, since they belong there, so go do that now too.


Let's go test this before we proceed.  Head back over to
BattleShipBoardTest (C-x t) and test this out.  In particular, we
should make a BattleShipBoard, check that it has no ships anywhere
(all coordinates return null).  Then we should start adding ships and
make sure the right coordinates return the Character 's'.  Lets write
a helper that a Character[][] for the values we expect, so that we can
easily check all coordinates in our BattleShipBoard have the right
value.  We might start to write this helper as:
```java
 private void checkWhatIsAtBoard(BattleShipBoard<Character> b, Character[][] expect) 
```
but then we might realize that we can make it generic int `<T>` with no other changes:
```java
 private <T> void checkWhatIsAtBoard(BattleShipBoard<T> b, T[][] expected)
```
So we might as well do that now and have it be more useful later!
Use that helper and write some test cases that make sure that (a) the board
starts empty (whatIsAt returns null for all coordinates in the board),
and that when you add ships, the right coordinates return 's' (and
that tryAddShip returns true).   As you do this, think about if you should abstract
out any other helpers in your testing.

You might be wondering if we should add error checking to the whatIsAt method
(to check if the coordinate is in bounds).  There is a bit of a tradeoff here.
On the one hand, if some other part of code erroneously queries an invalid coordinate,
then we would want to fail quickly and with a meaningful error message.  On the other hand,
we sometimes find it useful to be able to query things that are just out of bounds to make
other code more uniform (think back to minesweeper in 551, and how much easier it would have
been to count mines if you could have queried the squares just off the board).  For now,
we'll leave it up to you to decide if this method should check for coordinates out of bounds!

Make sure you have 100% coverage (except on the placeholder BasicShip.java and the
auto-generated App.java), and that you have documented everything.

Then git commit and git push

You finished step 7.

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 8](./task8.md)