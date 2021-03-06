import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int n;
    private final int trials;
    private final double[] trialsResults;
    private double mean;
    private double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials should be > 0");
        }
        this.n = n;
        this.trials = trials;
        trialsResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            trialsResults[i] = trial();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean == 0.0) {
            mean = StdStats.mean(trialsResults);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev == 0.0) {
            stddev = StdStats.stddev(trialsResults);
        }
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidence();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidence();
    }

    private double confidence() {
        return 1.96 * stddev() / Math.sqrt(trials);
    }

    private double trial() {
        Percolation percolation = new Percolation(n);
        int x = 0;
        while (!percolation.percolates()) {
            openRandomSite(percolation);
            x++;
        }
        return ((double) x) / (n * n);
    }

    private void openRandomSite(Percolation percolation) {
        while (true) {
            int row = 1 + StdRandom.uniform(n);
            int col = 1 + StdRandom.uniform(n);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
                return;
            }
        }
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.printf("mean                    = %f\n", percolationStats.mean());
        System.out.printf("stddev                  = %f\n", percolationStats.stddev());
        System.out.printf(
                "95%% confidence interval = [%f, %f]\n",
                percolationStats.confidenceLo(),
                percolationStats.confidenceHi()
        );
    }
}