## Task 14. No Collision Rule

Now, you should go write `NoCollisionRuleChecker.java`.
This class should check the rule that theShip does not collide with anything else
on theBoard (that all the squares it needs are empty).

You should be able to implement this class without changing any other class or interface
(and it should only be a few lines of code).

You should also be able to write some good test cases for this :)

Once you have tested `NoCollisionRuleChecker` in isolation, add another
@Test method (or more than one if you prefer) to `NoCollisionRuleCheckerTest.java`
(Don't forget, you can use tryAddShip to place some ships on the board, before checking the placement of another)
which *combines* NoCollisionRuleChecker and InBoundsRuleChecker and tests
that they both work together (This test will cover the last bit of PlacementRuleChecker).

Note that the combined test should make you very confident that you can correctly
check if the placement of a Ship on a Board is valid under any circumstances
that might arise.

Once you have 100% coverage and good documentation, you are done with
task 14 and ready to move on to task 15!


***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 15](./task15.md)