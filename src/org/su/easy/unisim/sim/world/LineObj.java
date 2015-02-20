package org.su.easy.unisim.sim.world;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a line object in the world. For the purposes of the simulation it
 * has zero width. Currently used to represent world bounds.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class LineObj implements WorldObj {

    private float rotation;
    private Vector2D position;
    private final Line2D line = new Line2D.Float();

    /**
     * Instantiates a new LineObj with specified position, length and rotation.
     * The line is projected symmetrically outwards from the specified center
     * point.
     *
     * @param position Center point of this line in world coordinates.
     * @param length Length, in metres, of this line.
     * @param rotation Angle to translate this line by (pivoting around the
     * position) in radians
     */
    public LineObj(Vector2D position, float length, float rotation) {
        this.rotation = rotation;
        this.position = position;
        float hL = length / 2;
        float cL = (float) (Math.cos(rotation) * hL);
        float sL = (float) (Math.sin(rotation) * hL);
        line.setLine(
                position.getX() - cL,
                position.getY() - sL,
                position.getX() + cL,
                position.getY() + sL
        );
    }

    /**
     * @return Gets the Java2D geometry Line2D object. Currently used for
     * rendering.
     */
    public Line2D getLine() {
        return line;
    }

    /**
     * @return the world coordinates of the center point of this line.
     */
    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public ArrayList<Double> getLineIntersectionDists(Line2D line2) {
        ArrayList<Double> arr = new ArrayList<>(1);
        arr.add(line.ptSegDist(line2.getX1(), line2.getY1()));
        return arr;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

}
