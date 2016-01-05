
public class DoubleChain {
	
	private DNode head;
	
	public DoubleChain(double val) {
		/* your code here. */
		head = new DNode(val); 
	}

	public DNode getFront() {
		return head;
	}

	/** Returns the last item in the DoubleChain. */		
	public DNode getBack() {
		/* your code here */
		if (head.next ==null){
			return head;
		}
		DNode pointer = head;
		while (pointer.next != null){
			pointer = pointer.next;			
		}
		return pointer;

	}
	
	/** Adds D to the front of the DoubleChain. */	
	public void insertFront(double d) {
		/* your code here */
		DNode newNode = new DNode(d);
		newNode.next = head;
		head.prev = newNode;
		head = newNode;
	}
	
	/** Adds D to the back of the DoubleChain. */	
	public void insertBack(double d) {
		/* your code here */
		DNode newNode = new DNode(d);
		if (head.next == null){
			head.next = newNode;
			newNode.prev = head;
		}
		DNode pointer = head;
		while (pointer.next!= null){
			pointer = pointer.next;
		}
		pointer.next = newNode;
		newNode.prev = pointer;
	}
	
	/** Removes the last item in the DoubleChain and returns it. 
	  * This is an extra challenge problem. */
	public DNode deleteBack() {
		/* your code here */
		return null;
	}
	/** Returns a string representation of the DoubleChain. 
	  * This is an extra challenge problem. */
	public String toString() {
		/* your code here */		
		return null;
	}

	public static class DNode {
		public DNode prev;
		public DNode next;
		public double val;
		
		private DNode(double val) {
			this(null, val, null);
		}
		
		private DNode(DNode prev, double val, DNode next) {
			this.prev = prev;
			this.val = val;
			this.next =next;
		}
	}
	
}
