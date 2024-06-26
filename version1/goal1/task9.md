## Task 9. App Skeleton

You are almost done with your first big picture goal:  making something you can actually run.
So far, we have been writing classes and test cases, but there is no actual "battleship program".

It is now time to remedy that!  We are going to make is so you can "play" (well at least run)
the battleship game.  However, this game is going to be very minimal.  It will do the following:

 - Create a `BattleshipBoard` and a `BoardTextView`
 - Ask the user to enter a `Placement`
 - Read the string for the Placement, and create a new Placement.
    - If the String is not a valid Placement, we are going to just let the exception
      propogate for now + crash the program _--we'll fix that in Task 17._
 - Put a `BasicShip` on the board in that place.
 - Print the board
 
and that's all!

>[!NOTE]
> - Notice three important things:
>   - (1) This is very far from the actual functionality of the game (we sill have ~12 tasks to go!)
>   - (2) This is the skeleton of the functionality of the game: it largely resembles what we need  to do.  The rest is just adding features
>   - (3) We already have already written (and tested!) almost everything we need to do this task.
    
Remember that auto-generated App.java class we have been ignoring since the start?
It is time to go back to that, and make it useful.   This is the default entry point
for the program when you run it.  If you wanted to change it, you could do so in build.gradle
by changing
```java
application {
    // Define the main class for the application.
    mainClassName = 'edu.duke.adh39.battleship.App'
}
```
but we can just go with this class. Lets get rid  of the "getGreeting" and
the contents of main (leave the declaration of main though).  Both of these were
put in by gradle init as placeholders.

- Instead, let us declare three fields in this class:
    - (1) final Board<Character> theBoard
    - (2) final BoardTextView view
    - (3) final BufferedReader inputReader
    - (4) final PrintStream out 
The third and fourth of these are in java.io, so needs to be imported.  Hit "C-c TAB" to import them

We might be tempted to write a constructor like this:
```java
 public App()  {
    this.theBoard = new Board(10, 20);
    this.view = new BoardTextView(theBoard);
    this.inputReader = new BufferedReader(new InputStreamReader(System.in));
    this.out = System.out;
  }
```
However, hardcoding the creation of those objects makes our code less flexible
(what if we want to read input from some other source?  or want a board that isn't 10x20?)
These may seem like hypotheticals, but this flexibility also makes testing much easier!
In fact, the whole reason we have the field out here is so that we can easily test
without writing to System.out.
Instead, lets make the constructor
```java
  public App(Board<Character> theBoard, Reader inputSource, PrintStream out) {
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = new BufferedReader(inputSource);
    this.out = System.out;
  }
```
Now we can write
```java
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    return new Placement(s);
  }
```
Note that wehn you put in "Reader" and go to import it with C-c TAB, you will get
"select class to import:"  since tehre are many options. Use the down arrow until
you get to "java.io.Reader" and hit enter.

Note that we are not doing error checking YET (later, in task 17).  Also note
that we don't print to System.out, we print to out.  Why?  We might want to print
somewhere else, especially for testing.   When we want System.out, we'll pass it into
the constructor.

Lets go test this out now.  Head over to AppTest.java.  There is a test method
(appHasAGreeting) left over from the autogeneration.  Get rid of that and write
a method called
```java
    @Test
    void test_read_placement() throws IOException 
```
To do this, we are going to make use
of a few things.  The first is a [StringReader](https://docs.oracle.com/javase/8/docs/api/java/io/StringReader.html)

This class is a lot like a StringStream in C++, but only for reading.  Basically,
we can construct it with a String, and then read from it like an input stream.
E.g.,   `StringReader sr = new StringReader("B2V\nC8H\na4v\n");`
That will be a string reader from which we can read three placements.

We will also want a [ByteArrayOutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/ByteArrayOutputStream.html) to collect the output. We can wrap it in a `PrintStream`:
```java
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bytes, true);
```
Now, `ps` is a PrintStream (looks like System.out) which writes
its data into bytes instead of to the screen. We pass "true"
for the autoflush parameter to ensure that data becomes availabile
immediately in bytes. Then
you can create a Board and an App
```java
    Board<Character> b = new BattleShipBoard<Character>(10, 20);
    App app = new App(b, sr, ps);
```
notice how App now reads from sr (our StringReader) and writes
to ps (our PrintStream that writes to our ByteArrayOutputStream),
so now actual interaction with stdin/stdout takes place.
We can then make a String for our prompt and the expected
Placements that we should get back:
```java
    String prompt = "Please enter a location for a ship:";
    Placement[] expected = new Placement[3];
    expected[0] = new Placement(new Coordinate(1, 2), 'V');
    expected[1] = new Placement(new Coordinate(2, 8), 'H');
    expected[2] = new Placement(new Coordinate(0, 4), 'V');
```
Now, we expect to be able to get those three Placements form
app.readPlacement.  We also want to check that each call
properly printed the prompt, which we can check by looking
at what is in bytes.  After we do that,w e want to reset bytes
so it is empty before the next test case:
```java
      for (int i = 0; i < expected.length; i++) {
        Placement p = app.readPlacement(prompt);
        assertEquals(p, expected[i]); //did we get the right Placement back
        assertEquals(prompt + "\n", bytes.toString()); //should have printed prompt and newline
        bytes.reset(); //clear out bytes for next time around
      }
```
Once you have put that test together, run it and make sure you have
covered all your code (except the placeholder BasicShip).

Next, you should write:
```java
  public void doOnePlacement() throws IOException
```
- Which should
  - read a Placement (prompt: "Where would you like to put your ship?")
  - Create a basic ship based on the location in that Placement
    (orientation doesn't matter yet)
  - Add that ship to the board
  - Print out the board (to out, not to System.out)

Each of the above steps should be one single line of code (since you have
already written a method to do it!).  Once you have written this method,
write a test for it in AppTest.java (do you see any opportunities to abstract
out, rather than repeat code that is shared with the other test?)

Now, we are finally ready to fill in main! First, change main's declaration to
```java
  public static void main(String[] args) throws IOException
```
if we have IO errors reading from stdin, we don't really have a good way to handle them here.
(Ask the user what to do? We can't read from them anyways...)

Now, make main create a 10x20 board, create an App with that board, and call
doOnePlacement on that app.  For the other arguments to App's constructor,
we'll pass new InputStreamReader(System.in) for the input, and pass System.out
for the output.  We wrap System.in in an InputStreamReader since System.in
is an InputStream, and we want a Reader (*). When you go to import
(C-c TAB), you will get prompted for what to import, as before, use the down
arrow to find java.io.InputStreamReader.

> [!NOTE]
> (*) Readers provide characters and Streams provide bytes.  Generally when working with text, you want a Reader.  You can always wrap an InputStream in an InputStreamReader to do the conversion.

Now, lets try to run the whole program!

We might just try to go in our terminal and do
```bash
./gradlew run
```
Oh no! A `NullPointerException` :( What happened?

It turns out that by default, gradle doesn't hook up standard input to the Java
program, and just gives it an empty stdin.  So when we tried to readLine (and
we aren't doing error handling yet), we got null and tried to use it.

Let's make gradle hook up stdin for us.  Go to `build.gradle` and at the end
(outside of any other blocks) put
```bash
run{
    standardInput = System.in
}
```
Be sure to save, and try ./gradlew run again.  Now it works, but we have
the gradle progress bar showing up in the middle of our program's execution.
That will get annoying....

Lets instead have gradle create a distributable version of our program: `./gradlew installDist`
This will make `build/install/battleship` which has bin (containing a Unix script to run the program, and a Windows .bat file) and lib (containing a JAR of our classes and all the libraries we depend on).

Now we can run
```bash
./build/install/battleship/bin/battleship
```
and it will run our program outside of gradle (just all by itself).

***
**Horray! We have a program that places one ship on the board!**
***

We have one last detail to finish up before we declare victory on
Goal 1.   We need a test case for main.  You might be thinking
"but I just tested main by hand, when I ran that program!"
But that is a *manual* test case.  We need something that can
be automated.  Something that we can run with no human oversight
on every change we make (or later, something our CI/CD tools
can run for us).

One thing that may seem tricky is that main is going to read
from System.in and write to System.out---we can't dependency
inject main :(

However, Java does support System.setIn and System.setOut
to change System.in and System.out.  We should use them
*sparingly*... but this is one such case.

Furthermore, instead of writing big long Strings in our
file (as this testcase will evolve into something longer
as our game develops), let us create some files in `src/test/resources`
to have our input and output for this testcase.  Having just played the "game"
and assured ourselves that the output was correct, lets do it again, but record
our input and output:
```bash
tee src/test/resources/input.txt | ./build/install/battleship/bin/battleship | tee src/test/resources/output.txt
```
Type one placement (e.g., A4V) then hit ctrl-D (for end of input).

You may be thinking "What unix black magic is this?"  but it is really just a three command pipeline
```bash
tee | your program | tee.
```
What does tee do?   It read standard input and writes two copies of it: one to standard
output and one to the file named as its argv[1].  That is, if you do `echo "Hello" | tee hello.txt`
you will see `Hello` printed on stdout, and also written to hello.txt

So in the above pipeline, the first tee records our input
to `src/test/resources/input.txt`, and also pipes it to our battleship
program (so it is still the input of the program).
The second tee records the program's output in `src/test/resources/output.txt`,
and also displays it to stdout so we can see the program's output.

Take a moment to `cat src/test/resources/input.txt` and `cat src/test/resources/output.txt`.

Note that as our game becomes more sophisticated, and main does more, you can
use this technique to update these files.  As you do so, check carefully that you
are getting the right behavior as you record the test case!

Now lets write
```java
  @Test
  void test_main() throws IOException 
```
- which will test our main.  We need just a few new concepts for this:
    - (1) getting the input files in a path independent way: we use
     getResourceAsStream for this.
    - (2) Changing around System.in and System.out
    - (3) Reading everything from a file.

Lets start out with the very familiar:
```java
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
```
(or maybe you abstracted this out and can just call that method?)

Next, we want to get an InputStream for our input.txt file:
```java
    InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");
    assertNotNull(input);
```
This seems complicated, but really just asks the current class to give us its ClassLoader
(the part of the Java runtime that reads classes from their files and sets them up in the JVM).
Then we ask the ClassLaoder to find us a resource named "input.txt" and give us back
an InputStream for it.  Then we check that it isn't null (so we can fail fast if
something went wrong).
Then we'll do the same thing for the expected output:
```java
    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");
    assertNotNull(expectedStream);
```
Next, we'll remember the current System.in and System.out:

```java
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
```
Then we'll change to our new input (from "input.txt") and output (our PrintStream that writes
into bytes), and run our App.main.  We'll do this inside a try...finally to ensure
we restore System.in and System.out:
```java
    try {
      System.setIn(input);
      System.setOut(out);
      App.main(new String[0]);
    }
    finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
```
Notice that we pass new String[0] to main---we don't need any arguments, so we'll just pass
a 0 element String[] (which is legal in Java).

Last we just need to read all the data from our expectedStream (output.txt):
`   String expected = new String(expectedStream.readAllBytes());`
Then we get the String out of bytes:
    `String actual = bytes.toString();`
And finally compare them:
    `assertEquals(expected, actual);`

Once you have written that testcase, you are ready to run all your test again.
You should have 100% coverage, except for the placeholder BasicShip.java.

One last technical detail: our test for main modifies the System
streams.  This means that if we were to ever have JUnit run our test
in parallel, we could get really messed up results if the test for main
is parallelized with any other test that uses System.out---even if
just for debugging output!   We can change our test declaration to
```java
@Test
@ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
void test_main() throws IOException {
  //all the code
}
```
Note that when you C-c TAB, there are a lot of choices of classes named Resources.
You want org.junit.jupiter.api.parallel.Resources

Then for any other test that uses a System stream, we could write
an appropriate @ResourceLock to ensure proper serialization of the tests.
We won't be running our tests in parallel, but if you write Java professionally,
you almost certainly will.

Note that as you work through the later stages of this, you can update
input.txt and output.txt to reflect the inputs and expected outputs
for main.  You can use the "tee" pipeline above to do this easily.

I'd also recommend taking a moment to think about how you can adapt
this one test case for main to many testcases (e.g. "input1.txt"
with "output1.txt", "input2.txt" with "output2.txt").

Be sure all your code is well documented, formatted, etc.

Then git commit and push!

Congratulations, you have built the minimal end-to-end system to get
started on Battleship... and you have done it with good testing
and documentation :)


You finished step 9 and with **Goal 1**. Well Done!

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!CAUTION]
> - **Generate a Release**
>   - Go to your GitHub Repository Page, and generate a new release. 
>       - Use `Extra Credit` as Tag. 
>       - Use `Goal 1` as Release name.

>[!NOTE]
> - You can now proceed to [Goal 2](../goal2/task10.md)