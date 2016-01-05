import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class MyHashMapTest {

    @Test
    public void part1() {
        MyHashMap<String, Integer> firstMap = new MyHashMap<String, Integer>();
        MyHashMap<String, Integer> secondMap = new MyHashMap<String, Integer>(50);
        MyHashMap<Integer, Integer> thirdMap = new MyHashMap<Integer, Integer>(25, 0.8f);

        firstMap.put("C-Ronaldo", 31);
        firstMap.put("Messi", 33);
        firstMap.put("Neimar", 15);

        assertEquals(3, firstMap.size());
        assertEquals(0, secondMap.size());
        assertEquals(0, thirdMap.size());

        assertEquals(true, firstMap.containsKey("C-Ronaldo"));
        assertEquals(true, firstMap.containsKey("Messi"));
        assertEquals(false, secondMap.containsKey("Messi"));

        for (int i = 0; i < 5000; i++) {
            thirdMap.put(i, i*2);
        }

        assertEquals(5000, thirdMap.size());
        assertEquals(true, thirdMap.containsKey(444));
        assertEquals(false, thirdMap.containsKey(10000));

        assertEquals((Integer) 888, thirdMap.get(444));

        thirdMap.remove(444);
        assertEquals(false, thirdMap.containsKey(444));

        thirdMap.remove(111, 222);
        assertEquals(false, thirdMap.containsKey(111));

        Set<Integer> s = thirdMap.keySet();

        assertEquals(4998, s.size());

    }

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(MyHashMapTest.class);
    }
}
