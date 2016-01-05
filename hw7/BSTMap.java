//Author: Zhaohong Jin 
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key; // sorted by key
        private V val; // associated data
        private Node left, right; // left and right subtrees
        private int N; // number of nodes in subtree

        public Node(K k, V v, int n) {
            this.key = k;
            this.val = v;
            this.N = n;
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
        return get(key) != null;
    }

    /*
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return get(root, key);
    }
    //inspiration : http://algs4.cs.princeton.edu/32bst/BST.java.html
    private V get(Node n, K key) {
        if (n == null) {
            return null;
        }
        int index = key.compareTo(n.key);
        if (index < 0) {
            return get(n.left, key);
        }
        if (index > 0) {
            return get(n.right, key);
        }
        return n.val;
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
        if (value == null) {
            return;
        }
        root = put(root, key, value);
    }

    //inspiration : http://algs4.cs.princeton.edu/32bst/BST.java.html
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
        n.N = n.N + 1;
        return n;
    }

    // prints out your BSTMap in order of increasing Key.
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
        V value = get(key);
        root = remove(root, key);
        return value;
    }

    private Node remove(Node n, K key) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            n.right = remove(n.right, key);
        }
        if (cmp < 0) {
            n.left = remove(n.left, key);
        } else {
            if (n.right == null) {
                return n.left;               
            }
            if (n.left == null) {
                return n.right;
            }
            Node t = n;
            n = min(t.right);
            n.right = deleteMin(t.right);
            n.left = t.left;
        }
        n.N = n.N - 1;
        return n;

    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else                return min(x.left);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = deleteMin(x.left);
        x.N = x.N - 1;
        return x;
    }

    private Node remove(Node n, K key, V value) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            n.right = remove(n.right, key);
        }
        if (cmp < 0) {
            n.left = remove(n.left, key);
        }
        if (cmp == 0 && n.val == value) {
            if (n.right == null) {
                return n.left;               
            }
            if (n.left == null) {
                return n.right;
            }
            Node t = n;
            n = min(t.right);
            n.right = deleteMin(t.right);
            n.left = t.left;
        }
        if (cmp == 0 && n.val != value) {
            return null;
        }    
        n.N = n.N - 1;
        return n;
    }

    @Override
    public V remove(K key, V value) {
        root = remove(root, key, value);
        return value;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

}
