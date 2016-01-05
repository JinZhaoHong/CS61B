import java.util.Set; /* java.util.Set needed only for challenge problem. */
import java.util.Iterator;

/** A data structure that uses a linked list to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Supports get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key. 
 *
 *  For simplicity, you may assume that nobody ever inserts a null key or value
 *  into your map.
 */ 
public class ULLMap<Key,Val> implements Map61B<Key, Val>, Iterable<Key> { //FIX ME
    /** Keys and values are stored in a linked list of Entry objects.
      * This variable stores the first pair in this linked list. You may
      * point this at a sentinel node, or use it as a the actual front item
      * of the linked list. 
      */
    private Entry<Key,Val> front;

    @Override
    public Val get(Key key) { //FIX ME
    //FILL ME IN
        Entry<Key, Val> entry = front.get(key);
        return entry.val; //FIX ME
    }

    @Override
    public void put(Key key, Val val) { //FIX ME
    //FILL ME IN
        front = new Entry<Key, Val>(key, val, front);
    }

    @Override
    public boolean containsKey(Key key) { //FIX ME
    //FILL ME IN
        Entry<Key, Val> entry = front.get(key);
        if (entry == null){
            return false;
        }
        return true; //FIX ME
    }

    @Override
    public int size() {
        if (front == null){
            return 0;
        }
        if (front.next == null){
            return 1;
        }
        int count = 1;
        Entry<Key, Val> pointer = front;
        while(!(pointer.next == null)){
            count+=1;
            pointer = pointer.next;
        }
        return count; // FIX ME (you can add extra instance variables if you want)
    }

    @Override
    public void clear() {
    //FILL ME IN
    front = null;
    }

    public Iterator<Key> iterator(){
        return new ULLMapIter();
    }

    //This method should take a map as its argument and return a valid inverse of this map
    // (according to the definition of inverse above). 
    //Note that if mutliple keys in the original map correspond to the same value, 
    //then you are free to choose any of these keys to use as the value in the new map.
    public static <Val, Key> ULLMap invert(ULLMap<Key, Val> map){
        ULLMap<Val, Key> invertMap = new ULLMap();
        Iterator<Key> mapIterator = map.iterator();
        while (mapIterator.hasNext()){
            Key inputKey = mapIterator.next();
            Val inputVal = map.get(inputKey);
            invertMap.put(inputVal, inputKey);
        }
        return invertMap;
    }


    /** Represents one node in the linked list that stores the key-value pairs
     *  in the dictionary. */
    private class Entry<Key,Val> {
    
        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        public Entry(Key k, Val v, Entry n) { //FIX ME
            key = k;
            val = v;
            next = n;
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        public Entry get(Key k) { //FIX ME
            //FILL ME IN (using equals, not ==)
            if (this == null){
                return null;
            }
            if (this.next==null && key.equals(k)){
                return this;
            }
            Entry<Key,Val> pointer = this;
            while (!(pointer.next == null)){
                if (pointer.key.equals(k)){
                    return pointer;
                }
                pointer = pointer.next;
            } //FIX ME
            return null;
        }

        /** Stores the key of the key-value pair of this node in the list. */
        private Key key; //FIX ME
        /** Stores the value of the key-value pair of this node in the list. */
        private Val val; //FIX ME
        /** Stores the next Entry in the linked list. */
        private Entry<Key,Val> next;
    
    }

    /* creating a map iterator*/
    private class ULLMapIter<Key, Val> implements Iterator<Key>{
        private int k = 1;

        public boolean hasNext(){
            return (k <= size()); 
        }

        public Key next(){
            if (hasNext()){
                Key returnKey = (Key) front.key;
                front = front.next;
                return returnKey;
            }
            return null;
        }

        public void remove(){
            throw new UnsupportedOperationException(" ");
        }


    }

    /* Methods below are all challenge problems. Will not be graded in any way. 
     * Autograder will not test these. */
    @Override
    public Val remove(Key key) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Val remove(Key key, Val value) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Key> keySet() { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }



}