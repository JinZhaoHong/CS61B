import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

/**
 * Class that provides JUnit tests for Gitlet, as well as a couple of utility
 * methods.
 * 
 * @author Joseph Moghadam
 * 
 *         Some code adapted from StackOverflow:
 * 
 *         http://stackoverflow.com/questions
 *         /779519/delete-files-recursively-in-java
 * 
 *         http://stackoverflow.com/questions/326390/how-to-create-a-java-string
 *         -from-the-contents-of-a-file
 * 
 *         http://stackoverflow.com/questions/1119385/junit-test-for-system-out-
 *         println
 * 
 */
public class GitletPublicTest {
    private static final String GITLET_DIR = ".gitlet/";
    private static final String TESTING_DIR = "test_files/";

    /* matches either unix/mac or windows line separators */
    private static final String LINE_SEPARATOR = "\r\n|[\r\n]";

    private String printingResult = new String();

    /**
     * Deletes existing gitlet system, resets the folder that stores files used
     * in testing.
     * 
     * This method runs before every @Test method. This is important to enforce
     * that all tests are independent and do not interact with one another.
     */
    @Before
    public void setUp() {
        File f = new File(GITLET_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f = new File(TESTING_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f.mkdirs();
    }

    
    /*@Test
    public void testBasicCheckoutAndBranch() {
        System.out.println(">>>>>>>>>>>>>Testing testBasicCheckoutAndBranch");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        //initiate
        printingResult = gitlet("init");
        System.out.println(printingResult);
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        printingResult = gitlet("add", f1);
        System.out.println(printingResult);
        printingResult = gitlet("add", f2);
        System.out.println(printingResult);
        printingResult = gitlet("add", f3);
        System.out.println(printingResult);
        printingResult = gitlet("add", f4);
        System.out.println(printingResult);

        printingResult = gitlet("commit", "soccer players");
        System.out.println(printingResult);

        writeFile(f3, "I am the FIFA Ballon d'Or of 2014");
        gitlet("add", f3);

        writeFile(f1, "My favorite player is C-Ronaldo");
        gitlet("rm", f1);

        gitlet("commit", "best player of the year");

        assertEquals(getText(f3), "I am the FIFA Ballon d'Or of 2014");
        assertEquals(getText(f1), "My favorite player is C-Ronaldo");


        gitlet("checkout", "2", f1);
        assertEquals(getText(f1), "I am a college soccer player");
        gitlet("checkout", f3);
        assertEquals(getText(f3), "I am the FIFA Ballon d'Or of 2014");
        gitlet("checkout", "2", f3);
        assertEquals(getText(f3), "I am a Portugal soccer player");

        gitlet("add", f1);
        gitlet("add", f3);

        gitlet("commit", "return to the originalState");
        gitlet("checkout", f1);
        assertEquals(getText(f1), "I am a college soccer player");
        gitlet("checkout", f3);
        assertEquals(getText(f3), "I am a Portugal soccer player");
        gitlet("checkout", "3", f3);
        assertEquals(getText(f3), "I am the FIFA Ballon d'Or of 2014");
        printingResult = gitlet("log");
        System.out.println(printingResult);
    }*/

    /**
     * Tests that init creates a .gitlet directory. Does NOT test that init
     * creates an initial commit, which is the other functionality of init.
     */
    @Test
    public void testBasicInitialize() {
        System.out.println(">>>>>>>>testing testBasicInitialize");
        gitlet("init");
        File f = new File(GITLET_DIR);
        assertTrue(f.exists());
    }

    /**
     * Tests that checking out a file name will restore the version of the file
     * from the previous commit. Involves init, add, commit, and checkout.
     */
    @Test
    public void testBasicCheckout() {
        System.out.println(">>>>>>>>testing testBasicCheckout");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");

        gitlet("add", wugFileName);

        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");

        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    /**
     * Tests that log prints out commit messages in the right order. Involves
     * init, add, commit, and log.
     */
    @Test
    public void testBasicLog() {
        System.out.println(">>>>>>>>testing testBasicLog");
        gitlet("init");

        String commitMessage1 = "initial commit";

        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);

        String commitMessage2 = "added wug";
        gitlet("commit", commitMessage2);


        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1 },
                extractCommitMessages(logContent));
    }

    @Test
    public void testBasicOperations() {
        System.out.println(">>>>>>>>>>>>>Testing testBasicOperations");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        //initiate
        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
       
        //commit on the master branch
        String commitMessage1 = "Soccer All Stars";
        gitlet("commit", commitMessage1);
        
        //create a two new branches
        gitlet("branch", "WorldCup");
        gitlet("branch", "Goals of 2015");
        
        //commit on the worldcup branch;
        gitlet("checkout", "WorldCup");
        assertEquals("I am a college soccer player", getText(f1));

        writeFile(f3, "I am the FIFA Ballon d'Or of 2014");
        gitlet("add", f3);
        String commitMessage2 = "CR7 becomes the best player";
        gitlet("commit", commitMessage2);

        assertEquals("I am the FIFA Ballon d'Or of 2014", getText(f3));
        
        //commit on the master branch
        gitlet("checkout", "master");

        assertEquals(f3Text, getText(f3));
        writeFile(f3, "I won the FIFA Ballon d'Or three times");
        writeFile(f2, "I won the FIFA Ballon d'Or four times");
        gitlet("add", f3);
        gitlet("add", f2);
        String commitMessage3 = "Who won more FIFA Ballon d'Or?";
        gitlet("commit", commitMessage3);

        //commit on the goals of 2015 branch
        gitlet("checkout", "Goals of 2015");
        writeFile(f3, "I scored 34 goals");
        writeFile(f2, "I scored 27 goals");
        gitlet("add", f3);
        gitlet("add", f2);
        String commitMessage4 = "Who scored more in 2015?";
        gitlet("commit", commitMessage4);
        
        //wrap everything up;
        gitlet("checkout", "master");
        assertEquals("I won the FIFA Ballon d'Or three times", getText(f3));
        assertEquals("I won the FIFA Ballon d'Or four times", getText(f2));

        gitlet("checkout", "WorldCup");
        assertEquals("I am the FIFA Ballon d'Or of 2014", getText(f3));

        gitlet("checkout", "Goals of 2015");
        assertEquals("I scored 34 goals", getText(f3));
        assertEquals("I scored 27 goals", getText(f2));

        //more for fun
        gitlet("checkout", "1", f3);
        gitlet("checkout", "1", f2);
        assertEquals("I am a Portugal soccer player", getText(f3));
        assertEquals("I am an Agentina soccer player", getText(f2));

        gitlet("add", f3);
        gitlet("add", f2);
        gitlet("commit", "Wrap it up!");

        printingResult = gitlet("global-log");
        System.out.println(printingResult);

        printingResult = gitlet("status");
        System.out.println(printingResult);

        printingResult = gitlet("log");
        System.out.println(printingResult);
    }

    /*@Test
    public void testRemoveReset() {
        System.out.println(">>>>>>>>>>>>>Testing Remove");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";
        
        //commit1
        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
       
        //commit on the master branch
        //commit 2
        String commitMessage1 = "Soccer All Stars";
        gitlet("commit", commitMessage1);
        
        //commit3
        writeFile(f1, "1");
        writeFile(f2, "2");
        writeFile(f3, "3");
        writeFile(f4, "4");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("rm", f1);
        gitlet("commit", "We abandoned f1");
        
        //commit 4
        writeFile(f1, "Berkeley");
        writeFile(f2, "Agentina");
        writeFile(f3, "Portugal");
        writeFile(f4, "Brazil");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("rm", f2);
        gitlet("rm", f3);
        gitlet("rm", f4);
        gitlet("commit", "We abandoned f2, f3, f4");

        //commit 5
        writeFile(f1, "Shanghai");
        writeFile(f2, "Beijing");
        writeFile(f3, "GuangZhou");
        writeFile(f4, "Xian");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);

        gitlet("commit", "We are backon");

        assertEquals("Shanghai", getText(f1));
        assertEquals("Beijing", getText(f2));
        assertEquals("GuangZhou", getText(f3));
        assertEquals("Xian", getText(f4));

        gitlet("reset", "4");
        assertEquals("Berkeley", getText(f1));
        assertEquals("2", getText(f2));
        assertEquals("3", getText(f3));
        assertEquals("4", getText(f4));

        gitlet("reset", "3");
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("2", getText(f2));
        assertEquals("3", getText(f3));
        assertEquals("4", getText(f4));

        writeFile(f1, "Final1");
        writeFile(f2, "Final2");
        writeFile(f3, "Final3");
        writeFile(f4, "Final4");
        
        //sixth commit
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("rm", f1);
        gitlet("rm", f2);
        gitlet("commit", "finally!");


        gitlet("reset", "1");
        gitlet("reset", "6");
        printingResult = gitlet("log");
        System.out.println(printingResult);
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("2", getText(f2));
        assertEquals("Final3", getText(f3));
        assertEquals("Final4", getText(f4));

    }*/

    @Test
    public void testBranching() {
       System.out.println(">>>>>>>>>>>>>Testing Branching");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        //initiate
        printingResult = gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("commit", "master");

        gitlet("branch", "master2");
        gitlet("branch", "master3");
        gitlet("branch", "master4");

        gitlet("checkout", "master2");
        writeFile(f2, "I am the new f2");
        gitlet("add", f2);
        gitlet("commit", "master2");


        gitlet("checkout", "master3");
        writeFile(f3, "I am the new f3");
        gitlet("add", f3);
        gitlet("commit", "master3");

        gitlet("checkout", "master4");
        writeFile(f4, "I am the new f4");
        gitlet("add", f4);
        gitlet("commit", "master4");



        gitlet("checkout", "master");
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("I am an Agentina soccer player", getText(f2));
        assertEquals("I am a Portugal soccer player", getText(f3));
        assertEquals("I am a Brazil soccer player", getText(f4));
        

        gitlet("checkout", "master2");
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("I am the new f2", getText(f2));
        assertEquals("I am a Portugal soccer player", getText(f3));
        assertEquals("I am a Brazil soccer player", getText(f4));


        gitlet("checkout", "master3");
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("I am an Agentina soccer player", getText(f2));
        assertEquals("I am the new f3", getText(f3));
        assertEquals("I am a Brazil soccer player", getText(f4));


        gitlet("checkout", "master4");
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("I am an Agentina soccer player", getText(f2));
        assertEquals("I am a Portugal soccer player", getText(f3));
        assertEquals("I am the new f4", getText(f4));

        printingResult = gitlet("status");
        System.out.println(printingResult);
        printingResult = gitlet("log");
        System.out.println(printingResult);


    }
    
    @Test
    public void simpleCommit() {
        System.out.println(">>>>>>>>>>>>>Testing Branching");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";

        gitlet("init");
        
        createFile(f1, f1Text);

        gitlet("add", f1);

        gitlet("commit", f1);
        
        writeFile(f1, "OMGGGGGG");
        gitlet("add", f1);
        gitlet("commit", f1);

        assertEquals("OMGGGGGG", getText(f1));
        gitlet("checkout", "1", f1);
        assertEquals("I am a college soccer player", getText(f1));

        printingResult = gitlet("log");
        System.out.println(printingResult);


    }

    @Test
    public void testRest() {
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";

        //initiate
        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("commit", "master");

        writeFile(f1, "My favorite soccer player of all time is CR7");
        writeFile(f2, "I hate CR7");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("commit", "master2");

        writeFile(f1, "I love Computer Science");
        writeFile(f2, "I play for FCB");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("commit", "master3");

        assertEquals("I love Computer Science", getText(f1));
        assertEquals("I play for FCB", getText(f2));

        gitlet("reset", "1");
        assertEquals("I am a college soccer player", getText(f1));
        assertEquals("I am an Agentina soccer player", getText(f2));

        gitlet("reset", "2");
        assertEquals("My favorite soccer player of all time is CR7", getText(f1));
        assertEquals("I hate CR7", getText(f2));

    }

    @Test 
    public void testMerge() {
        System.out.println(">>>>>>>>>>>>>Merge");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);

        gitlet("commit", "soccer players");
        gitlet("branch", "WorldCup");
        gitlet("branch", "UEFA");

        //now on the worldCup Branch
        gitlet("checkout", "WorldCup");
        writeFile(f1, "I love to watch the WorldCup");
        writeFile(f2, "I play for Agentina in the Worldcup");

        gitlet("add", f1);
        gitlet("add", f2);

        gitlet("commit", "path 1 first commit");

        //now on the UEFA branch
        gitlet("checkout", "UEFA");
        writeFile(f3, "I play for Portugal in the UEFA");
        writeFile(f4, "I play for Brazil in the UEFA");
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("commit", "path 2 first commit");

        
        printingResult = gitlet("merge", "WorldCup");
        System.out.println(printingResult);
        printingResult = gitlet("log");
        System.out.println(printingResult);

        assertEquals("I love to watch the WorldCup", getText(f1));
        assertEquals("I play for Agentina in the Worldcup", getText(f2));
        assertEquals("I play for Portugal in the UEFA", getText(f3));
        assertEquals("I play for Brazil in the UEFA", getText(f4));

        //if you don't add and commit them, merged files will not be keeped

        printingResult = gitlet("log");
        System.out.println(printingResult);

    }

    @Test
    public void testMerge2() {
        System.out.println(">>>>>>>>>>>>>Merge2");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);

        gitlet("commit", "soccer players");
        gitlet("branch", "WorldCup");
        gitlet("branch", "UEFA");

        //now on the worldCup Branch
        gitlet("checkout", "WorldCup");
        writeFile(f1, "I love to watch the WorldCup");
        writeFile(f2, "I play for Agentina in the Worldcup");
        writeFile(f3, "I play for Portugal in the UEFA");
        writeFile(f4, "I play for Brazil in the UEFA");

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);

        gitlet("commit", "path 1 first commit");

        //now on the UEFA branch
        gitlet("checkout", "UEFA");
        writeFile(f1, "conflict!");
        writeFile(f2, "conflict!!");
        writeFile(f3, "conflict!!!");
        writeFile(f4, "conflict!!!!");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("commit", "path 2 first commit");

        
        printingResult = gitlet("merge", "WorldCup");
        System.out.println(printingResult);
        printingResult = gitlet("log");
        System.out.println(printingResult);

        assertEquals("conflict!", getText(f1));
        assertEquals("conflict!!", getText(f2));
        assertEquals("conflict!!!", getText(f3));
        assertEquals("conflict!!!!", getText(f4));

        //if you don't add and commit them, merged files will not be keeped

        printingResult = gitlet("log");
        System.out.println(printingResult);
    }

    @Test
    public void testRebase() {
        System.out.println(">>>>>>>>>>>>>Rebase");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);

        gitlet("commit", "soccer players");
        gitlet("branch", "AsianPlayers");

        writeFile(f1, "I am RoyJin");
        writeFile(f2, "I am Leon Messi");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("commit", "path1");

        gitlet("checkout", "AsianPlayers");
        writeFile(f3, "I like GaoLin");
        writeFile(f4, "I like WuLei");
        gitlet("add", f3);
        gitlet("add", f4);
        gitlet("commit", "path2");

        gitlet("checkout", "master");
        printingResult = gitlet("rebase", "AsianPlayers");
        System.out.println(">>>>>>>>>> Rebase <<<<<<<<<");
        System.out.println(printingResult);

        printingResult = gitlet("log");
        System.out.println(printingResult);

        assertEquals("I am RoyJin", getText(f1));
        assertEquals("I am Leon Messi", getText(f2));
        assertEquals("I like GaoLin", getText(f3));
        assertEquals("I like WuLei", getText(f4));

        printingResult = gitlet("log");
        System.out.println(printingResult);

    }

    @Test
    public void basicTestForStatus() {
        System.out.println(">>>>>>>>>>>>>Rebase");
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";
        String f3 = TESTING_DIR + "C-Ronaldo.txt";
        String f3Text = "I am a Portugal soccer player";
        String f4 = TESTING_DIR + "Neymar.txt";
        String f4Text = "I am a Brazil soccer player";

        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);

        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("add", f3);
        gitlet("add", f4);

        gitlet("rm", f1);
        gitlet("rm", f4);

        printingResult = gitlet("status");
        System.out.println(printingResult);
    }

    @Test
    public void basicOperationOfFilesFromDifferentSources() {
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player"; 
        File worldCup = new File("WorldCup/");
        worldCup.mkdirs();
        String f3 = "WorldCup/CR7.docx";
        String f3Text = "I am the best player in the World";
        String f4 = "GaoLin.docx";
        String f4Text = "I am a Chinese player";

        gitlet("init");
        
        createFile(f1, f1Text);
        createFile(f2, f2Text);
        createFile(f3, f3Text);
        createFile(f4, f4Text);
        printingResult = gitlet("add", f1);
        System.out.println(printingResult);
        gitlet("add", f2);
        gitlet("add", f3);
        printingResult = gitlet("add", f4);
        System.out.println(printingResult);
        printingResult = gitlet("commit", "RaLaLa");
        System.out.println(printingResult);


    }

    //current branch is the history of the given branch
    @Test
    public void rebaseEdgeCase() {
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        createFile(f1, f1Text);
        gitlet("init");
        gitlet("add", f1);
        gitlet("commit", "first");

        gitlet("branch", "branch1");
        gitlet("checkout", "branch1");
        writeFile(f1, "Ralala");
        gitlet("add", f1);
        gitlet("commit", "second");

        gitlet("checkout", "master");
        assertEquals("I am a college soccer player", getText(f1));
        gitlet("rebase", "branch1");

        assertEquals("Ralala", getText(f1));
    }


    @Test
    public void mergeEdgeCase() {
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = "RoyJin.txt";
        String f2Text = "I am a college soccer player";

        createFile(f1, f1Text);
        createFile(f2, f2Text);

        gitlet("init");

        gitlet("add", f1);
        gitlet("commit", "TEST_DIR_RoyJin.txt");

        gitlet("branch", "branch2");
        gitlet("checkout", "branch2");
        gitlet("add", f2);
        gitlet("commit", "RoyJin.txt");
        gitlet("checkout", "master");
        gitlet("add", f2);
        gitlet("commit", "RoyJin.txt");
        printingResult = gitlet("merge", "branch2");
        System.out.println(printingResult);

    }

    //credit of this code: Trevor Nesbit on Piazza
    @Test
    public void testRebaseCornerCases() {
        String rugFileName = TESTING_DIR + "rug.txt";
        String bugFileName = TESTING_DIR + "bug.txt";
        String rugText1 = "rug1";
        String bugText = "bug";
        String rugText2 = "rug2";
        String rugText3 = "rug3";
        createFile(rugFileName, rugText1);
        gitlet("init");
        gitlet("branch", "ignore");
        gitlet("add", rugFileName);
        gitlet("commit", "c1"); // Master has rug1
        gitlet("branch", "trevor"); // Branch off and checkout Trevor
        gitlet("checkout", "trevor");
        writeFile(rugFileName, rugText2);
        createFile(bugFileName, bugText);
        gitlet("add", rugFileName);
        gitlet("add", bugFileName);
        gitlet("commit", "c2"); // Trevor has rug2

        // 1
        printingResult = gitlet("rebase", "master"); //should print already up to date.
        System.out.println(printingResult);
        assertEquals(rugText2, getText(rugFileName));
        gitlet("checkout", "master");
        assertEquals(rugText1, getText(rugFileName));
        gitlet("checkout", "trevor");
        assertEquals(rugText2, getText(rugFileName));

        // 2
        gitlet("checkout", "master");
        gitlet("rebase", "trevor"); // Master should now point to same commit as Trevor
        assertEquals(rugText2, getText(rugFileName)); // THIS IS WHERE THE FAILURE OCCURS
        writeFile(rugFileName, rugText3);
        gitlet("add", rugFileName);
        gitlet("commit", "c3");
        printingResult = gitlet("checkout", "ignore");
        System.out.println(printingResult);
        writeFile(bugFileName, "should be overwritten by checkout master");
        printingResult = gitlet("checkout", "master");
        System.out.println(printingResult);
        assertEquals(bugText, getText(bugFileName));
        assertEquals(rugText3, getText(rugFileName));
        gitlet("checkout", "trevor");
        assertEquals(rugText2, getText(rugFileName));
    }

    @Test
    public void additionalTestRemove() {
        String f1 = TESTING_DIR + "RoyJin.txt";
        String f1Text = "I am a college soccer player";
        String f2 = TESTING_DIR + "LeonMessi.txt";
        String f2Text = "I am an Agentina soccer player";

        createFile(f1, f1Text);
        createFile(f2, f2Text);

        gitlet("init");
        gitlet("add", f1);
        gitlet("add", f2);
        gitlet("commit", "first commit");

        writeFile(f1, "R1");
        writeFile(f2, "R2");
        gitlet("add", f1);
        printingResult = gitlet("rm", f2);
        System.out.println(printingResult);
        printingResult = gitlet("commit", "second commit");
        System.out.println(printingResult);
        assertEquals("R1", getText(f1));
        assertEquals("R2", getText(f2));
        gitlet("checkout", f1);
        gitlet("checkout", f2);
        assertEquals("R1", getText(f1));
        assertEquals("R2", getText(f2));
        printingResult = gitlet("checkout", "2", f2);
        System.out.println(printingResult);
        assertEquals("R2", getText(f2));

        writeFile(f2, "R22");
        writeFile(f1, "R11");
        gitlet("checkout", f1);
        gitlet("checkout", f2);
        assertEquals("R1", getText(f1));
        assertEquals("R22", getText(f2));

        gitlet("add", f2);
        gitlet("commit", "f2 backon");
        writeFile(f2, "R2222");
        gitlet("checkout", "0", f2);
        assertEquals("R2222", getText(f2));
        gitlet("checkout", "2", f2);
        assertEquals("R2222", getText(f2));
        gitlet("checkout", "3", f2);
        assertEquals("R22", getText(f2));

    }


















    /**
     * Convenience method for calling Gitlet's main. Anything that is printed
     * out during this call to main will NOT actually be printed out, but will
     * instead be returned as a string from this method.
     * 
     * Prepares a 'yes' answer on System.in so as to automatically pass through
     * dangerous commands.
     * 
     * The '...' syntax allows you to pass in an arbitrary number of String
     * arguments, which are packaged into a String[].
     */
    private static String gitlet(String... args) {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream printingResults = new ByteArrayOutputStream();
        try {
            /*
             * Below we change System.out, so that when you call
             * System.out.println(), it won't print to the screen, but will
             * instead be added to the printingResults object.
             */
            System.setOut(new PrintStream(printingResults));

            /*
             * Prepares the answer "yes" on System.In, to pretend as if a user
             * will type "yes". You won't be able to take user input during this
             * time.
             */
            String answer = "yes";
            InputStream is = new ByteArrayInputStream(answer.getBytes());
            System.setIn(is);

            /* Calls the main method using the input arguments. */
            Gitlet.main(args);

        } finally {
            /*
             * Restores System.out and System.in (So you can print normally and
             * take user input normally again).
             */
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        return printingResults.toString();
    }

    /**
     * Returns the text from a standard text file (won't work with special
     * characters).
     */
    private static String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Creates a new file with the given fileName and gives it the text
     * fileText.
     */
    private static void createFile(String fileName, String fileText) {
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile(fileName, fileText);
    }

    /**
     * Replaces all text in the existing file with the given text.
     */
    private static void writeFile(String fileName, String fileText) {
        FileWriter fw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, false);
            fw.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the file and all files inside it, if it is a directory.
     */
    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }

    /**
     * Returns an array of commit messages associated with what log has printed
     * out.
     */
    private static String[] extractCommitMessages(String logOutput) {
        String[] logChunks = logOutput.split("====");
        int numMessages = logChunks.length - 1;
        String[] messages = new String[numMessages];
        for (int i = 0; i < numMessages; i++) {
            System.out.println(logChunks[i + 1]);
            String[] logLines = logChunks[i + 1].split(LINE_SEPARATOR);
            messages[i] = logLines[3];
        }
        return messages;
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(GitletPublicTest.class);
    }
}
