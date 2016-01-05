

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.ArrayList;

/**
* A priority queue with a key, value pair
* @author Zhaohong Jin
*/
public class KeyValuePQ {

    //this is a max priority queue because the specified comparator
	private PriorityQueue<Double> pq 
	    = new PriorityQueue<Double>(20, Collections.reverseOrder());

	//this maps the elements of the qp to their value
	private HashMap<Double, ArrayList<TSTNode>> weightMap 
	    = new HashMap<Double, ArrayList<TSTNode>>();

    //size
    private int N;

    /**
    * @param n is the node to be enqueued
    */
	public void enqueue(TSTNode n) {
        if (n == null) {
            return;
        }
		if (!weightMap.containsKey(n.max)) {
			ArrayList<TSTNode> list = new ArrayList<TSTNode>();
			list.add(n);
			weightMap.put(n.max, list);
		} else {
			ArrayList<TSTNode> list = weightMap.get(n.max);
			list.add(n);
			weightMap.put(n.max, list);
		}
		pq.add(n.max);
        N = N + 1;
	}

    /**
    * return the character associated with the heaviest weight from the priority queue.
    * @return TSTNode ----> with the heaviest weight.
    */
	public TSTNode dequeue() {
		if (pq.peek() == null) {
			return null; //this is the null value of char
		}
        double weight = pq.poll();
        ArrayList<TSTNode> list = weightMap.get(weight);
        if (list.size() < 1) {
        	return null;
        }
        TSTNode c = list.get(0); //this is breaking the tie arbitrarily.
        list.remove(0);
        N = N - 1;
        weightMap.put(weight, list);
        return c;
	}

    /**
    * get the first character in the priority queue without dequeue it
    * @return TSTNode ---> the character in the front of the priority queue.
    */
	public TSTNode peek() {
		if (pq.peek() == null) {
			return null;
		}
		double weight = pq.peek();
        ArrayList<TSTNode> list = weightMap.get(weight);
        if (list.size() < 1) {
        	return null;
        }
        TSTNode n = list.get(0);
        return n;
	}

    /** 
     *  size() returns the number of objects in this CatenableQueue.
     *  @return the size of this CatenableQueue.
     **/
    public int size() {
        return N;
    }

    /**
     *  isEmpty() returns true if this CatenableQueue is empty, false otherwise.
     *  @return true if this CatenableQueue is empty, false otherwise. 
     **/
    public boolean isEmpty() {
        return size() == 0;
    }

    /** 
    * enqueue all the characters, if available, in the subentry of n
    * @param n the given node
    */
    public void enqueueSub(TSTNode n) {
        if (n.left != null) {
            enqueue(n.left);
        }
        if (n.middle != null) {
            enqueue(n.middle);
        }
        if (n.right != null) {
            enqueue(n.right);
        }
    }



}