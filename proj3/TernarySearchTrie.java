import java.util.PriorityQueue;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

/**
* A Ternary Search Tree.
* Code Inspired from Algorithms, 4th Edition
* @author Zhaohong Jin.
*/
public class TernarySearchTrie {
    private int N; //size
    private TSTNode root;

    /**
    * Given some word s of length N, insert should add s to the Trie in O(N) time and space.
    * @param key is the String to be inserted.
    * @param value is the weight associated with the key
    * @throws IllegalArgumentException if the String passed in is null or empty.
    */
    public void insert(String key, double value) {
    	if (key == null || key.equals("")) {
            throw new IllegalArgumentException("String is null or empty");
        }
        root = insert(root, key, value, 0);
    }

    private TSTNode insert(TSTNode n, String key, double value, int d) {
        char c = key.charAt(d);
        if (n == null) {
            n = new TSTNode();
            n.character = c;
        }
        if (c > n.character) {
            n.right = insert(n.right, key, value, d);
        } 
        else if (c < n.character) {
            n.left = insert(n.left, key, value, d);
        }
        else if (d < key.length() - 1) {
        	n.middle = insert(n.middle, key, value, d+1);
        }
        else {
        	n.value = value;
        	n.word = key;
        }
        return n;
    }


    /**
    * find the node corresponding to the key
    * @param key the string to be searched
    * @return double, the weight corresponding to the word.
    */
    public double find(String key) {
        if (key == null) {
            throw new NullPointerException("Can not input null keys");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("Can not input zero-length keys");
        }
        TSTNode n = find(root, key, 0);
        if (n == null) {
            return -1;
        }
        return n.value;
    }

    /**
    * Given some query String s of length N, 
    * find whether or not the query String is in the Trie in O(N) time.
    * if isFullWord is true, then only full word matches should return true.
    * @param s the string to be searched
    * @param isFullWord If isFullWord is false, then partial prefix matches should return true; 
    * @return if such string s exists or not
    */

    public boolean find(String key, boolean isFullWord) {
        if (key == null) {
            throw new NullPointerException("Can not input null keys");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("Can not input zero-length keys");
        }
        TSTNode n = find(root, key, 0);
        if (n == null) {
            return false;
        }
        if (n.value < 0 && isFullWord) {
            return false;
        }
        return true;
    }


    /**
    * find the node corresponding to the key
    * @param n  the root of the trie
    * @param key the string to be searched
    * @param d a counter
    * @return TSTNode, the modified root
    */
    private TSTNode find(TSTNode n, String key, int d) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("Can not input zero-length keys");
        }
        if (n == null) {
            return null;
        }
        char c = key.charAt(d);
        if (c > n.character) {
            return find(n.right, key, d);
        }
        else if (c < n.character) {
            return find(n.left, key, d);
        }
        else if (d < key.length() - 1) {
            return find(n.middle, key, d + 1);
        }
        else {
            return n;
        }
    }


    /**
    * given a node, find the max weight from its subnode.
    * @param node n.
    * @return max wright of the subnode of n.
    */
    private double findMax(TSTNode n) {
        double max = -1;
        if (n.left != null && n.left.max != -1) {
            max = n.left.max;
        }
        if (n.middle != null && n.middle.max != -1) {
            if (max < n.middle.max) {
                max = n.middle.max;
            }
        }
        if (n.right != null && n.right.max != -1) {
            if (max < n.right.max) {
                max = n.right.max;
            }
        }
        return max;
    }

    public void postOrderSetUp() {
        postOrderSetUp(root);
    }

    /**
    * this method set up the maxium weight of the subtries of each node 
    * @param n
    */
    private void postOrderSetUp(TSTNode n) {
        if (n != null) {
            postOrderSetUp(n.left);
            postOrderSetUp(n.middle);
            postOrderSetUp(n.right);
            if (n.left == null && n.middle == null && n.right == null) {
                n.max = n.value;
            } else {
                if (n.value != -1 && n.value > findMax(n)) {
                    n.max = n.value;
                } else {
                    n.max = findMax(n);
                }
            }
        }
    }


    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix The perfix of a word
     * @param k top k matching terms
     * @return Iterable a collection of the iterable topmatches strings 
     */
    public Iterable<String> topMatches(String prefix, int k) {
        TSTNode n = new TSTNode();
        if (prefix.equals("")) {
            n = root;
        } else {
            n = find(root, prefix, 0); //first find the node of the prefix
        }
        if (n == null) {
            ArrayList<String> emptyString = new ArrayList<String>();
            Iterable<String> returnString = emptyString;
            return returnString;
        }
        return topMatchesExtension(n, k, prefix);
    }

    /**
    * return an arraylist of word corresponding to the heavires weights from a node
    * @param n  the node that we start searching
    * @param k top k terms we want to have
    * @return Iterable a collection of the iterable topmatches strings 
    */
    public Iterable<String> topMatchesExtension(TSTNode n, int k, String prefix) {
        KeyValuePQ q = new KeyValuePQ();
        ArrayList<TSTNode> sortedList = new ArrayList<TSTNode>();
        TSTNode head = n;
        q.enqueue(n);
        while (!q.isEmpty()) {
            //weight of the heaviest reachable node in the priority queue is 
            //less than or equal to the weight of the kth heaviest term in the list
            if (sortedList.size() >= k) {
                Collections.sort(sortedList, Collections.reverseOrder()); // sort the list in a descending order
                if (q.peek().max <= sortedList.get(k - 1).value) {
                    return trim(sortedList, k);
                }
            }
            TSTNode tstN = q.dequeue();
            //we hit a word
            if (tstN.word != null) {
                sortedList.add(tstN);
            }
            if (tstN == head && tstN != root || tstN == root && !prefix.equals("")) {
                q.enqueue(n.middle);
            } 
            else {
                q.enqueueSub(tstN); //enqueue the subnode based on their max weight
            }
        }
        return translate(sortedList, k);
    }

    /**
    * this method return the largest k elements of a list
    * @param sortedList list to be trimed
    * @param k how many elements we want
    * @return Iterable
    */
    private Iterable<String> trim(ArrayList<TSTNode> sortedList, int k) {
        ArrayList<String> kList = new ArrayList<String>();
        for (int i = 0; i < k; i++) {
            String word = sortedList.get(i).word;
            kList.add(word);
        }
        Iterable<String> returnKList = kList;
        return returnKList;
    }

    /**
    * this method return the largest k elements of a list
    * @param sortedList list to be trimed
    * @param k
    * @return Iterable
    */
    private Iterable<String> translate(ArrayList<TSTNode> sortedList, int k) {
        Collections.sort(sortedList, Collections.reverseOrder());
        ArrayList<String> lst = new ArrayList<String>();
        int length = sortedList.size();
        if (length > k) length = k;
        for (int i = 0; i < length; i++) {
            String word = sortedList.get(i).word;
            lst.add(word);
        }
        Iterable<String> returnList = lst;
        return returnList;
    }
}
