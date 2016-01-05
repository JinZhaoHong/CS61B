/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 */
import java.util.LinkedList;
import java.util.HashSet;


/**
* autocomplete class
* @author Zhaohong Jin.
*/
public class Autocomplete {
    
    private TernarySearchTrie tst;
    private HashSet<String> allTerms;
    private boolean hasSetUp = false;

    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        tst = new TernarySearchTrie();
        allTerms = new HashSet<String>();
        int len = terms.length;
        if (len != weights.length) {
            throw new IllegalArgumentException(
                "The length of the terms and weights arrays are different.");
        }
        for (int i = 0; i < len; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException("There are negative weights.");
            }
            if (allTerms.contains(terms[i])) {
                throw new IllegalArgumentException("There are duplicate input terms.");
            }
            allTerms.add(terms[i]);
            tst.insert(terms[i], weights[i]);
        }
        if (!hasSetUp) {
            tst.postOrderSetUp();
            hasSetUp = true;
        }
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term The name of the string
     * @return double the weight of the given term
     */
    public double weightOf(String term) {
        if (tst.find(term) == -1) {
            return 0.0;
        }
        return tst.find(term);
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        if (prefix == null) {
            throw new NullPointerException();
        }
        Iterable<String> sorted = tst.topMatches(prefix, 1);
        String term = new String();
        for (String s : sorted) {
            term = s;
        }
        return term;
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix The perfix of a word
     * @param k top k matching terms
     * @return Iterable a collection of the iterable topmatches strings 
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (prefix == null) {
            throw new NullPointerException();
        }
        if (k < 0) {
            throw new IllegalArgumentException(
                "Trying to find the k top matches for non-positive k.");    
        }
        return tst.topMatches(prefix, k);
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        LinkedList<String> results = new LinkedList<String>();  
        /* YOUR CODE HERE; LEAVE BLANK IF NOT PURSUING BONUS */
        return results;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from standard input 
     * and prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }
}
