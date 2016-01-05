package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        arb.enqueue(33);
        arb.enqueue(44);
        arb.enqueue(62);
        arb.enqueue(-34);
        double peek = arb.peek();
        assertEquals(33, peek, 0);
        assertEquals(10, arb.capacity());
        assertEquals(4, arb.fillCount());
        assertEquals(false, arb.isEmpty());
        assertEquals(false, arb.isFull());
        for (int i = 0; i < 6; i++){
        	arb.enqueue(10);
        }
        assertEquals(true, arb.isFull());
        assertEquals(10, arb.fillCount());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
}
