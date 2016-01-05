import java.util.AbstractList;

public class ArrayList61B<E> extends AbstractList<E> {
	private E[] items;
	private int size;
	private int numberOfElements;

	public ArrayList61B(int initialCapacity) {
		if (initialCapacity < 1){
			throw new IllegalArgumentException("");
		}
		items = (E[]) new Object[initialCapacity];
		size = initialCapacity;
		numberOfElements = 0;

	}

	public ArrayList61B(){
		this(1);
	}

	public E get(int i) {
		if (i < 0 || i>= size){
			throw new IllegalArgumentException("");
		}
		return items[i];
	}

	public boolean add(E item){
		if (numberOfElements >= size){
			E[] oldCopy = items;
			int oldSize = size;
			items = (E[]) new Object[size * 2];
			size = size * 2;

			int index = 0;
			while (index < oldSize){
				items[index] = oldCopy[index];
				index+=1;
			}
		}
		items[numberOfElements] = item;
		numberOfElements += 1;
		return true;

	}

	public int size(){
		return numberOfElements;
	}



}