package org.evors.core.util.geometry;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.LookupTable;
import static java.lang.Math.abs;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.util.LookupFunctions;

/**
 * Represents a regular circle with a center point and radius.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Circle implements Shape2D {

    /**
     * Creates a new Circle with center point specified as a Vector2D and a
     * radius.
     *
     * @param centerPoint Vector2D of center point.
     * @param radius Radius. Must be nonnegative.
     * @return A new Circle with specified parameters.
     */
    public static Circle getFromCenter(Vector2D centerPoint, double radius) {
        return new Circle(centerPoint, radius);
    }

    /**
     * Creates a new Circle with center point specified as coordinates and a
     * radius.
     *
     * @param centerX X coordinate of center point.
     * @param centerY Y coordinate of center point.
     * @param radius Radius. Must be nonnegative.
     * @return A new Circle with specified parameters.
     */
    public static Circle getFromCenter(double centerX, double centerY,
            double radius) {
        return getFromCenter(new Vector2D(centerX, centerY), radius);
    }

    private Circle(Vector2D center, double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be nonnegative");
        }
        this.center = center;
        this.radius = radius;
    }

    private Vector2D center;
    private final double radius;
    private double rotation;

    @Override
    public Vector2D getCenter() {
        return center;
    }

    /**
     * Gets the radius of this circle.
     *
     * @return
     */
    public double getRadius() {
        return radius;
    }

    @Override
    public Vector2D getLocalToWorldCoords(Vector2D localCoords) {
        return getCenter().add(
                new Vector2D(
                        localCoords.getX() * LookupFunctions.cos(rotation),
                        localCoords.getY() * LookupFunctions.sin(rotation)
                )
        );
    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public boolean intersectsWith(Shape2D shape) {
        if (shape instanceof Circle) {
            Circle circle2 = (Circle) shape;
            double centerDists = circle2.getCenter().subtract(getCenter()).
                    getNorm();
            double radii = circle2.getRadius() + getRadius();
            if (abs(centerDists) < radii) {
                return true;
            }
        } else if (shape instanceof Line) {
            Line line = (Line) shape;
            if (intersectsWithLine(line)) {
                System.out.println("intersection, circle at " + getCenter().
                        toString() + ",line at " + line.toString());
            }
            return intersectsWithLine(line);
        } else if (shape instanceof Polygon) {
            Polygon poly = (Polygon) shape;
            for (Line l : poly.getLines()) {
                if (intersectsWithLine(l)) {
                    System.out.println("intersection, circle at " + getCenter().
                            toString() + ",line at " + l.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean intersectsWithLine(Line line) {
        return line.getShortestDistToPoint(center) < radius;
    }

    @Override
    public void move(double distance, double deltaAngle) {
        translate(new Vector2D(distance * LookupFunctions.cos(getRotation()),
                distance
                * LookupFunctions.sin(getRotation())));
        rotate(deltaAngle);
    }

    @Override
    public void rotate(double deltaAngle) {
        rotation += deltaAngle;
    }

    @Override
    public void setCenter(Vector2D center) {
        this.center = center;
    }

    @Override
    public Shape toJava2DShape() {
        Ellipse2D el = new Ellipse2D.Double();
        double cornerD = Math.sqrt(2 * radius * radius);
        el.setFrameFromCenter(center.getX(), center.getY(), center.getX()
                - cornerD, center.getY() - cornerD);
        return el;
    }

    @Override
    public void translate(Vector2D deltaMovement) {
        center = center.add(deltaMovement);
    }

    @Override
    public Intersection getSmallestIntersection(
            Line line) {
        return Intersection.CircleLine.calculate(this, line);
    }

}
