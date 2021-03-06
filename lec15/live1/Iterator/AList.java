/** Array based list.
 *  @author Josh Hug
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

import java.util.Iterator;

public class AList extends AbstractXList implements Iterable<Integer>{
    private int[] items;
    private int size; 

    public Iterator<Integer> iterator() {
        return new AListIterator();
    }

    public class ListPrinter {
        public int i = 0;

        public void printNext() {
            System.out.println(items[i]);
            i += 1;
        }
    }

    public class AListIterator implements Iterator<Integer> {
        public int i = 0;

        public Integer next() {
            if (hasNext()) {
                Integer returnItem = items[i];
                i += 1;
                return returnItem;
            }
            throw new NoSuchElementException();
        }

        public boolean hasNext() {
            return (i < size());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Invariants of ALists. 
     * 1. There are size items in the list.
     * 2. The last item in the list is in position: size - 1.
     * 3. The next insertion position is: size.
     * 4. The ith item is in position i.
     */

    /** Resizes ITEMS to C, copying things over. */
    private void resize(int c) {
        int[] newItems = new int[c];
        for (int i = 0; i < items.length; i += 1) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    /** Creates an empty list. */
    public AList() {
        items = new int[100];
        size = 0;
    }

    /** Inserts X into the back of the list. */
    @Override
    public void insertBack(int x) {
        // what position does the 0th insert go into? position 0
        if (size == items.length) {
            resize((int) (size * 1.01));
        }
        items[size] = x;
        size = size + 1; 
    }


    /** Gets the ith item in the list (0 is the front). */
    @Override    
    public int get(int i) {
        return items[i];        
    }

    /** Deletes item from back of the list and
      * returns deleted item. */
    public int deleteBack() {
        int oldBack = getBack(); //i am lazy
        size = size - 1; // THIS IS DELETION.
        return oldBack;
    }

    /** Returns the number of items in the list. */
    @Override    
    public int size() {
        return size;        
    }


} 