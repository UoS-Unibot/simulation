package org.evors.core.util.geometry;

import static java.lang.Double.NaN;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents an intersection calculation between two shapes, storing whether an
 * intersection is present, and the smallest line distance depending on the
 * types of shapes.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Intersection {

    /**
     * Point of intersection. Is NaN if there is no intersection.
     */
    public final Vector2D intersectionPoint;
    /**
     * Whether the two Shape2Ds given intersect.
     */
    public final boolean isIntersection;

    private Intersection(Vector2D intersectionPoint, boolean isIntersection) {
        this.intersectionPoint = intersectionPoint;
        this.isIntersection = isIntersection;
    }

    /**
     * Used to return an Intersection object specifying no intersection present
     *
     * @return an Intersection with isIntersection set to false.
     */
    public static Intersection noIntersection() {
        return new Intersection(Vector2D.NaN, false);
    }

    private static double cross2D(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getY() - v1.getY() * v2.getX();
    }

    /**
     * Used to get the lowest line distance to the intersection - NaN by
     * default, but overridden by more specific Intersection subclasses with
     * their own implementation.
     *
     * @return NaN
     */
    public double getSmallestLineDist() {
        return Double.NaN;
    }

    /**
     * Represents an intersection between two lines.
     */
    public static class LineLine extends Intersection {

        /**
         * Distance between line.p1 and the intersection point. Is NaN if there
         * is no intersection.
         */
        public final double line1DistToIntersect,
                /**
                 * Distance between line2.p1 and the intersection point. Is NaN
                 * if there is no intersection.
                 */
                line2DistToIntersect;

        private LineLine() {
            super(Vector2D.NaN, false);
            line1DistToIntersect = NaN;
            line2DistToIntersect = NaN;
        }

        private LineLine(double line1DistToIntersect,
                double line2DistToIntersect,
                Vector2D intersectionPoint, boolean intersecting) {
            super(intersectionPoint, intersecting);
            this.line1DistToIntersect = line1DistToIntersect;
            this.line2DistToIntersect = line2DistToIntersect;
        }

        /**
         * Constructs a new LineIntersection between the specified lines,
         * computing whether there is an intersection.
         *
         * @param line1 First line.
         * @param line2 Second line.
         * @return A LineLine object with the details of the intersection.
         */
        public static LineLine calculate(Line line1, Line line2) {
            /**
             * Implementation of http://stackoverflow.com/a/565282 Imagine that
             * both lines can be represented by the vectors p + r and q + s.
             * This algorithm attempts to find two scalars t and u such that p +
             * tr = q + us; this is the intersection point. Several cases must
             * be ruled out - collinearity (lines are 'lined up'), overlapping
             * lines and parallel lines.
             */
            Vector2D intersectionPoint;
            boolean isIntersection;
            double line1DistToIntersect, line2DistToIntersect;
            Vector2D q = line2.p1,
                    p = line1.p1,
                    s = line2.p2.subtract(line2.p1),
                    r = line1.p2.subtract(line1.p1),
                    qp = q.subtract(p);
            double rs = cross2D(r, s),
                    qpCrossR = cross2D(qp, r),
                    qpCrossS = cross2D(qp, s),
                    t = qpCrossS / rs,
                    u = qpCrossR / rs;
            if (rs == 0) {
                isIntersection = false;
                /**
                 * There are three possible cases if the cross product of r and
                 * s is 0, all of which result in Nan being returned: (1) the
                 * lines are parallel and do not intersect, (2) the lines are
                 * collinear and disjoint, with no intersection, or (3) the
                 * lines are collinear and overlapping, with no definitive
                 * intersection point.
                 */
                intersectionPoint = Vector2D.NaN;
                line1DistToIntersect = NaN;
                line2DistToIntersect = NaN;
            } else if (0 < t && t < 1
                    & 0 < u & u < 1) {
                //lines intersect, return p + tr
                intersectionPoint = p.add(t, r);
                isIntersection = true;
                line1DistToIntersect = t * r.getNorm();
                line2DistToIntersect = u * s.getNorm();
            } else {
                //non parallel and not intersecting
                intersectionPoint = Vector2D.NaN;
                isIntersection = false;
                line1DistToIntersect = NaN;
                line2DistToIntersect = NaN;
            }
            return new LineLine(line1DistToIntersect, line2DistToIntersect,
                    intersectionPoint, isIntersection);
        }

        /**
         * Returns the distance of the first line p1 to the intersection point.
         * Use line2DistToIntersect for the second line, and calculate distance
         * from the intersection point for other points.
         *
         * @return distance between first line p1 and intersection point, or NaN
         * if no intersection.
         */
        @Override
        public double getSmallestLineDist() {
            return line1DistToIntersect;
        }

    }

    /**
     * Represents an intersection between a circle and a line.
     */
    public static class CircleLine extends Intersection {

        /**
         * Point of first line intersection. NaN if no intersection.
         */
        public final Vector2D intersectp1,
                /**
                 * Point of second line intersection. NaN if line given is
                 * tangent to the circle
                 */
                intersectp2;

        /**
         * Whether the line given is a tangent to the circle.
         */
        public final boolean isTangent;
        /**
         * Distance from the first line segment to the first intersection point.
         */
        public final double linedistP1,
                /**
                 * Distance from the second intersection point to the end of the
                 * line.
                 */
                linedistP2;

        private CircleLine() {
            super(Vector2D.NaN, false);
            this.intersectp1 = Vector2D.NaN;
            this.intersectp2 = Vector2D.NaN;
            this.isTangent = false;
            this.linedistP1 = Double.NaN;
            this.linedistP2 = Double.NaN;
        }

        private CircleLine(Vector2D intersectp1, Vector2D intersectp2,
                boolean isTangent, double linedistP1, double linedistP2) {
            super(intersectp1, true);
            this.intersectp1 = intersectp1;
            this.intersectp2 = intersectp2;
            this.isTangent = isTangent;
            this.linedistP1 = linedistP1;
            this.linedistP2 = linedistP2;
        }

        /**
         * Calculates an intersection between the given circle and line.
         *
         * @param circle Circle
         * @param line Line
         * @return a CircleLine intersection object with information about the
         * intersection
         */
        public static CircleLine calculate(Circle circle, Line line) {

            //Implemented from http://mathworld.wolfram.com/Circle-LineIntersection.html
            Vector2D intersectp1, intersectp2;
            double linedistP1, linedistP2,
                    x1, y1, x2, y2;
            double dx = line.p2.getX() - line.p1.getX(),
                    dy = line.p2.getY() - line.p1.getY(),
                    dr = Math.sqrt(dx * dx + dy * dy),
                    D = line.p1.getX() * line.p2.getY() - line.p1.getY()
                    * line.p2.getX(),
                    discrim = circle.getRadius() * circle.getRadius() * dr * dr
                    - D * D;
            if (discrim < 0) { //no intersection
                return new CircleLine();
            } else if (discrim == 0) {
                //line is a tangent to the circle
                intersectp1 = new Vector2D((D * dy) / (dr * dr), (D * dx) / (dr
                        * dr));
                intersectp2 = Vector2D.NaN;
                linedistP1 = intersectp1.subtract(line.p1).getNorm();
                linedistP2 = line.p2.subtract(intersectp1).getNorm();
                return new CircleLine(intersectp1, intersectp2, true, linedistP1,
                        linedistP2);
            }
            double sqrtDiscrim = Math.sqrt(discrim);
            x1 = (D * dy + sgn(dy) * dx * sqrtDiscrim) / (dr * dr);
            x2 = (D * dy - sgn(dy) * dx * sqrtDiscrim) / (dr * dr);
            y1 = (-D * dx + Math.abs(dy) * sqrtDiscrim) / (dr * dr);
            y2 = (-D * dx - Math.abs(dy) * sqrtDiscrim) / (dr * dr);
            intersectp1 = new Vector2D(x1, y1);
            intersectp2 = new Vector2D(x2, y2);
            linedistP1 = intersectp1.subtract(line.p1).getNorm();
            linedistP2 = line.p2.subtract(intersectp2).getNorm();
            return new CircleLine(intersectp1, intersectp2, false, linedistP1,
                    linedistP2);
        }

        private static int sgn(double x) {
            return x < 0 ? -1 : 1;
        }

        /**
         * Returns the smallest line distance to intersection.
         *
         * @return the line distance from the line p1 to the intersection.
         */
        @Override
        public double getSmallestLineDist() {
            return linedistP1;
        }

    }
}
