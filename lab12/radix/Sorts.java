/* Radix.java */

package radix;

/**
 * Sorts is a class that contains an implementation of radix sort.
 * @author
 */
public class Sorts {
    private static int R = 16;
    private static int W = 8;

    /**
     *  Sorts an array of int keys according to the values of <b>one</b>
     *  of the base-16 digits of each key. Returns a <b>NEW</b> array and
     *  does not modify the input array.
     *  
     *  @param key is an array of ints.  Assume no key is negative.
     *  @param whichDigit is a number in 0...7 specifying which base-16 digit
     *    is the sort key. 0 indicates the least significant digit which
     *    7 indicates the most significant digit
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys sorted according to the chosen digit.
     **/
    public static int[] countingSort(int[] keys, int whichDigit) {
        //YOUR CODE HERE
        int N = keys.length;
        int[] sortedArray = new int[N];
        int[] count = new int[R+1];
        // Compute frequency counts 
        for (int i = 0; i < N; i++) {
            int whichDigitCopy = whichDigit;
            int index = keys[i];
            while (whichDigitCopy > 0) {
                index = index >>> 4;
                whichDigitCopy -= 1;
            }
            index = index % 10;
            count[index + 1] = count[index + 1] + 1;
        }
        //Transform counts to indices
        for (int r = 0; r < R; r++) {
            count[r+1] += count[r];
        }
        // Distribute the records.
        for (int i = 0; i < N; i++) {
            int whichDigitCopy = whichDigit;
            int index = keys[i];
            while (whichDigitCopy > 0) {
                index = index >>> 4;
                whichDigitCopy -= 1;
            }
            index = index % 10;
            sortedArray[count[index]++] = keys[i];
        }
        return sortedArray;
    }

    /**
     *  radixSort() sorts an array of int keys (using all 32 bits
     *  of each key to determine the ordering). Returns a <b>NEW</b> array
     *  and does not modify the input array
     *  @param key is an array of ints.  Assume no key is negative.
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys in sorted order.
     **/
    public static int[] radixSort(int[] keys) {
        //YOUR CODE HERE
        int N = keys.length;
        int [] sortedKeys = new int[N];
        for (int d = W - 1; d >= 0; d--) {
            sortedKeys = countingSort(keys, d);
        }
        return sortedKeys;        
    }
}
