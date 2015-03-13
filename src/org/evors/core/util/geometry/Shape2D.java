package org.evors.core.util.geometry;

import java.awt.Shape;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The Shape2D represents an abstract shape which can be moved around, rotated
 * and checked for intersections with other shapes.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface Shape2D {

    /**
     * Gets the center point of this Polygon.
     *
     * @return
     */
    Vector2D getCenter();

    /**
     * Converts local coordinates specified relative to the center point to
     * world coordinates.
     *
     * @param localCoords coordinates relative to the center point; the center
     * point is at 0,0.
     * @return the world point of the local coordinates.
     */
    Vector2D getLocalToWorldCoords(Vector2D localCoords);

    /**
     * Gets the current rotation of the shape.
     *
     * @return rotation in radians.
     */
    double getRotation();

    /**
     * Returns whether this shape intersects with another shape.
     *
     * @param shape Other shape to check for intersections.
     * @return whether the two shapes intersect.
     */
    boolean intersectsWith(Shape2D shape);

    /**
     * Translates and rotates this Polygon by the specified amount.
     *
     * @param distance distance to move this Polygon in the current orientation
     * @param deltaAngle Angle in radians to change the orientation by.
     */
    void move(double distance, double deltaAngle);

    /**
     * Rotates this Polygon around the center point by the given delta angle.
     *
     * @param deltaAngle Angle to rotate by in radians.
     */
    void rotate(double deltaAngle);

    /**
     * Sets the center point of this Polygon.
     *
     * @param center
     */
    void setCenter(Vector2D center);

    /**
     * Returns a Java AWT geometry Shape as a polygon with the specified lines.
     * Any gaps between lines will be closed. Typically for drawing.
     *
     * @return
     */
    Shape toJava2DShape();

    /**
     * Translates this Polygon by the specified change vector. Does not take
     * rotation into account.
     *
     * @param deltaMovement Change vector for movement.
     */
    void translate(Vector2D deltaMovement);

    /**
     *
     * @param rangeFinderLine
     * @return
     */
    public Intersection getSmallestIntersection(
            Line rangeFinderLine);

}
