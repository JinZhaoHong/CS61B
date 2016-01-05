//Author: Zhaohong Jin
import java.util.HashSet;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {
    //adapted from the source code of HashMap;
    private float DEFAULT_LOAD_FACTOR = 0.75f;
    private MyMap<K,V>[] nodes;
    private int DEFAULT_CAPACITY = 11;
    private int N;
    private int threshold;
    private Set<K> cache = new HashSet<K>();


    public MyHashMap() {
        nodes = (MyMap<K,V>[]) new MyMap[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        nodes = (MyMap<K,V>[]) new MyMap[initialSize];
        this.DEFAULT_CAPACITY = initialSize;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize, float loadFactor) {
        if (initialSize < 0)
            throw new IllegalArgumentException("Illegal Capacity: "
                                               + initialSize);
        if (!(loadFactor > 0)) // check for NaN too
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        nodes = (MyMap<K,V>[]) new MyMap[initialSize];
        this.DEFAULT_LOAD_FACTOR = loadFactor;
        this.DEFAULT_CAPACITY = initialSize;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private int hash(Object o) {
        return Math.abs(o.hashCode() % DEFAULT_CAPACITY);
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        nodes = null;
        N = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. 
     * Should run on average constant time when called on a HashMap. 
     */
    public boolean containsKey(K key) {
        int k = hash(key);
        MyMap<K,V> mm = nodes[k];
        while (mm != null) {
            if (key.equals(mm.key)) {
                return true;
            }
            mm = mm.next;
        }
        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. Should run on average constant time
     * when called on a HashMap. 
     */
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int k = hash(key);
        MyMap<K,V> mm = nodes[k];
        while (mm != null) {
            if (key.equals(mm.key)) {
                return mm.val;
            }
            mm = mm.next;
        }
        return null;
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return N;
    }

    /* Associates the specified value with the specified key in this map. 
     * Should run on average constant time when called on a HashMap. */
    public void put(K key, V value) {

        cache.add(key);
        int k = hash(key);
        MyMap<K,V> mm = nodes[k];
        while (mm != null) {
            if (key.equals(mm.key)) {
                mm.val = value;
                return;
            }
            mm = mm.next;
        }
        
        N = N+1;
        if (N > threshold) {
            hashAgain();
            k = hash(key);
        }
        MyMap<K,V> newmm = new MyMap<K,V>(key, value);
        newmm.next = nodes[k];
        nodes[k] = newmm;
    }

    // inspired by source code of the HashMap;
    private void hashAgain() {
        MyMap<K,V>[] copy = nodes;
        DEFAULT_CAPACITY = DEFAULT_CAPACITY * 2 + 1;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        
        nodes = (MyMap<K,V>[]) new MyMap[DEFAULT_CAPACITY];
        
        for (int i = 0; i < copy.length; i++) {
            MyMap<K,V> inner = copy[i];
            while (inner != null) {
                int index = hash(inner.key);
                MyMap<K,V> temp = nodes[index];
                MyMap<K,V> next = inner.next;
                inner.next = nodes[index];
                nodes[index] = inner;
                inner = next;
            }
        }
    }

    /* Removes the mapping for the specified key from this map if present. 
     * Should run on average constant time when called on a HashMap. */
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int k = hash(key);
        MyMap<K,V> first = nodes[k];
        MyMap<K,V> second = first.next;
        if (first.key.equals(key)) {
            nodes[k] = second;
            cache.remove(key);
            return first.val;
        }
        while (second != null) {
            if (second.key.equals(key)) {
                cache.remove(key);
                first.next = second.next;
                return second.val;
            }
            first = first.next;
            second = second.next;
        }
        return null;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Should run on average constant time when called on 
     * a HashMap. */
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        int k = hash(key);
        MyMap<K,V> first = nodes[k];
        MyMap<K,V> second = first.next;
        if (first.key.equals(key) && first.val.equals(value)) {
            nodes[k] = second;
            cache.remove(key);
            return first.val;
        }
        while (second != null) {
            if (second.key.equals(key) && second.val.equals(value)) {
                first.next = second.next;
                cache.remove(key);
                return second.val;
            }
            first = first.next;
            second = second.next;
        }
        return null;

    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return cache;

    }

}
