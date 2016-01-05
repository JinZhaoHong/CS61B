/**
* AlphabetSort sorts a list of words into alphabetical order, 
* according to a given permutation of some alphabet. 
* It takes input from stdin and prints to stdout.
* @author Zhaohong Jin
*/

import java.util.Scanner;
import java.util.HashSet;

/**
* a class sort words in an alphabetical order according to the given dictionary
* @author Zhaohong Jin.
*/
public class AlphabetSort {

    private static Trie foreignTrie;
    private static char[] alphabet;
    //check whether a letter appear multiple times in the alphabet
    private static HashSet<Character> allChar;
     
    /**
    * main class
    * @param args takes in a directed file
    */
    public static void main(String[] args) {
        allChar = new HashSet<Character>();
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            throw new IllegalArgumentException("No words or alphabet are given");
        }
        //this part read the first line of the standard input and construct the dictionary
        String alphabetString = sc.nextLine();
        if (!sc.hasNextLine()) {
            throw new IllegalArgumentException("No words or alphabet are given");
        }
        int len = alphabetString.length();
        alphabet = new char[len];
        for (int i = 0; i < len; i++) { 
            char c = alphabetString.charAt(i);
            if (allChar.contains(c)) {
                throw new IllegalArgumentException(
                    "A letter appears multiple times in the alphabet.");
            }
            alphabet[i] = c;
            allChar.add(c);
        }
        foreignTrie = new Trie(alphabet);
        //this part insert the rest of the lines into the trie
        boolean illegalChar = false; //test for illegal characters in the word.
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (!allChar.contains(c)) { 
                    illegalChar = true;
                }
            }
            if (!illegalChar) {
                foreignTrie.insertForeign(s); 
            }
            illegalChar = false;
        }
        sc.close();
        foreignTrie.preOrderSort();
    }

}
