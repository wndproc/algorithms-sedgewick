import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIAL_CAPACITY = 8;
    private int size = 0;
    private Item[] array;
    private int tail = -1;

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[INITIAL_CAPACITY];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        verifyInput(item);
        checkSizeAndResize();
        array[++tail] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        verifySize();
        while (true) {
            int randomIndex = StdRandom.uniform(tail + 1);
            Item item = array[randomIndex];
            if (item != null) {
                array[randomIndex] = null;
                size--;
                checkSizeAndResize();
                return item;
            }
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        verifySize();
        while (true) {
            int randomIndex = StdRandom.uniform(tail + 1);
            Item item = array[randomIndex];
            if (item != null) {
                return item;
            }
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private void verifyInput(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null");
        }
    }

    private void verifySize() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
    }

    private void checkSizeAndResize() {
        boolean arrayIsFull = tail == array.length - 1;
        boolean arrayIsEmpty = size <= array.length / 4 && array.length > INITIAL_CAPACITY;
        if (arrayIsEmpty) {
            array = copyArray(array.length / 2);
            tail = size - 1;
        } else if (arrayIsFull) {
            array = copyArray(array.length * 2);
            tail = size - 1;
        } else if (size == 0) {
            tail = -1;
        }
    }

    private Item[] copyArray(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        for (int oldIndex = 0, newIndex = 0; oldIndex <= tail; oldIndex++) {
            if (array[oldIndex] != null) {
                newArray[newIndex] = array[oldIndex];
                newIndex++;
            }
        }
        return newArray;
    }

    private class RandomIterator implements Iterator<Item> {
        Item[] randomArray;
        int currentItem;

        public RandomIterator() {
            randomArray = copyArray(size);
            StdRandom.shuffle(randomArray);
        }

        @Override
        public boolean hasNext() {
            return currentItem < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator hasn't next");
            }
            return randomArray[currentItem++];
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        queue.enqueue("6");
        queue.enqueue("7");
        queue.enqueue("8");
        for (String string : queue) {
            System.out.print(string + " ");
        }
        System.out.println("\n");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        for (String string : queue) {
            System.out.print(string + " ");
        }
    }
}