
package csc212;

public class Node<T> {
    public T data;
    public Node<T> next;

    public Node(T value) {
        data = value;
        next = null;
    }
}
