package org.su.easy.unisim.sim.world;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a static rectangular WorldObj, with specified shape and size.
 * Immutable so that position, size and rotation are only set upon
 * instantiation.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class RectangleObj implements WorldObj {

    private Vector2D topLeftCorner, bottomRightCorner, position, size;
    private float rotation;
    private final ArrayList<Line2D> lines = new ArrayList<>();

    /**
     * Instantiates a new RectangleObj with specified size and rotation at a
     * specified position.
     *
     * @param position Vector2D position in world coordinates.
     * @param size Vector2D size in world coordinates.
     * @param rotation Angle to translate this object by in radians.
     */
    public RectangleObj(Vector2D position, Vector2D size, float rotation) {
        this.position = position;
        this.rotation = rotation;
        for (int i = 0; i < 4; i++) {
            lines.add(new Line2D.Float(0, 0, 0, 0));
        }
        this.size = size;
        updateElements();
    }

    /**
     * @return Gets a Java 2D geometry Rectangle2D: currently used for
     * rendering.
     */
    public Rectangle2D getRectangle() {
        return new Rectangle2D.Double(
                getTopLeftCorner().getX(),
                getTopLeftCorner().getY() - getSize().getY(),
                getSize().getX(),
                getSize().getY());
    }

    /**
     * Updates the positions/sizes of the corner coordinates and lines.
     */
    private void updateElements() {
        double halfW = getSize().getX() / 2.0, halfH = getSize().getY() / 2.0;
        topLeftCorner = getPosition().subtract(new Vector2D(halfW, -halfH));
        bottomRightCorner = getPosition().add(new Vector2D(halfW, -halfH));
        double x1 = getTopLeftCorner().getX(),
                x2 = getBottomRightCorner().getX(),
                y1 = getTopLeftCorner().getY(),
                y2 = getBottomRightCorner().getY(),
                w = getSize().getX(),
                h = getSize().getY();

        lines.get(0).setLine(x1, y1, x1 + w, y1);
        lines.get(1).setLine(x1, y1, x1, y1 - h);
        lines.get(2).setLine(x2, y2, x2, y2 + h);
        lines.get(3).setLine(x2, y2, x2 - w, y2);
    }

    @Override
    public ArrayList<Double> getLineIntersectionDists(Line2D line) {
        ArrayList<Double> points = new ArrayList<>();
        for (Line2D l : lines) {
            if (line.intersectsLine(l)) {
                points.add(l.ptSegDist(line.getX1(), line.getY1()));
            }
        }
        return points;
    }

    /**
     * @return Vector2D with the size, in metres, of this RectangleObj.
     */
    public Vector2D getSize() {
        return size;
    }

    /**
     * @return the world coordinates of the top left corner of the rectangle.
     */
    public Vector2D getTopLeftCorner() {
        return topLeftCorner;
    }

    /**
     * @return the world coordinates of the bottom right corner of the
     * rectangle.
     */
    public Vector2D getBottomRightCorner() {
        return bottomRightCorner;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public boolean intersectsWithArea(Area area) {
        return area.intersects(getRectangle());
    }

}
