/**
* This test files will test all the method required in the project3
* @author Zhaohong Jin
*/

import org.junit.Test;
import static org.junit.Assert.*;

public class Project3Test {

    @Test
    public void testBasicTrie() {
    	Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertEquals(true, t.find("hell", false));
        assertEquals(true, t.find("hello", true));
        assertEquals(true, t.find("good", false));
        assertEquals(false, t.find("bye", false));
        assertEquals(false, t.find("heyy", false));
        assertEquals(false, t.find("hell", true));   
    }

    @Test
    public void testBasicTST() {
    	TernarySearchTrie tst = new TernarySearchTrie();
    	tst.insert("hello", 10);
        tst.insert("hey", 10);
        tst.insert("goodbye", 10);
        assertEquals(true, tst.find("hell", false));
        assertEquals(true, tst.find("hello", true));
        assertEquals(true, tst.find("good", false));
        assertEquals(false, tst.find("bye", false));
        assertEquals(false, tst.find("heyy", false));
        assertEquals(false, tst.find("hell", true));
    }

    /*@Test
    public void testPriorityQueue() {
        KeyValuePQ kvpq = new KeyValuePQ();
        kvpq.enqueue('a', 0.0);
        kvpq.enqueue('b', 1.1);
        kvpq.enqueue('c', 2.2);
        kvpq.enqueue('d', 3.3);
        kvpq.enqueue('e', 2.0);
        kvpq.enqueue('f', 1.5);

        assertEquals('d', kvpq.peek());
        assertEquals(6, kvpq.size());
        assertEquals(false, kvpq.isEmpty());

        assertEquals('d', kvpq.dequeue());
        assertEquals(5, kvpq.size());
        assertEquals(false, kvpq.isEmpty());

        assertEquals('c', kvpq.dequeue());
        assertEquals('e', kvpq.dequeue());
        assertEquals('f', kvpq.dequeue());
        assertEquals('b', kvpq.dequeue());
        assertEquals('a', kvpq.dequeue());
        assertEquals(0, kvpq.size());
        assertEquals(true, kvpq.isEmpty());
    }*/


    public static void main(String[] args) {
    	jh61b.junit.textui.runClasses(Project3Test.class);
    }


}