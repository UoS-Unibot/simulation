package org.evors.core.util.geometry;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.util.LookupFunctions;

/**
 * The Polygon class represents a group of lines. Shape-Shape intersections for
 * collision detection can be calculated, and Shape-Line intersections (for e.g.
 * ray tracing) are provided.
 *
 * A Polygon can be built up by using the default constructors and adding lines
 * - much easier is the static factory method which will create a rectangle for
 * you.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Polygon implements Shape2D {

    private final LinkedList<Line> lines;
    private Vector2D center;
    private double rotation;

    /**
     * Creates an empty Shape2D() centred at the origin.
     */
    public Polygon() {
        this(Vector2D.ZERO);
    }

    /**
     * Creates an empty Shape2D at the specified center point.
     *
     * @param center Center point.
     */
    public Polygon(Vector2D center) {
        lines = new LinkedList<>();
        this.center = center;
    }

    /**
     * Creates a new rectangular Polygon with four boundary lines from the
     * specified center point.
     *
     * @param center Center point of rectangle.
     * @param size Size of rectangle.
     * @param rotation amount to rotate this rectangle by.
     * @return a rectangular Polygon
     */
    public static Polygon createRectangleFromCenter(Vector2D center,
            Vector2D size, double rotation) {
        Polygon shape = new Polygon(center);
        double halfW = size.getX() / 2,
                halfH = size.getY() / 2,
                x1 = center.getX() - halfW,
                y1 = center.getY() + halfH,
                x2 = center.getX() + halfW,
                y2 = center.getY() - halfH;
        shape.addLine(Line.fromCoords(x1, y1, x1, y2));
        shape.addLine(Line.fromCoords(x1, y2, x2, y2));
        shape.addLine(Line.fromCoords(x2, y2, x2, y1));
        shape.addLine(Line.fromCoords(x2, y1, x1, y1));
        shape.rotate(rotation);
        return shape;
    }

    /**
     * Returns a Java AWT geometry Shape as a polygon with the specified lines.
     * Any gaps between lines will be closed. Typically for drawing.
     *
     * @return
     */
    @Override
    public Shape toJava2DShape() {
        Path2D s = new Path2D.Double();
        for (Line line : lines) {
            s.append(line.toLine2D(), true);
        }
        s.closePath();
        return s;
    }

    /**
     * Adds a line to this Polygon.
     *
     * @param line
     */
    public void addLine(Line line) {
        lines.add(line);
    }

    /**
     * Gets whether this Polygon contains any lines.
     *
     * @return
     */
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /**
     * Rotates this Polygon around the center point by the given delta angle.
     *
     * @param deltaAngle Angle to rotate by in radians.
     */
    @Override
    public void rotate(double deltaAngle) {
        rotation = (rotation + deltaAngle) % (2 * Math.PI);
        for (Line line : lines) {
            line.rotate(center, deltaAngle);
        }
    }

    /**
     * Converts local coordinates specified relative to the center point to
     * world coordinates.
     *
     * @param localCoords coordinates relative to the center point; the center
     * point is at 0,0.
     * @return the world point of the local coordinates.
     */
    @Override
    public Vector2D getLocalToWorldCoords(Vector2D localCoords) {
        return getCenter().add(
                new Vector2D(
                        localCoords.getX() * LookupFunctions.cos(rotation),
                        localCoords.getY() * LookupFunctions.sin(rotation)
                )
        );
    }

    /**
     * Translates and rotates this Polygon by the specified amount.
     *
     * @param distance distance to move this Polygon in the current orientation
     * @param deltaAngle Angle in radians to change the orientation by.
     */
    @Override
    public void move(double distance, double deltaAngle) {
        translate(new Vector2D(distance * LookupFunctions.cos(getRotation()), distance
                * LookupFunctions.sin(getRotation())));
        rotate(deltaAngle);
    }

    /**
     * Translates this Polygon by the specified change vector. Does not take
     * rotation into account.
     *
     * @param deltaMovement Change vector for movement.
     */
    @Override
    public void translate(Vector2D deltaMovement) {
        center = center.add(deltaMovement);
        for (Line l : getLines()) {
            l.translate(deltaMovement);
        }
    }

    /**
     * Gets the collection of lines in this Polygon.
     *
     * @return
     */
    public Collection<Line> getLines() {
        return lines;
    }

    /**
     * Gets any intersections between the lines in this shape and the lines in
     * the shape provided.
     *
     * @param shape Other shape to check for intersections.
     * @return a list of intersecting LineIntersections; if there are no
     * intersections, an empty list.
     */
    public Collection<Vector2D> getIntersectionPoints(Polygon shape) {
        LinkedList<Vector2D> ips = new LinkedList<>();
        for (Line l1 : getLines()) {
            for (Line l2 : shape.getLines()) {
                Intersection.LineLine result = l1.getIntersection(l2);
                if (result.isIntersection) {
                    ips.add(result.intersectionPoint);
                }
            }
        }
        return ips;
    }

    /**
     * Gets the smallest intersection with the line specified, e.g. for ray
     * tracing.
     *
     * @param line Line to check for intersections.
     * @return the Intersection with the lowest segment distance.
     */
    @Override
    public Intersection getSmallestIntersection(Line line) {
        Intersection lowestLine = Intersection.noIntersection();
        for (Line shapeLine : getLines()) {
            Intersection.LineLine li = line.getIntersection(shapeLine);
            if (li.isIntersection) {
                if (!lowestLine.isIntersection) {
                    lowestLine = li;
                } else if (li.line1DistToIntersect
                        < lowestLine.getSmallestLineDist()) {
                    lowestLine = li;
                }
            }
        }
        return lowestLine;
    }

    /**
     * Gets the actual distance of the smallest line segment intersecting with
     * this shape, or NaN if there is no intersection.
     *
     * @param line Line to get intersections with.
     * @return the smallest intersection distance with the given line, or NaN if
     * no intersection.
     */
    public double getSmallestLineIntersectionDist(Line line) {
        Double lowestDist = Double.NaN;
        for (Line shapeLine : getLines()) {
            Intersection.LineLine li = line.getIntersection(shapeLine);
            if (li.isIntersection) {
                if (lowestDist.isNaN()) {
                    lowestDist = li.line1DistToIntersect;
                } else if (li.line1DistToIntersect < lowestDist) {
                    lowestDist = li.line1DistToIntersect;
                }
            }
        }
        return lowestDist;
    }

    /**
     * Returns whether this shape intersects with another shape.
     *
     * @param shape Other shape to check for intersections.
     * @return whether the two shapes intersect.
     */
    @Override
    public boolean intersectsWith(Shape2D shape) {
        if (shape instanceof Polygon) {
            Polygon poly = (Polygon)shape;
            for (Line l1 : getLines()) {
                for (Line l2 : poly.getLines()) {
                    if (l1.getIntersection(l2).isIntersection) {
                        return true;
                    }
                }
            }
        } else if (shape instanceof Circle) {
            Circle circle = (Circle)shape;
            if(circle.intersectsWith(this))
                return true;
        } else if(shape instanceof Line) {
            Line line = (Line)shape;
            for (Line shapeLine : lines) {
                if(shapeLine.getIntersection(line).isIntersection)
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the center point of this Polygon.
     *
     * @return
     */
    @Override
    public Vector2D getCenter() {
        return center;
    }

    /**
     * Sets the center point of this Polygon.
     *
     * @param center
     */
    @Override
    public void setCenter(Vector2D center) {
        this.center = center;
    }

    /**
     * Gets the current rotation of the shape.
     *
     * @return rotation in radians.
     */
    @Override
    public double getRotation() {
        return rotation;
    }

}
