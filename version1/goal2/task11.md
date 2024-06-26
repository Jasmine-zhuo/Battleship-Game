## Task 11. Add "hit tracking" to BasicShip

Now we want to handle the hit tracking of our BasicShips.

This is primarily going to involve these three methods:
```java
	public boolean isSunk() 
	public void recordHitAt(Coordinate where)
	public boolean wasHitAt(Coordinate where)
```
And we also want to fix our "TODO" on `public T getDisplayInfoAt(Coordinate where)`.

The first thing I notice in preparing to work on these
is that for all of them, we said (in our documentation
in Ship.java) that the three methods that take a Coordinate
throw an `IllegalArgumentException` if the Coordinate is not
in this ship.  We may as well go ahead and write
a protected helper method to abstract out this checking/exception
throwing:
```java
  protected void checkCoordinateInThisShip(Coordinate c)
```
All this method does is check if c is part of this ship (in myPieces),
and if not, throw an IllegalArgumentException.

Now, go write and test the four methods listed above.
I'd recommend writing recordHitAt and wasHitAt first
(these should each be two lines long), then testing that pair.
Even though we are editing BasicShip, we are going to do our testing
in RectangleShipTest.java (since BasicShip is abstract now).
Then write isSunk (also a short method---mine is 4 lines of code,
not counting the two lines that are just close braces). Then
test that.

Finally, update `getDisplayInfoAt`.
Be sure to test this for both true and false values.
It does not have an "if" inside of it, so branch coverage won't
make sure you cover both cases.  The "if" is inside of SimpleShipDisplayInfo,
which you already have coverage of from other tests.  We still need
to make sure that we did this method correctly though!

Now all of our placeholders are gone from BasicShip, so we should
truly have 100% coverage on our code (for me, that is presently
320/320 statements and 50/50 branches).


You finished step 11.

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 12](./task12.md)