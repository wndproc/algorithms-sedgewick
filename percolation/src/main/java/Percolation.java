import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private enum Site {
        BLOCKED, OPEN
    }

    private final Site[][] sites;
    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF unionFindWithoutBottom;
    private final int n;
    private final int first = 0;
    private final int last;
    private int openSites = 0;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }

        this.n = n;
        sites = new Site[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sites[i][j] = Site.BLOCKED;
            }
        }

        unionFind = new WeightedQuickUnionUF(n * n + 2);
        unionFindWithoutBottom = new WeightedQuickUnionUF(n * n + 1);

        //create virtual first site which connected to top
        for (int i = 1; i <= n; i++) {
            union(first, i);
        }
        //create virtual last site which connected to bottom
        last = n * n + 1;
        for (int i = n * (n - 1) + 1; i <= n * n; i++) {
            unionFind.union(last, i);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        int i = row - 1;
        int j = col - 1;
        if (sites[i][j] == Site.BLOCKED) {
            sites[i][j] = Site.OPEN;
            openSites++;

            int p = i * n + j;
            //connect with top
            if (i - 1 >= 0 && sites[i - 1][j] == Site.OPEN) {
                union(p, p - n);
            }

            //connect with bottom
            if (i + 1 < n && sites[i + 1][j] == Site.OPEN) {
                union(p, p + n);
            }

            //connect with left
            if (j - 1 >= 0 && sites[i][j - 1] == Site.OPEN) {
                union(p, p - 1);
            }

            //connect with right
            if (j + 1 < n && sites[i][j + 1] == Site.OPEN) {
                union(p, p + 1);
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row - 1][col - 1] == Site.OPEN;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return sites[row - 1][col - 1] == Site.OPEN &&
                unionFindWithoutBottom.connected((row - 1) * n + col - 1, first);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.connected(first, last);
    }

    private void union(int p, int q) {
        unionFind.union(p, q);
        unionFindWithoutBottom.union(p, q);
    }

    private void validate(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException("Invalid row / col");
        }
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}