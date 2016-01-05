//Author: Zhaohong Jin 
import java.util.Set;


public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

	private Node root;
    
    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int N;             // number of nodes in subtree

        public Node(K key, V val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }

        public V get (Node n, K key) {
        	if (n == null) {
        		return null;
        	}
        	int index = key.compareTo(n.key);
        	if (index < 0) {
        		return get (n.left, key);
        	}
        	if (index > 0) {
        		return get (n.right, key);
        	}
        	return n.val;
        }
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
    	root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
    	if (root == null) {
    		return false;
    	}
    	return root.get(root, key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. 
     */
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        return root.get(root, key);
    }

   /* Returns the number of key-value mappings in this map. */
    public int size() {
    	return size(root);
    }

    private int size(Node n) {
        if (root == null) {
    		return 0;
    	}
    	return root.N;

    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
    	if (root == null) {
    		return;
    	}
    	put(root, key, value);
    }

    private Node put(Node n, K key, V value) {
    	if (n == null) {
    		return new Node(key, value, 1);
    	}
    	int index = key.compareTo(n.key);
    	if (index < 0) {
    		n.left = put(n.left, key, value);
    	}
    	if (index > 0) {
    		n.right = put(n.right, key, value);
    	}
        if (index == 0) {
        	n.val = value;
        }
        n.N = 1 + size(n.left) + size(n.right);
        return n;

    }

    //prints out your BSTMap in order of increasing Key.
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node n) {
        if (n == null) {
        	return;
        }
        if (n.left == null && n.right == null) {
        	System.out.println(n.key);
        }
        printInOrder(n.left);
        printInOrder(n.right);
    }


    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }


}