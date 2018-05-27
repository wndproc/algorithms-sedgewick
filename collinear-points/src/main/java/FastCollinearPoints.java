import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] initialPoints) {
        validateForNulls(initialPoints);
        Point[] points = Arrays.copyOf(initialPoints, initialPoints.length);
        Arrays.sort(points);
        validateForDuplicates(points);

        for (int a = 0; a < points.length - 3; a++) {
            Point[] sortedPoints = Arrays.copyOfRange(points, a + 1, points.length);
            Arrays.sort(sortedPoints, points[a].slopeOrder());
            double slope = points[a].slopeTo(sortedPoints[0]);
            int numberOfSegments = 2;
            for (int i = 1; i < sortedPoints.length; i++) {
                double nextSlope = points[a].slopeTo(sortedPoints[i]);
                if (nextSlope == slope) {
                    numberOfSegments++;
                    if (i != sortedPoints.length - 1) {
                        continue;
                    }
                }
                if (numberOfSegments >= 4) {
                    segments.add(new LineSegment(points[a], sortedPoints[i - 1]));
                }
                numberOfSegments = 2;
                slope = nextSlope;
            }
        }
    }

    private void validateForNulls(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void validateForDuplicates(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        // In in = new In("/Users/merzlyakov/projects/other/algorithms-sedgewick/collinear-points/src/main/resources/input6.txt");
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}