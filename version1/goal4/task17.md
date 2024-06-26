## Task 17.  Place all ships

Let's take a look at doPlacement for a moment
```java
  public void doOnePlacement() throws IOException {
    Placement p = readPlacement("Player " + name + " where do you want to place a Destroyer?");
    Ship<Character> s = shipFactory.makeDestroyer(p);
    theBoard.tryAddShip(s);
    out.print(view.displayMyOwnBoard());
  }
```
This does basically what we need, we just need to make it a little more general.
Specifically it should not always say "Destroyer" nor should it always call
makeDestroyer (it should call makeSubmarine, etc).

Passing in a string for the ship name is easy, but what about the different function
we want to do? Java has lambdas (anonymous functions). So we could make this
```java
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    Placement p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
    Ship<Character> s = createFn.apply(p);
    theBoard.tryAddShip(s);
    out.print(view.displayMyOwnBoard());
  }
  ```

Note that Function<Placement, Ship<Character>> is an object an apply method that takes a Placement and returns a Ship<Character>
(there are a few other methods in it too).  Importing it will have many choices, the one you want is java.util.function.Function.

then we would call it like this
```java
  doOnePlacement("Submarine", (p)->shipFactory.makeSubmarine(p));
  doOnePlacement("Submarine", (p)->shipFactory.makeSubmarine(p));
  doOnePlacement("Destroyer", (p)->shipFactory.makeDestroyer(p));
  doOnePlacement("Destroyer", (p)->shipFactory.makeDestroyer(p));
  doOnePlacement("Destroyer", (p)->shipFactory.makeDestroyer(p));
  //and similarly for the other 5 ships.
```
The `doOnePlacement` function doesn't seem too bad... but we need to have a less painful use of it.

Instead, let us set up some data so we can iterative over it.  First, we need an ArrayList
of the ship names that we want to work from.  Then we want a map from ship name to the lambda to create it.
Lets add these two fields to TextPlayer:
```java
  final ArrayList<String> shipsToPlace;
  final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
```
  Be sure to initialize them in the constructor.

Then lets setup two methods:
```java
   protected void setupShipCreationMap()

   protected void setupShipCreationList()
```
  In the first of these, we'll put the String -> lambda mappings into shipCreationFns, for example:
```java  
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
```
  You should do the other 3.

  In the second of these, we'll put in the ships we want to add, in the order
  we want to add them.  If there are multiple copies, we'll include the item
  the appropriate number of times (e.g., 2 copies of "Submarine").
  We could do
```java
  shipsToPlace.add("Submarine")
  shipsToPlace.add("Submarine")
  shipsToPlace.add("Destroyer")
  shipsToPlace.add("Destroyer")
  shipsToPlace.add("Destroyer")
  //and more other ships
```
  but that brings us back to the painfulness we want to avoid.  Instead, lets do
```java
     shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
     shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
     //and similarly for the other ships
```
That seems much nicer!

Now, call these functions from the constructor.  We'll note that we
have left some fleixbility for change in this: a subclass could
override these methods to change what ships are made in what order.
However, the flexibility is somewhat limited by the
construction order problem we encountered back when we were
constructing RectangleShip.   Any child class that overrides
these would only be able to write "pure" functions---they would
not be able to use any parameters passed to the constructor,
since the constructor would not be able to put it in fields
before these methods get called!
We could give more flexibility abstracting this part
out into a separate class... Maybe we'll refactor that if we need to...


Now that we have done that setup, we can just go write one short loop
in doPlacementPhase, in which we iterate over the shipsToPlace,
lookup the creation function, and call doOnePlacement with that information.


As usual, write test cases, update your test_main input/output (now its going
to involve placing 20 ships!    This is why we would rather record it
than write it all out by hand...). I had to make one small change in
test_do_one_placement also.

One word of caution: now that we are making changes that are all
integrated into the main program, it may seem tempting to just
use the test_main to test everything.  Doing so is a really bad idea.
Why?  
    - Two reasons:
        - (1) It is much easier for us to mess up with test_main, since we are
            checking the behavior by hand as we go.  We really want to have
            as much code tested as possible separately from this.
        - (2) If something goes wrong, and test_main fails it only tells us
            "something is broken somewhere in the program".  If something
            fails in another test cases it tells us "this particular method is broken".
            The later is MUCH MUCH MUCH easier to debug!

    Remember: Hours of debugging can save you minutes of testing!
    (What does this mean?  You skip minutes of time writing test cases
        and add hours of debugging time)
One good way to check that you aren't cutting corners here is to
put @Disabled on test_main and see where you still get coverage. You
should still have most of App.java (probably all except main) covered
with the other tests.


Make sure your documentation is up to date... and then git commit and push.

All that is left in the placement phase is error handling, in task 18 :)

Before you do that, however, think for a moment about the decision to
reorder our tasks.  Namely, that we have already refactored TextPlayer.
Do you see now why we did this? If not, we were going to have
a lot more things in App to move around.  


Done with 17! 

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 18](./task18.md)