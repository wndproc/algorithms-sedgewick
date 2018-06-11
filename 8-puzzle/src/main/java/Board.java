import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] blocks;
    private final int n;
    private int zeroI;
    private int zeroJ;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = copyArray(blocks);
        n = blocks.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                    return;
                }
            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    continue;
                }
                hamming += blocks[i][j] == expected(i, j) ? 0 : 1;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    continue;
                }
                manhattan += Math.abs(i - expectedI(blocks[i][j]));
                manhattan += Math.abs(j - expectedJ(blocks[i][j]));
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != expected(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twin = copyArray(blocks);
        exchange(twin, 0, 0, 0, 1);
        return new Board(twin);
    }

    // does this board equal y?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return n == board.n && Arrays.deepEquals(blocks, board.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        if (zeroI - 1 >= 0) {
            neighbors.add(createNeighbor(zeroI - 1, zeroJ));
        }
        if (zeroI + 1 < n) {
            neighbors.add(createNeighbor(zeroI + 1, zeroJ));
        }
        if (zeroJ - 1 >= 0) {
            neighbors.add(createNeighbor(zeroI, zeroJ - 1));
        }
        if (zeroJ + 1 < n) {
            neighbors.add(createNeighbor(zeroI, zeroJ + 1));
        }
        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(blocks[i][j]);
                if (j < n - 1) {
                    sb.append(" ");
                }
            }
            if (i < n - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private int expected(int i, int j) {
        return i == (n - 1) && j == (n - 1) ? 0 : j + i * n + 1;
    }

    private int expectedI(int value) {
        return value == 0 ? n - 1 : (value - 1) / n;
    }

    private int expectedJ(int value) {
        return value == 0 ? n - 1 : (value - 1) % n;
    }

    private static int[][] copyArray(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return copy;
    }

    private static void exchange(int[][] array, int i1, int j1, int i2, int j2) {
        int temp = array[i1][j1];
        array[i1][j1] = array[i2][j2];
        array[i2][j2] = temp;
    }

    private Board createNeighbor(int i, int j) {
        int[][] copy = copyArray(blocks);
        exchange(copy, zeroI, zeroI, i, j);
        return new Board(copy);
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        System.out.println(board.toString());
        System.out.println("is goal: " + board.isGoal());
        System.out.println("hamming: " + board.hamming());
        System.out.println("manhattan: " + board.manhattan());

        System.out.println();
        System.out.println("Neighbors");
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
            System.out.println("is goal: " + neighbor.isGoal());
            System.out.println("hamming: " + neighbor.hamming());
            System.out.println("manhattan: " + neighbor.manhattan());
        }

        System.out.println();
        System.out.println("Twin");
        System.out.println(board.twin());
    }
}