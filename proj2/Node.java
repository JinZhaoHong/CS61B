import java.io.Serializable;
//Author: Zhaohong Jin
public class Node<T> implements Serializable {

    public T head;
    public String message;
    public String time;
    public int id;
    public String branch;
    public Node<T> tail;

    public Node(T head, String message, String time, int id, String branch, Node<T> tail) {
        this.head = head;
        this.message = message;
        this.time = time;
        this.id = id;
        this.branch = branch;
        this.tail = tail;
    }

}
