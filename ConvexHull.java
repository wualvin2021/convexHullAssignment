import java.util.*;

/*
 * Convex Hull implementation using Graham's algorithm.
 * Point class represents a point in 2D space with x and y coordinates.
 * The algorithm finds the convex hull of a set of 2D points.
 */
class Point implements Comparable<Point> {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Compare first by y, then by x (for pivot selection)
    @Override
    public int compareTo(Point p) {
        if (this.y == p.y) return this.x - p.x;
        return this.y - p.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
/*
 * Implementation of Graham's algorithm to find the convex hull of a set of points.
 * The algorithm sorts the points by polar angle with respect to a pivot point,
 * then constructs the convex hull using a stack to maintain the vertices of the hull.
 * The orientation test is used to determine the turn direction between three points.
 */
public class ConvexHull {

    // Orientation test: returns positive for counter-clockwise,
    public static int orientation(Point a, Point b, Point c) {
        return (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
    }

    // Distance squared between two points
    public static int distSq(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    // Graham Scan main method
    public static List<Point> grahamScan(List<Point> points) {
        if (points.size() < 3) return points; // trivial case

        // Find pivot (lowest y, then x)
        Point pivot = Collections.min(points);

        // Sort by polar angle w.r.t pivot
        points.sort((p1, p2) -> {
            int orient = orientation(pivot, p1, p2);
            if (orient == 0) {
                // closer point first if collinear
                return distSq(pivot, p1) - distSq(pivot, p2);
            }
            return -orient; // counter-clockwise first
        });

        // Build hull using stack
        Stack<Point> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        // Process remaining points
        for (int i = 2; i < points.size(); i++) {
            Point top = hull.pop();
            while (!hull.isEmpty() && orientation(hull.peek(), top, points.get(i)) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }

    // Test the Convex Hull implementation
    public static void main(String[] args) {
        List<Point> pts = Arrays.asList(
                new Point(0, 3), new Point(2, 2),
                new Point(1, 1), new Point(2, 1),
                new Point(3, 0), new Point(0, 0),
                new Point(3, 3)
        );

        List<Point> hull = grahamScan(new ArrayList<>(pts));
        System.out.println("Convex Hull: " + hull);
    }
}
