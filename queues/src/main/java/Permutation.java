import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (true) {
            try {
                randomizedQueue.enqueue(StdIn.readString());
            } catch (NoSuchElementException e) {
                break;
            }
        }
        Iterator<String> iterator = randomizedQueue.iterator();
        for (int i = 0; i < k && iterator.hasNext(); i++) {
            System.out.println(iterator.next());
        }
    }
}
