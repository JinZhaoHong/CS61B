public class MyMap<K, V> {
    public K key;
    public V val;
    public MyMap next;

    public MyMap (K key, V val) {
        this.key = key;
        this.val = val;
    }

}
