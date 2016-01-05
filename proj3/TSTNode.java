/**
* @author Zhaohong Jin
*/
public class TSTNode implements Comparable<TSTNode> {
    public char character;
    public double value = -1; //if this node doesn't contain anything, value = -1;
    public double max = -1; 
    public String word; //if exists == true, then word should store the word so far
    public TSTNode left;
    public TSTNode middle;
    public TSTNode right;

    /**
    * @param other
    * @return int 1,0,-1
    */
    public int compareTo(TSTNode other) {
        if (this.value > other.value) return 1;
        if (this.value < other.value) return -1;
        return 0;
    }
}