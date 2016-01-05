/** 
 *  @author Josh Hug
 */

public class MaxSList extends SList {
    public String flavor = "MaxSList flavored";
    
    public void printSuperFlavor() {
        super.printFlavor();
    }

    @Override
    public void printFlavor() {
        // the static type of 'this' is MaxSList
        // therefore we use the flavor value associated
        // with MaxSList!
        System.out.println("MaxSList's flavor is: " + flavor);
    }

    int max = 0;
    public MaxSList() {
        super(); /* call my superclass's constructor */
        max = 0;
    }

    public MaxSList(int x) {
        super(x); /* call my superclass's one parameter constructor */
        max = x;
    }

    @Override
    public void insertFront(int x) {
        System.out.println("The MaxSList version was called.");
        if (x > max) {
            max = x;
        }
        super.insertFront(x);
    }

    @Override
    public void insertBack(int x) {
        if (x > max) {
            max = x;
        }
        super.insertBack(x);
    }

    public int max() {
        return max;
    }

    public static void main(String[] args) {
        /*MaxSList msl = new MaxSList(10);
        msl.insertBack(50);
        System.out.println(msl.getFront());
        System.out.println(msl.max());*/
        SList sl = new MaxSList(10);
        sl.insertFront(100);
    }
    /* your code here */
}