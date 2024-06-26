## Task 4. Placement class

For this step, you are going to write the Placement class.  A Placement has two fields:
- (1) a final Coordinate where
- (2) a final char orientation

It should have a constructor that takes in both of these pieces of data and initializes
the fields appropritately. It should also have getters for both fields, a toString(),
equals(), and hashCode method.   Lastly, it should have a constructor that takes
a string (such as "A0V") and initializes the fields based on the string.  Note
you should make good use of the fact that Coordinate already has such a constructor
(don't duplicate that code)!  String has a "substring" method, which may be helpful here.

You should be pretty pro at these things by now, so we are going to let you handle
this step on your own.   Be sure to write good tests as you go, and keep your
code coverage at 100%.
Don't forget to document everything as you go.

One tricky thing to make sure you handled correctly:
```java
    Placement p1 = new Placement(c1, 'v');
    Placement p2 = new Placement(c2, 'V');
    assertEquals(p1, p2);
```
This test should pass since we said that we were handling everything
with case insensitivity.  Hopefully you handled case insensitivy by
making everything uppercase (or lower case) in the constructor,
then you know you have uniformity everywhere else.  Observe how
this is one of the great joys and powers of OOP:  since the orientation
is private, once we enforce an invariant on it (e.g., "always capital")
the rest of our code can rely on that!
We also hope you tested for these sorts of cases.

Remember to git commit and push when you finish this step.


You finished step 4.

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 5](./task5.md)