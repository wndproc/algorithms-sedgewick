import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        verifyInput(item);
        Node<Item> node = new Node<>(item, head, null);
        if (head != null) {
            head.prev = node;
        } else {
            tail = node;
        }
        head = node;
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        verifyInput(item);
        Node<Item> node = new Node<>(item, null, tail);
        if (tail != null) {
            tail.next = node;
        } else {
            head = node;
        }
        tail = node;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        verifySize();
        Item item = head.item;
        Node<Item> nextNode = head.next;
        if (nextNode != null) {
            nextNode.prev = null;
        }
        head = nextNode;
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        verifySize();
        Item item = tail.item;
        Node<Item> prevNode = tail.prev;
        if (prevNode != null) {
            prevNode.next = null;
        }
        tail = prevNode;
        size--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void verifyInput(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null");
        }
    }

    private void verifySize() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
    }

    private static class Node<Item> {
        Item item;
        Node next;
        Node prev;

        public Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        Node<Item> currentNode = head;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator hasn't next");
            }
            Item item = currentNode.item;
            currentNode = currentNode.next;
            return item;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("1");
        deque.addFirst("2");
        deque.addLast("3");
        deque.addLast("4");
        for (String string : deque) {
            System.out.print(string + " ");
        }
        System.out.println("\n" + deque.removeFirst());
        for (String string : deque) {
            System.out.print(string + " ");
        }
        System.out.println("\n" + deque.removeLast());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
    }
}