package GrahamsScanConvexHull;


import java.util.Comparator;


public class Point implements Comparable<Point> {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    String printPoint() {
        return this.x + ", " + this.y;
    }



    // ------- Default compare

    // sort ascending by Y, break ties ascending by X.

    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // cross product compare of 3 points

    public int isLeftTurn(Point a, Point b, Point c) {

        double crossProduct = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (crossProduct < 0)
            return -1; // no, right turn
        else if (crossProduct > 0)
            return 1; // yes, left turn
        else
            return 0; // colinear
    }

    // Below is the custom sort comparator for polar angle sort...

    public Comparator<Point> polarAngleSort() {
        return new PolarAngleSort();
    }

    private class PolarAngleSort implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {

            // distances
            double distx1 = a.x - x;
            double disty1 = a.y - y;
            double distx2 = b.x - x;
            double disty2 = b.y - y;

            if (disty1 >= 0 && disty2 < 0) return -1; // a higher than b
            else if (disty2 >= 0 && disty1 < 0) return 1; // b higher than a
            else if (disty1 == 0  && disty2 == 0) { // all 3 points collinear
                if (distx1 >= 0 && distx2 < 0) return -1;
                else if (distx2 >= 0 && distx1 < 0) return 1;
                else return 0;
            }
            else return -isLeftTurn(Point.this, a, b);
        }

    }

}

