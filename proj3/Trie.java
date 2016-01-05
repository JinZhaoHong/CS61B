/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * Some code inspired from Algorithm 4th Edition.
 */
import java.util.TreeMap;

/**
* a Trie data structure.
* @author Zhaohong Jin
*/
public class Trie {
    //referring to the ASCII table
    private static final int R = 256;
    private Node root;
    private int N; //number of keys in trie
    private TreeMap<Character, Integer> foreignDictionary;

    /** 
    * represent each node in a trie.
    */
    private class Node {
        private boolean exists;
        private String word = null; //if exists == true, then word should store the word so far
        private Node[] next;

        /**
        * initialize a new Node
        */
        public Node() {
            exists = false;
            next = new Node[R];
        }
        
        /**
        * initialization of the Node from a foreign alphabet
        * @param len of the foreign alphabet
        */
        public Node(int len) {
            exists = false;
            next = new Node[len];
        }

    }
    
    /**
    * initialization of a normal Trie
    */
    public Trie() {

    }
    
    /**
    * initialization of a Trie with a forign dctionary instead
    * of a ASCII
    * @param foreignAlphabet is a alphabet permutation of some forign alphabets
    */
    public Trie(char[] foreignAlphabet) {
        foreignDictionary = new TreeMap<Character, Integer>();
        int len = foreignAlphabet.length;
        for (int i = 0; i < len; i++) {
            foreignDictionary.put(foreignAlphabet[i], i);
        }
    }


    /**
    * Given some query String s of length N, 
    * find whether or not the query String is in the Trie in O(N) time.
    * if isFullWord is true, then only full word matches should return true.
    * @param s the string to be searched
    * @param isFullWord If isFullWord is false, then partial prefix matches should return true; 
    * @return if such string s exists or not
    */
    public boolean find(String s, boolean isFullWord) {
        Node result = find(root, s, 0, isFullWord);
        return result != null;
    }

    /**
    * to be called by boolean find.
    * @param n  the root of the trie
    * @param key the string to be searched
    * @param d a counter
    * @param isFullWord 
    * @return Node, the modified root
    */
    private Node find(Node n, String key, int d, boolean isFullWord) {
        if (n == null) {
            return null;
        }
        if (d == key.length()) {
            if (!n.exists && isFullWord) {
                return null;
            }
            return n;
        }
        char c = key.charAt(d);
        return find(n.next[c], key, d + 1, isFullWord);
    }

    /**
    * Given some word s of length N, insert should add s to the Trie in O(N) time and space.
    * @param s is the String to be inserted.
    * @throws IllegalArgumentException if the String passed in is null or empty.
    */
    public void insert(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("String is null or empty");
        }
        root = insert(root, s, 0);
    }

    /**
    * this method is called by void insert
    * @param n the root
    * @param key the String to be inserted.
    * @param d the counter
    * @return Node, the modified root
    */
    private Node insert(Node n, String key, int d) {
        if (n == null) {
            n = new Node();
        }
        if (d == key.length()) {
            N = N + 1;
            n.exists = true;
            n.word = key;
            return n;
        }
        char c = key.charAt(d);
        n.next[c] = insert(n.next[c], key, d + 1);
        return n;
    }


    /**
    * returns the number of strings in this symbol table
    * @return the number of strings in the symbol table
    */
    public int size() {
        return N;
    }
    
    /**
    * check if the trie is empty
    * @return <tt>true<tt> if the trie is empty
    */ 
    public boolean isEmpty() {
        return size() == 0;
    }



    /**
    * ====================================================
    * The next part of the trie handles foreign alphabets
    * ====================================================
    */

    /**
    * Given some foreign word s of length N, insert should add s to the Trie in O(N) time and space.
    * @param s is the String to be inserted.
    * @throws IllegalArgumentException if the String passed in is null or empty.
    */
    public void insertForeign(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("String is null or empty");
        }
        Node result = insertForeign(root, s, 0);
        if (result == null) {
            return;
        }
        root = result;
    }


    /**
    * this method is called by void insert, using the foreign dictionary
    * @param n the root
    * @param key the String to be inserted.
    * @param d the counter
    * @return Node, the modified root
    */
    private Node insertForeign(Node n, String key, int d) {
        if (n == null) {
            n = new Node(foreignDictionary.size());
        }
        if (d == key.length()) {
            N = N + 1;
            n.exists = true;
            n.word = key; //this is crucial to alphabetSort.
            return n;
        }
        char c = key.charAt(d);
        if (!foreignDictionary.containsKey(c)) {
            return null;
        }
        int position = foreignDictionary.get(c); //foreign alphabet has new orders
        n.next[position] = insertForeign(n.next[position], key, d + 1);
        return n;
    }


    /**
    * Given some query String s of length N, 
    * find whether or not the query String is in the Trie in O(N) time.
    * if isFullWord is true, then only full word matches should return true.
    * @param s the string to be searched
    * @param isFullWord If isFullWord is false, then partial prefix matches should return true; 
    * @return if such string s exists or not
    */
    public boolean findForeign(String s, boolean isFullWord) {
        Node result = findForeign(root, s, 0, isFullWord);
        return result != null;
    }

    /**
    * to be called by boolean find.
    * @param n  the root of the trie
    * @param key the string to be searched
    * @param d a counter
    * @param isFullWord 
    * @return Node, the modified root
    */
    private Node findForeign(Node n, String key, int d, boolean isFullWord) {
        if (n == null) {
            return null;
        }
        if (d == key.length()) {
            if (!n.exists && isFullWord) {
                return null;
            }
            return n;
        }
        char c = key.charAt(d);
        int position = foreignDictionary.get(c); //foreign alphabet has new orders
        return findForeign(n.next[position], key, d + 1, isFullWord);
    }


    /**
    * pre-order traversal of the trie and print out every word in alphabatical order
    */
    public void preOrderSort() {
        preOrderSort(root);

    }

    /**
    * pre-order traversal of the trie and print out every word in alphabatical order
    * @param n ROOT of the Trie
    */
    public void preOrderSort(Node n) {
        if (n != null) {
            if (n.exists) {
                System.out.println(n.word);
            }
            for (Node child : n.next) {
                preOrderSort(child);
            }
        }
    }

}
