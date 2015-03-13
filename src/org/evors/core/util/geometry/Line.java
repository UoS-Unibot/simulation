package org.evors.core.util.geometry;

import java.awt.geom.Line2D;
import static java.lang.Double.NaN;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.util.LookupFunctions;

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
public class Line extends Polygon {

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
                v.getX() + length * LookupFunctions.cos(angle),
                v.getY() + length * LookupFunctions.sin(angle)
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
     * Implementation of Polygon; returns a collection with the sole member
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
    public Intersection.LineLine getIntersection(Line line2) {
        return Intersection.LineLine.calculate(this, line2);
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
                pivot.getX() + LookupFunctions.cos(deltaAngle) * (point.getX() - pivot.
                getX())
                - LookupFunctions.sin(deltaAngle) * (point.getY() - pivot.getY()),
                pivot.getY() + LookupFunctions.sin(deltaAngle) * (point.getX() - pivot.
                getX())
                + LookupFunctions.cos(deltaAngle) * (point.getY() - pivot.getY())
        );
    }

    /**
     * Gets the shortest distance from the line to a given point.
     *
     * @param point Point to get distance from.
     * @return Shortest distance from the line to a given point
     */
    public double getShortestDistToPoint(Vector2D point) {
        /**
         * Adapted from C code by Prof Phil Husbands.
         */
        double l2, t;
        Vector2D A, B, proj;
        Vector2D line = p2.subtract(p1);
        l2 = line.getNormSq();
        if (l2 == 0) {
            return point.subtract(p1).getNorm();
        }
        A = point.subtract(p1);
        B = line;
        t = A.dotProduct(B) / l2;
        if (t < 0) {
            return point.subtract(p1).getNorm(); //actual point lies off p1
        } else if (t > 1) {
            return point.subtract(p2).getNorm(); //lies off p2
        } else {
            proj = p1.add(t, line);
            return point.subtract(proj).getNorm();
        }
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
     * Provides a string representation of this line.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Line{" + "p1=" + p1 + ", p2=" + p2 + '}';
    }
}
