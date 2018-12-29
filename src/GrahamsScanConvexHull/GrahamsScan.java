package GrahamsScanConvexHull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GrahamsScan {

    private static Stack<Point> convexHull = new Stack<Point>();

    public static void main(String[] args) throws IOException {

        File pointsFile = new File(args[0]);

        BufferedReader br = new BufferedReader(new FileReader(pointsFile));

        List<Point> points = new ArrayList<>();


        // -----------------------------------------------

        // extracting the point from the .txt file line by line.

        String pointString;

        while ((pointString = br.readLine()) != null){
            String[] p = pointString.split(" ");
            String xStr = p[0];
            int x = Integer.parseInt(xStr);
            String yStr = p[1];
            int y = Integer.parseInt(yStr);
            Point pt = new Point(x, y);
            points.add(pt);
        }

        Point[] pointsArray = new Point[points.size()];

        // -----------------------------------------------

        Point p0 = getOrigin(points);
        System.out.println("p0 = " + p0.printPoint());

        // -----------------------------------------------

        // from list to array...

        for (int i = 0; i < points.size(); i++) {
            pointsArray[i] = points.get(i);
        }

        // pre-sort by y-coord :: pre process

        System.out.println("Before sort: ");
        for (Point p : pointsArray) {
            System.out.println(p.printPoint());
        }

        Arrays.sort(pointsArray);

        System.out.println("After sort: ");
        for (Point p : pointsArray) {
            System.out.println(p.printPoint());
        }

        // Now that we have the origin p0, we can calculate the polar angle of all the rest of the points pointsArray.get(1) through pointsArray.get(n) in regards to p0.

        // custom sort by Comparator polarAngleSort()j
        Arrays.sort(pointsArray, 1, points.size(), pointsArray[0].polarAngleSort());

        System.out.println("After Polar sort: ");

        for (Point p : pointsArray) {
            System.out.println(p.printPoint());
        }

        // Now, we have a list of all the ingested points sorted by their ascending polar angle with distance as a tie breaker from the origin point p0.

        // all that remains is to use our isLeftTurn() and stack logic to determine the points of the convex hull.

        getConvexHull(pointsArray);

        Point[] convexArray = stackToArray(convexHull);


//        System.out.println("Convex hull points printed from array: ");
//
//        for (Point p : convexArray) {
//            System.out.println(p.printPoint());
//        }
    }


    private static Point getOrigin(List<Point> points){

        Point p0 = points.get(0); // need to find the "lowest" point, x is tie break. Start with first in list.

        for (Point pt : points) {

            if (pt.y < p0.y) {
                p0 = pt;
            }
            if (pt.y == p0.y) {
                if (pt.x < p0.x) {
                    p0 = pt;
                }
            }
        }

        return p0;
    }


    private static Stack<Point> getConvexHull(Point[] polarOrderedPoints) {

        Point origin
                = polarOrderedPoints[0];

        convexHull.push(origin); // push bottom-most point onto hull as origin.

        Point first
                = polarOrderedPoints[1];

//        convexHull.push(first); // push next polar-angled point.

        // find first non-collinear point to origin and first

        int next;

        for (next = 2; next < polarOrderedPoints.length; next++)
            if (origin.isLeftTurn(origin, first, polarOrderedPoints[next]) != 0) {
                break;
            }
            convexHull.push(polarOrderedPoints[next-1]);


        for (int i = next; i < polarOrderedPoints.length; i++) {
            Point topOfStack = convexHull.pop();
            while (origin.isLeftTurn(convexHull.peek(), topOfStack, polarOrderedPoints[i]) <= 0) {
                topOfStack = convexHull.pop();
            }
            convexHull.push(topOfStack);
            convexHull.push(polarOrderedPoints[i]);
        }

        System.out.println("Convex Hull Points: ");

        for (int i = 0; i < convexHull.size(); i++) {
            System.out.println(convexHull.get(i).printPoint());
        }


        return convexHull;
    }

    public static Point[] stackToArray (Stack<Point> stack) {

        Point[] convexArray = new Point[stack.size()];

        for (int i = 0; i < stack.size(); i++) {
            convexArray[i] = stack.get(i);
        }

        return convexArray;
    }
}
