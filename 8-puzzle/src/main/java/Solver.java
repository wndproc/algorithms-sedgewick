import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private final boolean solvable;
    private final int moves;
    private final List<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SearchNode> mainPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        SearchNode mainNode = new SearchNode(initial);
        mainPQ.insert(mainNode);
        SearchNode twinNode = new SearchNode(initial.twin());
        twinPQ.insert(twinNode);

        while (true) {
            mainNode = mainPQ.delMin();
            if (mainNode.board.isGoal()) {
                break;
            }
            addNeighbors(mainPQ, mainNode);

            twinNode = twinPQ.delMin();
            if (twinNode.board.isGoal()) {
                break;
            }
            addNeighbors(twinPQ, twinNode);
        }

        if (mainNode.board.isGoal()) {
            solvable = true;
            moves = mainNode.moves;
            solution = getSolution(mainNode);
        } else {
            solvable = false;
            moves = -1;
            solution = null;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private static void addNeighbors(MinPQ<SearchNode> pq, SearchNode searchNode) {
        Board predecessor = searchNode.predecessor != null ? searchNode.predecessor.board : null;
        for (Board neighbor : searchNode.board.neighbors()) {
            if (!neighbor.equals(predecessor)) {
                pq.insert(new SearchNode(neighbor, searchNode));
            }
        }
    }

    private static List<Board> getSolution(SearchNode lastSearchNode) {
        List<Board> solution = new ArrayList<>();
        SearchNode searchNode = lastSearchNode;
        while (searchNode != null) {
            solution.add(searchNode.board);
            searchNode = searchNode.predecessor;
        }
        Collections.reverse(solution);
        return solution;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        final Board board;
        final int moves;
        final SearchNode predecessor;
        final int hamming;

        SearchNode(Board board) {
            this.board = board;
            moves = 0;
            predecessor = null;
            hamming = board.hamming();
        }

        SearchNode(Board board, SearchNode predecessor) {
            this.board = board;
            this.moves = predecessor.moves + 1;
            this.predecessor = predecessor;
            hamming = board.hamming();
        }

        int getPriority() {
            return moves + hamming;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.getPriority(), o.getPriority());
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        // Board board = new Board(new int[][]{{1, 2}, {0, 3}});
        System.out.println(board);

        Solver solver = new Solver(board);
        System.out.println("Solvable: " + solver.solvable);
        System.out.println("Moves: " + solver.moves);
        System.out.println("Solution: ");
        for (Board neighbor : solver.solution()) {
            System.out.println(neighbor);
            System.out.println();
        }

        Solver twinSolver = new Solver(board.twin());
        System.out.println("Twin solvable: " + twinSolver.solvable);
    }
}