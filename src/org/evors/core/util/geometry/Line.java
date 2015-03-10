/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import java.awt.geom.Line2D;
import static java.lang.Double.NaN;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The Line class is the basis of the geometry API, representing a line segment
 * with the ability to translate and rotate. Line-Line intersections can also be
 * calculated, returning the LineIntersection helper class, which provides
 * information on line intersections.
 *
 * For initialisation, the new() constructor be specified with two points;
 * alternatively for ease of use a number of static factory helper methods are
 * provided.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Line extends Shape2D {

    /**
     * Base point of the line segment.
     */
    public Vector2D p1;

    /**
     * Point this line segment projects to.
     */
    public Vector2D p2;

    /**
     * Initialises a Line with points [0,0],[0,0].
     */
    public Line() {
        this(Vector2D.ZERO, Vector2D.ZERO);
    }

    /**
     * Creates a new Line from the specified coordinates. Equivalent to return
     * new Line(new Vector2D(x1, y1), new Vector2D(x2, y2));
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return a new Line with the specified coordinates.
     */
    public static Line fromCoords(double x1, double y1, double x2, double y2) {
        return new Line(new Vector2D(x1, y1), new Vector2D(x2, y2));
    }

    private static Vector2D translatePolar(Vector2D v, double angle,
            double length) {
        return new Vector2D(
                v.getX() + length * Math.cos(angle),
                v.getY() + length * Math.sin(angle)
        );
    }

    /**
     * Creates a new line specifying the base point as a Vector2D and polar
     * notation.
     *
     * @param p1
     * @param angle Angle in radians.
     * @param length
     * @return a new Line created from the specified polar coordinates.
     */
    public static Line fromPolarVec(Vector2D p1, double angle, double length) {
        return new Line(
                p1,
                translatePolar(p1, angle, length)
        );
    }

    /**
     * Creates a new line specifying the base coordinates and polar notation.
     *
     * @param x1
     * @param y1
     * @param angle Angle in radians.
     * @param length
     * @return a new Line created from the specified polar coordinates.
     */
    public static Line fromPolarCoords(double x1, double y1, double angle,
            double length) {
        return fromPolarVec(new Vector2D(x1, y1), angle, length);
    }

    /**
     * Creates a new line extending symmetrically from the specified center
     * point with length and rotation provided.
     *
     * @param center Center point of the new Line.
     * @param length Total length of the line; it will extend by half this in
     * either direction.
     * @param angle Orientation of the line, with the center point as the pivot.
     * @return a new Line created from the specified center point and polar
     * coordinates.
     */
    public static Line fromCenterPoint(Vector2D center, double length,
            double angle) {
        return new Line(
                translatePolar(center, (angle + Math.PI) % (2 * Math.PI), length
                        / 2),
                translatePolar(center, angle, length / 2)
        );
    }

    /**
     * Convenience constructor; creates a new line extending symmetrically from
     * the specified center coordinates with length and rotation provided.
     *
     * @param c1
     * @param c2
     * @param length
     * @param angle Angle in radians.
     * @return
     */
    public static Line fromCenterPoint(double c1, double c2, double length,
            double angle) {
        return fromCenterPoint(new Vector2D(c1, c2), length, angle);
    }

    /**
     * Initialises a Line with the points provided.
     *
     * @param p1 Base point of the line segment.
     * @param p2 Second point of the line segment.
     */
    public Line(Vector2D p1, Vector2D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Implementation of Shape2D; returns a collection with the sole member
     * being this line.
     *
     * @return a single element Collection with this Line.
     */
    @Override
    public Collection<Line> getLines() {
        LinkedList<Line> lines = new LinkedList<>();
        lines.add(this);
        return lines; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Computes an intersection between this and the specified line segment.
     *
     * @param line2 Other line to intersect with.
     * @return a LineIntersection object with information about the
     * intersection.
     */
    public LineIntersection getIntersection(Line line2) {
        return new LineIntersection(this, line2);
    }

    /**
     * Rotates this line around the base point by the specified delta angle.
     *
     * @param deltaAngle Angle to rotate by in radians.
     */
    @Override
    public void rotate(double deltaAngle) {
        p2 = getRotatedPoint(p1, p2, deltaAngle);
    }

    /**
     * Rotates this line around the specified pivot point by the specified delta
     * angle.
     *
     * @param pivot Point to rotate relative to.
     * @param deltaAngle Angle to rotate by in radians.
     */
    public void rotate(Vector2D pivot, double deltaAngle) {
        p1 = getRotatedPoint(pivot, p1, deltaAngle);
        p2 = getRotatedPoint(pivot, p2, deltaAngle);
    }

    /**
     * Returns a Java AWT geom Line2D with the same points as this line.
     * Typically used for rendering.
     *
     * @return Line2D with same points as this line.
     */
    public Line2D toLine2D() {
        return new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    /**
     * Returns the length of this line.
     *
     * @return Length between the two line points.
     */
    public double getLength() {
        return p2.subtract(p1).getNorm();
    }

    private Vector2D getRotatedPoint(Vector2D pivot, Vector2D point,
            double deltaAngle) {
        return new Vector2D(
                pivot.getX() + Math.cos(deltaAngle) * (point.getX() - pivot.
                getX())
                - Math.sin(deltaAngle) * (point.getY() - pivot.getY()),
                pivot.getY() + Math.sin(deltaAngle) * (point.getX() - pivot.
                getX())
                + Math.cos(deltaAngle) * (point.getY() - pivot.getY())
        );
    }

    /**
     * Translates this line by the specified change vector.
     *
     * @param deltaMovement Change vector specifying amount of movement.
     */
    @Override
    public void translate(Vector2D deltaMovement) {
        p1 = p1.add(deltaMovement);
        p2 = p2.add(deltaMovement);
    }

    private static double cross2D(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getY() - v1.getY() * v2.getX();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Line{" + "p1=" + p1 + ", p2=" + p2 + '}';
    }

    /**
     * Provides a LineIntersection with intersecting = false.
     *
     * @return
     */
    public static LineIntersection getNoIntersection() {
        return LineIntersection.noIntersection();
    }

    /**
     * Computes Line - Line intersections and provides information about
     * intersection point and distances on either line segment.
     */
    public static class LineIntersection {

        /**
         * Whether the two Lines given intersect.
         */
        public final boolean isIntersection;
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

        /**
         * Point of intersection. Is NaN if there is no intersection.
         */
        public final Vector2D intersectionPoint;

        /**
         * Provides a LineIntersection with intersecting = false.
         *
         * @return
         */
        public static LineIntersection noIntersection() {
            return new LineIntersection();
        }

        private LineIntersection() {
            isIntersection = false;
            line1DistToIntersect = NaN;
            line2DistToIntersect = NaN;
            intersectionPoint = Vector2D.NaN;
        }

        /**
         * Constructs a new LineIntersection between the specified lines,
         * computing whether there is an intersection.
         *
         * @param line1
         * @param line2
         */
        public LineIntersection(Line line1, Line line2) {
            /**
             * Implementation of http://stackoverflow.com/a/565282 Imagine that
             * both lines can be represented by the vectors p + r and q + s.
             * This algorithm attempts to find two scalars t and u such that p +
             * tr = q + us; this is the intersection point. Several cases must
             * be ruled out - collinearity (lines are 'lined up'), overlapping
             * lines and parallel lines.
             */
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
        }

    }
}
