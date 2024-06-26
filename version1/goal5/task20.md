## Task 20. Display two boards side by side

Now it is time to display the two boards side by side.  Take a moment to think about how you
are going to do this before we proceed.


Were you thinking we'll rewrite displayAnyBoard to have two inner loops that go across this board's
columns and the the other board's columns?   If so, you are making things 100x more complicated
than they need to be!  (I'll note that if we printed things out directly rather than returning
a String, that is pretty much what we would have to do though).


Let's instead take the two strings for the boards, and do a bit of string manipulation to
get the result we want.

E.g., lets say
```
myBoard = "  0|1|2|3\n" +
          "A s|s| |  A\n+
          "B  |d|d|d B\n"+
          "  0|1|2|3\n"
```
and their board is
```
myBoard = "  0|1|2|3\n" +
          "A s|d|d|d A\n+
          "B s| | |  B\n"+
          "  0|1|2|3\n"
```
we can make one string:
```
          "  0|1|2|3                 0|1|2|3\n" + 
          "A s|s| |  A             A s|d|d|d A\n"+
          "B  |d|d|d B             B s| | |  B\n "+
          "  0|1|2|3                 0|1|2|3\n"
```
All we have to do is split up the two strings based on their newlines, and concatenate
the pieces together in the right order.

Fortunately for us, Java's strings have [`.split()`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#split-java.lang.String-) method, which will cut the string
up into pieces based on a regular expression.
A regular expression is overkill for what we need, since we just want to split up based on "\n",
but we can use that as our regexp (a litteral character is a regular expression that just matches itself).

So `String [] lines = someString.split("\n");` will give us an array of all the lines (splitting at \n).

With that in mind, lets think about exactly what our method needs to do:

- (1) It needs to add the two "header" lines, shown in the README, e.g.
"Your ocean" and "Player B's ocean".  We'll want these to be parameters,
especially since the second changes based on whose board (and we don't have a way to see
whose board).

- (2) The example in the README shows the second board starting at column 39 (numbering from 0).
    Should it always start at 39?  The README doesn't specify how to handle boards that aren't 10x20,
    but we can make a pretty reasonable inference here.  A board of width W takes up
    2*W+3 characters (1 for the row letters, 1 space, 2 characters per square, and then another
    set of row letters). For W=10, that makes 23 columns (numbered 0 to 22).  That means that
    it would be pretty reasonable to keep constant spacing between the boards, and
    make the second board start at column number 2*W+19.

- (3) Along the same lines, the first header starts at column number 5.  We can keep
    that as always 5.  The second starts at 42, which we can generalize to 2*W+22.
     

Now we are ready to write:
```java
  public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader) {
```
This method should put "this" view's board's "my own board" on the left, and enemyView's "enemy board" on the
right.  The last hint I'll give you is that I found [StringBuilder](https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html) quite useful.
   

With all of the above info, we leave the rest to you to figure out :)

Of course, you are going to test this rigorously, get 100% coverage, and update your documentation
as needed.

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 21](./task21.md) (almost there!)